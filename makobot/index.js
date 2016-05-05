var express = require('express');
var bodyParser = require('body-parser');
var request = require('request');

var app = express();
var jsonParser = bodyParser.json();

app.set('port', (process.env.PORT || 5000));

app.use(express.static(__dirname + '/public'));

// views is directory for all template files
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');

app.get('/', function(request, response) {
  response.render('pages/index');
});

app.listen(app.get('port'), function() {
  console.log('Node app is running on port', app.get('port'));
});

app.get('/webhook/', function (req, res) {
  if (req.query['hub.verify_token'] === 'verify_mako') {
    res.send(req.query['hub.challenge']);
  }
  res.send('Error, wrong validation token');
});

app.post('/webhook/', jsonParser, function(req, res) {
  // console.log(req.body.entry[0].messaging);
  messaging_events = req.body.entry[0].messaging;
  for (i = 0; i < messaging_events.length; i++) {
    event = req.body.entry[0].messaging[i];
    sender = event.sender.id;
    if (event.message && event.message.text) {
      text = event.message.text;
      if (text === 'lesson') {
	    sendGenericMessage(sender);
	    continue;
	  }
	  else if (text === 'gif') {
	  	sendTextMessage(sender, "http://gph.is/245jDa3");
	  	continue;
	  }
	  else {
      	sendTextMessage(sender, "Text received, echo: "+ text.substring(0, 200));
      	continue;
      }
    }
    if (event.message && event.message.sticker_id) {
      sticker_id = event.message.sticker_id;
      if (sticker_id === 369239343222814 || sticker_id === 369239263222822 || sticker_id === 369239383222810) {
      	sendTextMessage(sender, "Liked");
      	continue;
      }
    }
    if (event.postback && event.postback.payload) {
    	text = event.postback.payload;
      	sendTextMessage(sender, "Text received, echo: "+ text.substring(0, 200));
    }
  }
  res.sendStatus(200);
});

var token = "EAADrxm348aEBAIyMoDh1rf3lScB2NCOWWm9IUx9H1AAZB9nWyEFwV5vJ98ejHeJC0Mcpp3DhZCS5yFvAiYU3qwmXXMh3lDt2QaFAr43Tik3ybHhooF3d8WFw97loxWyn9C4Bg0XOJecsrHhAAoHv5IJAcKms5y6fMtzrtiWgZDZD";

function sendTextMessage(sender, text) {
  messageData = {
    text:text
  }
  request({
    url: 'https://graph.facebook.com/v2.6/me/messages',
    qs: {access_token:token},
    method: 'POST',
    json: {
      recipient: {id:sender},
      message: messageData,
    }
  }, function(error, response, body) {
    if (error) {
      console.log('Error sending message: ', error);
    } else if (response.body.error) {
      console.log('Error: ', response.body.error);
    }
  });
}

function sendGenericMessage(sender) {
  messageData = {
    "attachment": {
      "type": "template",
      "payload": {
        "template_type": "generic",
        "elements": [{
          "title": "K'Nex Wheelbarrow",
          "subtitle": "Learn about lever classes and pulley systems!",
          "image_url": "https://makokit.herokuapp.com/img/sample/knex.jpg",
          "buttons": [{
            "type": "web_url",
            "url": "https://makokit.herokuapp.com",
            "title": "Go to Website"
          }, {
            "type": "postback",
            "title": "Learn This!",
            "payload": "Learn This!",
          }],
        },{
          "title": "Cooking Chemistry",
          "subtitle": "Fry your eggs like you know what you're doing!",
          "image_url": "https://makokit.herokuapp.com/img/sample/cooking.jpg",
          "buttons": [{
            "type": "web_url",
            "url": "https://makokit.herokuapp.com",
            "title": "Go to Website"
          }, {
            "type": "postback",
            "title": "Learn This!",
            "payload": "Learn This!",
          }],
        }]
      }
    }
  };
  request({
    url: 'https://graph.facebook.com/v2.6/me/messages',
    qs: {access_token:token},
    method: 'POST',
    json: {
      recipient: {id:sender},
      message: messageData,
    }
  }, function(error, response, body) {
    if (error) {
      console.log('Error sending message: ', error);
    } else if (response.body.error) {
      console.log('Error: ', response.body.error);
    }
  });
}

