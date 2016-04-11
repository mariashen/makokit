class Api::LessonsController < ApplicationController
	skip_before_filter :verify_authenticity_token

	def show
		@lesson = Lesson.find(params[:id])	
		@instructions = @lesson.instructions
		@answers = Answer.where( instruction_id: @instructions.pluck(:id) )
		puts @answers.inspect
		@jumps = AnswerJump.where( answer_id: @answers.pluck(:id) )
		response = {}

		response[:lesson] = @lesson
		response[:instructions] = Hash[@instructions.map{|i| [i.id, i ] }]
		response[:answers] = Hash[@answers.map{|a| [a.id, a ] }]
		response[:jumps] = Hash[@jumps.map{|j| [j.id, j ] }]

		render json: response
	end


	def index
		render json: Lesson.all
	end



end
