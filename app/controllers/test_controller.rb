class TestController < ApplicationController
	def test

		instruction = Instruction.where(lesson_id: 3)

		response = {}

		response[:instructions] = instruction

		render json: response
	end
end