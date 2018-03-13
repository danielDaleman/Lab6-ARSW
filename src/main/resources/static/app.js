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
        stompClient.send('/topic/newpoint.'+identi, {}, JSON.stringify(message));
    };
    
    
    var getMousePosition = function (evt) {
        canvas = document.getElementById("canvas");
        var rect = canvas.getBoundingClientRect();		
        return {
            x: evt.clientX - rect.left,
            y: evt.clientY - rect.top
        };
    };


    var connectAndSubscribe = function () {		
		
		console.info('Connecting to WS...');
        var socket = new SockJS('/stompendpoint');
        stompClient = Stomp.over(socket);
        
        //subscribe to /topic/TOPICXX when connections succeed
        stompClient.connect({}, function (frame) {			
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/newpolygon.'+identi, function (eventbody) {
				var theObject=JSON.parse(eventbody.body);
				var canvas = document.getElementById("canvas");
				var ctx = canvas.getContext("2d");
				ctx.beginPath();
				ctx.arc(theObject.x, theObject.y, 3, 0, 2 * Math.PI);
				ctx.moveTo(0,0);
				for (var i = 0; i < theObject.length - 1; i++){
                    ctx.moveTo(theObject[i].x, theObject[i].y);
                    ctx.lineTo(theObject[i + 1].x, theObject[i + 1].y);
				}				
				ctx.moveTo(theObject[theObject.length - 1].x, theObject[theObject.length - 1].y);
                ctx.lineTo(theObject[0].x, theObject[0].y);																				
				ctx.stroke();				                
            });
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
    
    var puntoActual = function(evt){
		var point = getMousePosition(evt);
		app.publishPoint(point.x,point.y);
		
	};

    return {

        init: function () {
            var can = document.getElementById("canvas");          
            if(window.PointEvent){
				can.addEventListener("pointerdown", puntoActual);
			}else{
				can.addEventListener("mousedown", puntoActual);
			}
            //websocket connection
            //connectAndSubscribe(ide);
			
        },

        publishPoint: function(px,py){			            			
			var pt = new Point(px, py);
            addPointToCanvas(pt);			
            //publicar el evento	            
            stompClient.send("/app/newpoint."+identi, {}, JSON.stringify(pt)); 		
          
        },

        disconnect: function () {
            if (stompClient !== null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
		},		
		
		drawById : function(id){
			identi = id;
			var can = document.getElementById("canvas");
			connectAndSubscribe();
		}
		
    }
	

})();
