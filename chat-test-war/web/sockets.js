/* 
 * Copyright (C) 2017 plintus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

function Websocket() {
    var websocket = null;
    var onmessagefuncs = [];
    var onloadfuncs = [];
    var onopenfuncs = [];
    var sockuri;
    var socketdebug = true;

    this.init = function (uri, onopen, onmessage, onerror, onclose, isdepricated) {

        if (socketdebug) {
            console.log('Opening WebSocket: ' + uri);
        }
        sockuri = uri;
        websocket = new WebSocket(uri);
        websocket.onerror = function (event) {
            onerror ? onerror(event) : onError(event);
        };
        websocket.onopen = function (event) {
            onopen ? onopen(event) : onOpen(event);
        };
        websocket.onmessage = function (event) {
            onmessage ? onmessage(event) : onMessage(event);
        };
        websocket.onclose = function (event) {
            onclose ? onclose(event) : onClose(event);
        };
    };

    this.close = function () {
        websocket.close();
    };

    this.send = function (message) {
        if (socketdebug) {
            console.log('WebSocket doSend \n' + message);
        }
        if (websocket.readyState === 1) {
            websocket.send(message);
        } else {
            if (socketdebug) {
                console.log('WebSocket is ' + websocket.readyState);
            }
        }
    };
    /*
     this.SignXml = function (storageName, keyType, xmlToSign, callback) {
     
     var xml = {
     "module": "kz.gov.pki.knca.commonUtils",
     "method": "signXml",
     "args": [storageName, keyType, xmlToSign, "", ""] //AUTH, SIGN
     };
     
     var wssuri = "wss://127.0.0.1:13579";
     var sssock = new Websocket();
     sssock.init(wssuri,
     function () {
     sssock.send(JSON.stringify(xml));
     },
     function (result) {
     //var json = JSON.parse(result);
     console.log("SignXml: "+JSON.parse(result));
     callback(result);
     }
     );
     };
     */

    function onMessage(evt) {
        console.log('WebSocket onMessage');
        if (evt) {
            var json = JSON.parse(evt.data);
            if (socketdebug) {
                console.log(json);
            }
            for (var i = 0; i < onmessagefuncs.length; i++) {
                var callback = onmessagefuncs[i];
                if (callback.response === json.type) {
                    callback.funcptr(json);
                }
            }
        }

    }

    function onOpen(evt) {
        if (socketdebug) {
            console.log('WebSocket onOpen');
        }
        for (var i = 0; i < onopenfuncs.length; i++) {
            var callback = onopenfuncs[i];
            callback.funcptr();
        }
    }
    function onError(evt) {
        if (socketdebug) {
            console.log('WebSocket onError');
        }
    }
    function onClose(evt) {
        if (socketdebug) {
            console.log('WebSocket onClose');
        }
    }

    this.addOnMessage = function (response, funcptr) {
        var callback = {};
        callback.response = response;
        callback.funcptr = funcptr;
        onmessagefuncs.push(callback);
    };
    //onopensocket
    //
    this.addOnOpen = function (funcptr) {
        var callback = {};
        callback.funcptr = funcptr;
        onopenfuncs.push(callback);
    };
    //onload page
    this.addOnLoad = function (funcptr) {
        var callback = {};
        callback.funcptr = funcptr;
        onloadfuncs.push(callback);
    };

    document.addEventListener("DOMContentLoaded", function (event) {
        for (var i = 0; i < onloadfuncs.length; i++) {
            var callback = onloadfuncs[i];
            callback.funcptr();
        }
    });

}