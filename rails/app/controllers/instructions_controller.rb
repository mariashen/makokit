class InstructionsController < ApplicationController


	def create
		@instruction = Instruction.new(instruction_params)
		@instruction.save
		@lesson = @instruction.lesson
		respond_to do |format|
			format.html { redirect_to lesson_path(@lesson) }
			format.js { @new_instruction = Instruction.new() }
		end
	end

	def edit
		@instruction = Instruction.find(params[:id])
		respond_to do |format|
			format.html
			format.js
		end
	end


	def update
		@instruction = Instruction.find(params[:id])
		@instruction.update_attributes(instruction_params)
		respond_to do |format|
			format.html { redirect_to lesson_path( @instruction.lesson) }
			format.js { redirect_to lesson_path(@instruction.lesson), status: 303}
		end
	end



	def destroy
		@instruction = Instruction.find(params[:id]).destroy
		respond_to do |format|
			format.html { redirect_to lesson_path( instruction.lesson) }
			format.js { redirect_to lesson_path(@instruction.lesson), status: 303 }
		end
	end



	private

		def instruction_params
			params.require(:instruction).permit(:lesson_id, :text, :image_url, :video_url, :display_index, :avatar)
		end
end
