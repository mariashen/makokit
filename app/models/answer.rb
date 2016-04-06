class Answer < ActiveRecord::Base
	belongs_to :instruction
	has_one :lesson, through: :instruction
	has_one :jump, class_name: "AnswerJump", dependent: :destroy

	def next_instruction
		self.jump.instruction if self.jump
	end

	def incorrect?
		self.correct == false
	end

end
