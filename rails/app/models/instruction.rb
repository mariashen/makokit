class Instruction < ActiveRecord::Base
	default_scope { order('display_index ASC') }
	belongs_to :lesson
	has_many :answers
	has_many :answer_jumps, dependent: :destroy
	has_many :arriving_answers, through: :answer_jumps, class_name: 'Answer'
	
	# add image attachment
	has_attached_file :avatar, 
                    styles: { :medium => "200x200>", :thumb => "100x100>" }
                    # :storage => :s3,
                    # :s3_credentials => "#{Rails.root}/config/secrets.yml",
  	validates_attachment_content_type :avatar, :content_type => /^image\/(png|gif|jpeg|jpg|.gif)/
	after_create do 
		last_index = self.collection.last_index
		self.update_attribute(:display_index, last_index) 
		self.collection.find_by(display_index: last_index - 1).update_attribute(:next_instruction_id, self.id) if last_index > 0
	end
	after_destroy { self.collection.reorder_indices }

	

	def update_attributes(attributes)
		self.update(attributes)

		if attributes[:display_index]
			new_index = attributes[:display_index].to_i
			previous_instruction = nil

			# take out at the end the record we are trying to change
			self.update_attribute(:display_index, self.collection.count) 
			i = 0
			# reloads collection with the new order (record at the end)
			self.collection.each do |inst|
				previous_instruction.update_attribute(:next_instruction_id, inst.id) if previous_instruction
				i += 1 if i == new_index #makes space for the new record
				inst.update_attribute(:display_index, i) # reorders everything
				i += 1
				previous_instruction = inst
			end
			self.update_attribute(:display_index, new_index)
		end
	end


	#might not be useful
	def available_indices_to_move_to
		a = (0..self.collection.last_index).to_a
		a.delete(self.display_index)
		return a
	end

	def previous_instruction
		Instruction.find_by(lesson_id: self.lesson_id, next_instruction_id: self.id)
	end

	def next_instruction
		Instruction.find_by(id: self.next_instruction_id)
	end

	def collection
		self.lesson.instructions
	end


end
