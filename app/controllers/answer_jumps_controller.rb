class AnswerJumpsController < ApplicationController

  def create
  	jump = AnswerJump.create(jump_params)
  	redirect_to edit_answer_path(jump.answer)
  end

  def destroy
  	jump = AnswerJump.find(params[:id]).destroy
  	redirect_to edit_answer_path(jump.answer)
  end

  def update
  	jump = AnswerJump.find(params[:id]).destroy
  	jump.update_attributes(jump_params)
  	redirect_to edit_answer_path(jump.answer)
  end

	private


	def jump_params
		params.require(:answer_jump).permit(:answer_id, :instruction_id)
	end

end
