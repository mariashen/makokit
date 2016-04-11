class AnswerJump < ActiveRecord::Base
	validates :answer_id, presence: true, uniqueness: true
	validates :instruction_id, presence: true
	belongs_to :instruction
	belongs_to :answer
end
