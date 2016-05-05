class TestController < ApplicationController
	def test

		instruction = Instruction.all

		response = {}

		response[:instructions] = instruction

		render json: response
	end
end