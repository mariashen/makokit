class LessonsController < ApplicationController

	
	def dashboard
	end

	def new
		@lesson = Lesson.new
	end

	def create
		@lesson = Lesson.new(lesson_params)
		if @lesson.save
			redirect_to lesson_path(@lesson)
		else
			render 'new'
		end
	end

	def edit
		@lesson = Lesson.find(params[:id])
		@new_instruction = Instruction.new()
		respond_to do |format|
			format.html
			format.js
		end
	end


	def update
		@lesson = Lesson.find(params[:id])
		@lesson.update_attributes(lesson_params)
		redirect_to lesson_path(@lesson)
	end


	def show
		@lesson = Lesson.find(params[:id])
		@new_instruction = Instruction.new()
		respond_to do |format|
			format.html
			format.js
		end
	end

	def index
		@lessons = Lesson.all
	end



	private


	def lesson_params
		params.require(:lesson).permit(:name, :category, :description, :image_url)
	end

end
