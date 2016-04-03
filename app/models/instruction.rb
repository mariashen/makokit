class Instruction < ActiveRecord::Base
	belongs_to :lesson
	has_many :answers
	has_many :answer_jumps, dependent: :destroy
	has_many :arriving_answers, through: :answer_jumps, class_name: 'Answer'
end
