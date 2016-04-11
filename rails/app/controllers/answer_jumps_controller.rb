class AnswerJumpsController < ApplicationController

  
  def new
    @answer = Answer.find(params[:answer_id])
    @jump = AnswerJump.new()
  end

  def create
    jump = AnswerJump.create(jump_params)
    @answer = jump.answer
    respond_to do |format|
      format.html { redirect_to edit_answer_path(jump.answer) }
      format.js
    end
  end

  def edit
    @jump = AnswerJump.find(params[:id])
    @answer = @jump.answer
  end

  def update
    @jump = AnswerJump.find(params[:id])
    @jump.update_attributes(jump_params)
    @answer = @jump.answer
    respond_to do |format|
      format.html { redirect_to edit_answer_path(@answer) }
      format.js
    end
  end

  def destroy
  	jump = AnswerJump.find(params[:id]).destroy
    @answer = jump.answer
    respond_to do |format|
      format.html { redirect_to edit_answer_path(@answer) }
      format.js
    end
  end

  

	private


	def jump_params
		params.require(:answer_jump).permit(:answer_id, :instruction_id)
	end

end
