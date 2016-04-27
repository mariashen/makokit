class WebhookController < ApplicationController


	def permission
		if params['hub.verify_token'] == 'verify_mako'
			render plain: params['hub.challenge']
		else
			render plain: 'Error, wrong validation token'
		end
	end

	def receive
	end
	
	# private

	# 	def instruction_params
	# 		params.require(:instruction).permit(:lesson_id, :text, :image_url, :video_url, :display_index)
	# 	end
end
