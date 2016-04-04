class AnswersController < ApplicationController


	def new
		@instruction = Instruction.find(params[:instruction_id])
		@answer = @instruction.answers.build
		respond_to do |format|
			format.html
			format.js
		end
	end

	def create
		@instruction = Instruction.find(params[:instruction_id])
		@answer = @instruction.answers.build(answer_params)
		@answer.save
		redirect_to edit_lesson_path(@answer.instruction.lesson), status: 303
	end


	def edit
		@answer = Answer.find(params[:id])	
		@instruction = @answer.instruction
		@jump = @answer.jump
		@jump ||= AnswerJump.new()
		respond_to do |format|
			format.html
			format.js
		end
	end


	def update
		@answer = Answer.find(params[:id])	
		@answer.update_attributes(answer_params)
		respond_to do |format|
			format.html { redirect_to edit_lesson_path(@answer.instruction.lesson) }
			format.js
		end
	end


	def destroy
		@answer = Answer.find(params[:id]).destroy
		respond_to do |format|
			format.html { redirect_to edit_lesson_path(@answer.instruction.lesson) }
			format.js
		end
	end



	private

		def answer_params
			params.require(:answer).permit(:text, :image_url, :correct)
		end
end
