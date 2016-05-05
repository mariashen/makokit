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
		messaging_events = request_body['entry'][0]['messaging']
		puts messaging_events
		# for i in 0..(messaging_events.count-1)
		# 	event = request_body['entry'][0]['messaging'][i]
		# 	sender = event['sender']['id']
		# 	if event['message'] and event['message']['text']
		# 		if event['message']['text'] == "lesson"
		# 			sendAllLessons(sender)
		# 		else
		# 			sendTextMessage(sender, "hello")
		# 		end
		# 	end
		# 	# if event['postback'] and event['postback']['payload']
		# 	# 	text = ['postback']['payload']
		# 	# 	sendTextMessage(sender, text)
		# 	# end
		# end

		render plain: request_body
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

		def sendAllLessons(sender)
			token = 'EAADrxm348aEBAIyMoDh1rf3lScB2NCOWWm9IUx9H1AAZB9nWyEFwV5vJ98ejHeJC0Mcpp3DhZCS5yFvAiYU3qwmXXMh3lDt2QaFAr43Tik3ybHhooF3d8WFw97loxWyn9C4Bg0XOJecsrHhAAoHv5IJAcKms5y6fMtzrtiWgZDZD'
			url = 'https://graph.facebook.com/v2.6/me/messages?access_token=' + token

			lessons = Lesson.all
			elementsData = []
			lessons.each do |l|
				next if l.image_url == ''

				# instruction = Instructions.order(display_index: :asc).find_by lesson_id: l.id

				lessonData = {:title => l.name, 
				:subtitle => l.description, 
				:image_url => l.image_url, 
				:buttons => [{:type => "postback", :title => "Learn This!", :payload => instruction.text}]}

				elementsData.push(lessonData)
			end

			recipientData = {:id => sender}
			payloadData = {:template_type => "generic", :elements => elementsData}
			messageData = {:type => "template", :payload => payloadData}
			attachmentData = {:attachment => messageData}
			response = Unirest.post url, 
                        headers:{ "Content-Type" => "application/json" }, 
                        parameters:{ :recipient => recipientData, :message => attachmentData }.to_json
		end
end
