class TestController < ApplicationController
	def test

		instruction = Instruction.where(lesson_id: 3).order(:display_index).take

		response = {}

		response[:instructions] = instruction

		render plain: instruction.text
	end
end