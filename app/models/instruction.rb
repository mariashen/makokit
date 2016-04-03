class Instruction < ActiveRecord::Base
	belongs_to :lesson
	has_many :answers
	has_many :answer_jumps, dependent: :destroy
	has_many :arriving_answers, through: :answer_jumps, class_name: 'Answer'
	#link list management
	attr_accessor :previous_instruction_id
	before_create { self.previous_instruction_id = self.lesson.instructions.last.id }
	after_create { Instruction.find(self.previous_instruction_id).update_attribute(:next_instruction_id, self.id) }
	after_destroy do  
		inst = Instruction.find_by_next_instruction_id(self.id)
		inst.update_attribute(:next_instruction_id, self.next_instruction_id) if inst 
	end



end
