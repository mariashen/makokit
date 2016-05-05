class WebhookController < ApplicationController

	require 'unirest'
	require 'json'
	require 'fuzzystringmatch'

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
		# puts messaging_events
		for i in 0..(messaging_events.count-1)
			event = request_body['entry'][0]['messaging'][i]
			sender = event['sender']['id']
			if event['message'] and event['message']['text']
				if event['message']['text'] == "lesson" or event['message']['text'] == "Lesson"
					sendTextMessage(sender, "Here are our most recent lessons")
					sendAllLessons(sender)
				elsif  event['message']['text'] == "help"
					help = "Tell Mako what you want to learn. Or type 'lesson' to see most recent lessons"
					sendTextMessage(sender, help)
				else
					sendTextMessage(sender, "Here are the lessons that matched '" + event['message']['text'] + "'")
					sendLessons(sender, event['message']['text'])
				end
			end
			if event['postback'] and event['postback']['payload']
				instruction_id = event['postback']['payload'].to_i
				instruction = Instruction.find(instruction_id)
				answers = []
				if Answer.exists?(instruction_id: instruction.id)
					Answer.where(instruction_id: instruction.id).each do |ans|
						answer = {}
						if AnswerJump.exists?(answer_id: ans.id)
							jump = AnswerJump.where(answer_id: ans.id).take
							answer = {:text => ans.text, :payload => jump.instruction_id.to_s}
						else
							answer = {:text => ans.text, :payload => instruction.next_instruction_id.to_s}
						end
						answers.push(answer)
					end
				else
					answers = [{:text => "Next", :payload => instruction.next_instruction_id.to_s}]
				end
				# answers = [{:text => "Next", :payload => instruction.next_instruction_id.to_s}]
				
				unless instruction.image_url.empty?
					sendImage(sender, instruction.image_url)
				end
				sendButtons(sender, instruction.text, answers)
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
		jarow = FuzzyStringMatch::JaroWinkler.create( :native )
p jarow.getDistance(  "jones",      "johnson" )

		def sendImage(sender, image_url)
			token = 'EAADrxm348aEBAIyMoDh1rf3lScB2NCOWWm9IUx9H1AAZB9nWyEFwV5vJ98ejHeJC0Mcpp3DhZCS5yFvAiYU3qwmXXMh3lDt2QaFAr43Tik3ybHhooF3d8WFw97loxWyn9C4Bg0XOJecsrHhAAoHv5IJAcKms5y6fMtzrtiWgZDZD'
			url = 'https://graph.facebook.com/v2.6/me/messages?access_token=' + token

			recipientData = {:id => sender}
			messageData = {:type => "image", :payload => {:url => image_url}}
			attachmentData = {:attachment => messageData}
			response = Unirest.post url, 
                        headers:{ "Content-Type" => "application/json" }, 
                        parameters:{ :recipient => recipientData, :message => attachmentData }.to_json
		end

		def sendButtons(sender, text, answers)
			token = 'EAADrxm348aEBAIyMoDh1rf3lScB2NCOWWm9IUx9H1AAZB9nWyEFwV5vJ98ejHeJC0Mcpp3DhZCS5yFvAiYU3qwmXXMh3lDt2QaFAr43Tik3ybHhooF3d8WFw97loxWyn9C4Bg0XOJecsrHhAAoHv5IJAcKms5y6fMtzrtiWgZDZD'
			url = 'https://graph.facebook.com/v2.6/me/messages?access_token=' + token

			buttons = []
			answers.each do |a|
				buttonData = {:type => "postback", :title => a[:text], :payload => a[:payload]}
				buttons.push(buttonData)
			end
			recipientData = {:id => sender}
			payloadData = {:template_type => "button", :text => text, :buttons => buttons}
			messageData = {:type => "template", :payload => payloadData}
			attachmentData = {:attachment => messageData}
			response = Unirest.post url, 
                        headers:{ "Content-Type" => "application/json" }, 
                        parameters:{ :recipient => recipientData, :message => attachmentData }.to_json
		end

		def sendLessons(sender, lessonText)
			token = 'EAADrxm348aEBAIyMoDh1rf3lScB2NCOWWm9IUx9H1AAZB9nWyEFwV5vJ98ejHeJC0Mcpp3DhZCS5yFvAiYU3qwmXXMh3lDt2QaFAr43Tik3ybHhooF3d8WFw97loxWyn9C4Bg0XOJecsrHhAAoHv5IJAcKms5y6fMtzrtiWgZDZD'
			url = 'https://graph.facebook.com/v2.6/me/messages?access_token=' + token

			jarow = FuzzyStringMatch::JaroWinkler.create( :native )
			# jarow.getDistance("jones", "johnson" )
			lessons = Lesson.order(id: :desc)
			elementsData = []
			lessons.each do |l|
				next if l.image_url == '' or jarow.getDistance(l.name, lessonText) < 0.6

				instruction = Instruction.where(lesson_id: l.id).order(:display_index).take
				# payload = "text:#{instruction.text},,,next:#{instruction.next_instruction_id},,,image_url:#{instruction.image_url}"
				lessonData = {:title => l.name, 
				:subtitle => l.description, 
				:image_url => l.image_url, 
				:buttons => [{:type => "postback", :title => "Learn This!", :payload => instruction.id.to_s}]}

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

		def sendAllLessons(sender)
			token = 'EAADrxm348aEBAIyMoDh1rf3lScB2NCOWWm9IUx9H1AAZB9nWyEFwV5vJ98ejHeJC0Mcpp3DhZCS5yFvAiYU3qwmXXMh3lDt2QaFAr43Tik3ybHhooF3d8WFw97loxWyn9C4Bg0XOJecsrHhAAoHv5IJAcKms5y6fMtzrtiWgZDZD'
			url = 'https://graph.facebook.com/v2.6/me/messages?access_token=' + token

			lessons = Lesson.order(id: :desc)
			elementsData = []
			lessons.each do |l|
				next if l.image_url == ''

				instruction = Instruction.where(lesson_id: l.id).order(:display_index).take
				# payload = "text:#{instruction.text},,,next:#{instruction.next_instruction_id},,,image_url:#{instruction.image_url}"
				lessonData = {:title => l.name, 
				:subtitle => l.description, 
				:image_url => l.image_url, 
				:buttons => [{:type => "postback", :title => "Learn This!", :payload => instruction.id.to_s}]}

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
