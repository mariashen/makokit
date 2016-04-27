class WebhookController < ApplicationController

	require 'unirest'
	require 'json'

	skip_before_action :verify_authenticity_token

	def permission
		if params['hub.verify_token'] == 'verify_mako'
			render plain: params['hub.challenge']
		else
			render plain: 'Error, wrong validation token'
		end
	end

	def receive
		request_body = JSON.parse(request.body.read)
		messaging_events = request_body.entry[0].messaging
		for i in 0..messaging_events.count
			event = request_body.entry[0].messaging[i]
			sender = event.sender.id
			if event.message and event.message.text
				sendTextMessage(sender, "hello")
			end
		end

		render status: :ok
	end
	
	private

		def sendTextMessage(sender, text)
			token = 'EAADrxm348aEBAIyMoDh1rf3lScB2NCOWWm9IUx9H1AAZB9nWyEFwV5vJ98ejHeJC0Mcpp3DhZCS5yFvAiYU3qwmXXMh3lDt2QaFAr43Tik3ybHhooF3d8WFw97loxWyn9C4Bg0XOJecsrHhAAoHv5IJAcKms5y6fMtzrtiWgZDZD'
			url = 'https://graph.facebook.com/v2.6/me/messages?access_token=' + token
			recipientData = {:id => sender}.to_json
			messageData = {:text => text}.to_json
			response = Unirest.post url, 
                        headers:{ "Accept" => "application/json" }, 
                        parameters:{ :recipient => recipientData, :message => messageData }.to_json
		end
end
