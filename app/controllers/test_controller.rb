class TestController < ApplicationController
	def test

		instruction = Instruction.where(lesson_id: 3).order(:display_index).take

		response = {}

		response[:instructions] = instruction

		render json: response
	end
end