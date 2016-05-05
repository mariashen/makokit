class TestController < ApplicationController
	def test

		instruction = Instruction.order(display_index: :asc).find_by lesson_id: l.id
		render plain: instruction
	end
end