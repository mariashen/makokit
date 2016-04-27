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
		# puts request.body.read
		# returnee = 'nope'
		puts "Hello, logs!"
		request_body = JSON.parse(request.body.read)
		messaging_events = request_body['entry'][0]['messaging']
		# event = request_body['entry'][0]['messaging'][0]
		for i in 0..(messaging_events.count-1)
			# returnee = i.to_s
			event = request_body['entry'][0]['messaging'][i]
			sender = event['sender']['id']
			puts sender
			if event['message'] and event['message']['text']
				sendTextMessage(sender, "hello")
				# returnee = 'hello'
			end
		end

		render plain: ''
	end
	
	private

		def sendTextMessage(sender, text)
			token = 'EAADrxm348aEBAIyMoDh1rf3lScB2NCOWWm9IUx9H1AAZB9nWyEFwV5vJ98ejHeJC0Mcpp3DhZCS5yFvAiYU3qwmXXMh3lDt2QaFAr43Tik3ybHhooF3d8WFw97loxWyn9C4Bg0XOJecsrHhAAoHv5IJAcKms5y6fMtzrtiWgZDZD'
			url = 'https://graph.facebook.com/v2.6/me/messages?access_token=' + token
			recipientData = {:id => sender}
			messageData = {:text => text}
			response = Unirest.post url, 
                        headers:{ "Content-Type" => "application/json" }, 
                        parameters:{ :recipient => recipientData, :message => messageData }.to_json
		end
end
