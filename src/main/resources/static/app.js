var app = (function () {

    class Point{
        constructor(x,y){
            this.x=x;
            this.y=y;
        }        
    }
    
    var stompClient = null;			
	var identi = null;
	
    var addPointToCanvas = function (point) {        
        var canvas = document.getElementById("canvas");
        var ctx = canvas.getContext("2d");
        ctx.beginPath();
        ctx.arc(point.x, point.y, 3, 0, 2 * Math.PI);
        ctx.stroke();   
        var message = {x:point.x, y:point.y};     
        stompClient.send('/app/newpoint.'+identi, {}, JSON.stringify(message));
    };
    
    
    var getMousePosition = function (evt) {
        canvas = document.getElementById("canvas");
        var rect = canvas.getBoundingClientRect();		
        return {
            x: evt.clientX - rect.left,
            y: evt.clientY - rect.top
        };
    };


    var connectAndSubscribe = function (ide) {		
		
		console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        
        //subscribe to /topic/TOPICXX when connections succeed
        stompClient.connect({}, function (frame) {
			identi = ide;
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/newpoint.'+identi, function (eventbody) {
				var theObject=JSON.parse(eventbody.body);
				var canvas = document.getElementById("canvas");
				var ctx = canvas.getContext("2d");
				ctx.beginPath();
				ctx.arc(theObject.x, theObject.y, 3, 0, 2 * Math.PI);
				ctx.stroke();				                
            });
        });

    };
    
    

    return {

        init: function (ide) {
            var can = document.getElementById("canvas");          
            
            //websocket connection
            connectAndSubscribe(ide);
			
        },

        publishPoint: function(ev){			            
			var put = getMousePosition(ev);			
			var pt = new Point(put.x, put.y);
            addPointToCanvas(pt);			
            //publicar el evento	            
            stompClient.send("/topic/newpoint."+identi, {}, JSON.stringify(pt)); 		
          
        },

        disconnect: function () {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
		}		
		
    }
	

})();
