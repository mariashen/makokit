class TestController < ApplicationController
	def test

		answer = Answer.where(instruction_id: 17).all

		# response = {}

		# response[:instructions] = instruction
		if answer
			render plain: answer.text
		else
			render plain: 'nothing to see here'
		end
	end
end