<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>chat</title>
<script src="https://code.jquery.com/jquery-2.2.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/json2/20160511/json2.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.2/sockjs.js" ></script>
<script type="text/javascript" >
 
    var sock = null;
    var message = {};
 
    $(document).ready(function(){
        chatSock = new SockJS("/chat/echo-ws");
         
        chatSock.onopen = function() { 
            message={};
            message.message = "Hello !";
            message.type = "all";
            message.to = "all";
            chatSock.send(JSON.stringify(message));
        };
         
        chatSock.onmessage = function(evt) {
        	
            $("#chatMessage").append(evt.data);
            $("#chatMessage").append("<br />");
            $("#chatMessage").scrollTop(99999999);
        };
         
        chatSock.onclose = function() {
            sock.send("Chat program is closed.");
        }
         
         $("#message").keydown(function (key) {
             if (key.keyCode == 13) {
                $("#sendMessage").click();
             }
          });
         
        $("#sendMessage").click(function() {
            if( $("#message").val() != "") {
                 
                message={};
                message.message = $("#message").val().replace("<", "&lt;");
                message.type = "all";
                message.to = "all";
                 
                var to = $("#to").val();
                if ( to != "") {
                    message.type = "one";
                    message.to = to;
                }
                 
                chatSock.send(JSON.stringify(message));
                $("#chatMessage").append("Me ->  "+$("#message").val().replace("<", "&lt;"));
                $("#chatMessage").append("<br/>");
                $("#chatMessage").scrollTop(99999999);
                 
                $("#message").val("");
            }
        });
    });
</script>
</head>
<body>
    <input type="button" id="sendMessage" value="Send" />
    <input type="text" id="message" placeholder="message content"/>
    <input type="text" id="to" placeholder="private message to"/>
    <div id="chatMessage" style="overFlow: auto; max-height: 500px;"></div>
</body>
</html>


