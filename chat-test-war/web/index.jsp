<%-- 
    Document   : index.jsp
    Created on : 4 авг. 2021 г., 15:25:53
    Author     : plintus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="./prototype.js"></script>
        <script type="text/javascript" src="./funcs.js"></script>
        <script type="text/javascript" src="./sockets.js"></script>
        <link href='./site.css' rel='stylesheet' type='text/css'>
        <title>Chat Page</title>
    </head>
    <body>
        <%@include file="/vars.jspf" %>
        <div style="width:800px; min-height: 600px; height:100%;" id="mainWindow">
            <!-- toggle chat-->
            <div style="width:100%; min-height: 600px;" id="chatWindow">
                <!-- chat nicname -->
                <div style="width:100%;height:30px;border: solid black 1px;" id="nicWindow">
                    <input type="text" name="nickName" id="nickName"/>
                </div>
                <!-- all messages-->
                <div style="width:100%; height: 600px; border: solid black 1px; overflow-y: scroll" id="messagesWindow">
                    <div style="width:100%;" id="canvasWindow"></div>
                </div>
                <!-- send message-->
                <div style="width:30px;height:30px;border: solid black 1px" id="sendWindow">
                    <input type="text" name="sendText" id="sendText"/>
                    <button name="sendTextButton" id="sendTextButton" 
                            onclick="sendMessages();return false;"/>
                </div>
            </div>
            <!-- toggle stat-->
            <!--<div style="width:100%;height:100%;" id="statWindow"></div>-->
        </div>
        <script>

            function Chat() {
                this.getAllMessages = function (message) {
                    $("canvasWindow").innerHTML = message.object;
                };
                this.newMessage = function (message) {
                    var old=$("canvasWindow").innerHTML;
                    console.log(old);
                    console.log(message.object);
                    $("canvasWindow").innerHTML = old + message.object;
                };
            }

            function onSocketOpen() {
                var sobj = {type: "getAllMessages",
                    object: 0};
                sock.send(JSON.stringify(sobj));
            }

            function sendMessages() {
                var sobj = {type: "sendMessage",
                    object: {text: $("sendText").value, user: $("nickName").value}};
                console.log(sobj);
                sock.send(JSON.stringify(sobj));
            }

            var chat = new Chat();
            sock.addOnOpen(onSocketOpen);
            sock.addOnMessage('getAllMessages', chat.getAllMessages);
            sock.addOnMessage('newMessage', chat.newMessage);
        </script>
    </body>
</html>
