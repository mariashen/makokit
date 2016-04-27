class WebhookController < ApplicationController


	def permission

		render json: "response"
	end

	def receive
	end
	
	# private

	# 	def instruction_params
	# 		params.require(:instruction).permit(:lesson_id, :text, :image_url, :video_url, :display_index)
	# 	end
end
