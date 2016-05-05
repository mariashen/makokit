class TestController < ApplicationController
	def test

		# answer = Answer.where(instruction_id: 17).take

		# response = {}

		# response[:instructions] = instruction

		answers = []
		if Answer.exists?(instruction_id: 18)
			Answer.where(instruction_id: 18).each do |ans|
				answer = {}
				if AnswerJump.exists?(answer_id: ans.id)
					jump = AnswerJump.where(answer_id: ans.id).take
					answer = {:text => ans.text, :payload => jump.instruction_id.to_s}
				else
					answer = {:text => ans.text, :payload => "19"}
				end
				answers.push(answer)
			end

		else
			answers = [{:text => "Next", :payload => "19"}]
			# render.plain
		end

		render plain: 'answer'
	end
end