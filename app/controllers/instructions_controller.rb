class InstructionsController < ApplicationController


	def create
		instruction = Instruction.new(instruction_params)
		instruction.save
		redirect_to edit_lesson_path(instruction.lesson)
	end



	def destroy
		@instruction = Instruction.find(params[:id]).destroy
		respond_to do |format|
			format.html { redirect_to edit_lesson_path( instruction.lesson) }
			format.js
		end
	end



	private

		def instruction_params
			params.require(:instruction).permit(:lesson_id, :text, :image_url, :video_url)
		end
end
