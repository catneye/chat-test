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
                    <div style="float:left;  margin: 5px; width:12em; height: 1em">name</div>
                    <input type="text" name="nickName" id="nickName" style="float:left;  margin: 5px; width:12em; height: 1em">
                    <div style="clear: both"></div>
                </div>
                <!-- all messages-->
                <div style="width:100%; height: 600px; border: solid black 1px; overflow-y: scroll" id="messagesWindow">
                    <div style="width:100%;" id="canvasWindow"></div>
                </div>
                <!-- send message-->
                <div style="width:100%;height:30px;border: solid black 1px" id="sendWindow">
                    <div style="float:left;  margin: 5px; width:12em; height: 1em">Text</div>
                    <input type="text" name="sendText" id="sendText" style="float:left; margin: 5px" >
                    <button name="sendTextButton" id="sendTextButton" 
                            onclick="sendMessages();return false;" style="float:left;  margin: 5px; width:12em; height: 1.6em" >Send</button>
                    <div style="clear: both"></div>
                </div>
            </div>
            <!-- stat-->
            <div style="width:100%;height:10em;background-color: lightyellow" id="statWindow">
            </div>
        </div>
        <script>

            function Chat() {
                this.getAllMessages = function (message) {
                    $("canvasWindow").innerHTML = message.object;
                };
                this.getAllStats = function (message) {
                    console.log(message);
                    //$("canvasWindow").innerHTML = message.object;
                    //[{"id":0.9799630525212919,"ndoc":1,"nentry":1,"word":"bk"},
                    //{"id":0.534399846783888,"ndoc":1,"nentry":1,"word":"werwer"},
                    var json = JSON.parse(message.object);
                    var last = json[json.length - 1];
                    var max = last.nentry;
                    var block = $("statWindow");

                    while (block.firstChild) {
                        block.removeChild(block.firstChild);
                    }

                    //header
                    var divhead = document.createElement("p");
                    divhead.setStyle({'background-color': 'white', 'width': "100%", 'text-align': 'center'});
                    divhead.innerHTML = 'Stat';


                    var divhead0 = document.createElement("div");
                    divhead.setStyle({'background-color': 'white', 'width': "100%"});

                    var divhead1 = document.createElement("div");
                    divhead1.setStyle({'background-color': 'white', 'width': "10%", 'float': 'left'});
                    divhead1.innerHTML = 'Word';

                    var divhead2 = document.createElement("div");
                    divhead2.setStyle({'background-color': 'white', 'width': "90%", 'text-allign': 'center'});
                    divhead2.innerHTML = 'Count';

                    var divhead3 = document.createElement("div");
                    divhead3.setStyle({'clear': 'both'});

                    divhead0.appendChild(divhead1);
                    divhead0.appendChild(divhead2);
                    divhead0.appendChild(divhead3);
                    block.appendChild(divhead);
                    block.appendChild(divhead0);
                    json.forEach(function (item, index, array) {

                        //<div width:100%>
                        //    <div width:90%>
                        //    nentry
                        //    </div>
                        //    <div width:10%>
                        //    word
                        //    </div>
                        //</div>

                        var el = item;

                        var divblock = document.createElement("div");
                        divblock.setStyle({'background-color': 'wheat', 'width': "100%"});
                        var h = el.nentry * 90 / max;

                        var divfiller = document.createElement("div");
                        divfiller.setStyle({'width': (90 - h + "%"), 'float': 'left'});

                        var divnentry = document.createElement("div");
                        divnentry.setStyle({'background-color': 'green', 'width': h + "%", 'float': 'left'});
                        divnentry.innerHTML = el.nentry;

                        var divword = document.createElement("div");
                        divword.setStyle({'background-color': 'white', 'width': "10%", 'float': 'left'});
                        divword.innerHTML = el.word;

                        var divclear = document.createElement("div");
                        divclear.setStyle({'clear': 'both'});

                        divblock.appendChild(divfiller);
                        divblock.appendChild(divword);
                        divblock.appendChild(divnentry);
                        divblock.appendChild(divclear);
                        block.appendChild(divblock);
                    });
                };
                this.newMessage = function (message) {
                    var old = $("canvasWindow").innerHTML;
                    console.log(old);
                    console.log(message.object);
                    var json = JSON.parse(message.object);
                    $("canvasWindow").innerHTML = old + json.user + ": <br >" + json.texts + "<br >";
                };
            }

            function onSocketOpen() {
                var sobj = {type: "getAllMessages",
                    object: 0};
                sock.send(JSON.stringify(sobj));
                var sobj = {type: "getAllStats",
                    object: 0};
                sock.send(JSON.stringify(sobj));
            }

            function sendMessages() {
                if ($("sendText").value && $("nickName").value) {
                    var sobj = {type: "sendMessage",
                        object: {text: $("sendText").value, user: $("nickName").value}};
                    console.log(sobj);
                    sock.send(JSON.stringify(sobj));
                } else {
                    alert("Nicname and text need be fill");
                }
            }

            var chat = new Chat();
            sock.addOnOpen(onSocketOpen);
            sock.addOnMessage('getAllMessages', chat.getAllMessages);
            sock.addOnMessage('newMessage', chat.newMessage);
            sock.addOnMessage('getAllStats', chat.getAllStats);
        </script>
    </body>
</html>
