
window.addEventListener('load', function(){
	console.log('content.js start');

	var dataDiv = document.querySelector('div#DroidMotion');

	if(dataDiv == null || dataDiv.length<1){
		console.log('do nothing');
		return ;
	}
	
	console.log('fond it');


	var sendDroidEvent =  function(name, data){
		var myEvent = document.createEvent('CustomEvent');

		myEvent.initCustomEvent('droid_' + name,true,false,data);

		dataDiv.dispatchEvent(myEvent);

		console.log('built & sent a [ droid_' + name + ' ] event');
	}


	var handler = {
		verify : function(type){
			return handler[type] && typeof(handler[type]) == 'function';
		},
		event :function(msg){
			console.log("from bg : event ", msg.data);
			sendDroidEvent(msg.name, msg.data);

		},
		accept:function(msg){
			console.log("from bg : id ", msg.data.id);
			sendDroidEvent(msg.name, msg.data);
		}
	}

	var port = chrome.extension.connect({name: "backgroud"});

	port.postMessage({type: "new"});

	port.onMessage.addListener(function(msg) {

		if(handler.verify(msg.type))
			handler[msg.type](msg);
		else
			console.log("invalid : " + msg);
	});

});