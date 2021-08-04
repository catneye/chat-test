/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.ws;

import com.catneye.bean.ChatBeanRemote;
import com.catneye.db.Chat;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author plintus
 */
@ServerEndpoint(value = "/ChatSocket", configurator = Configurator.class,
        encoders = ResponseEncoder.class, decoders = MessageDecoder.class)
@Stateless
public class ChatSocket implements Serializable {

    @Inject
    private WSHandler sessionHandler;

    private EndpointConfig config;
    @EJB
    ChatBeanRemote chatEjb;

    @OnOpen
    public void onConnectionOpen(Session session, EndpointConfig config) {
        this.config = config;
        HttpSession httpSession = (HttpSession) this.config.getUserProperties().get("httpSession");
        Logger.getLogger(ChatSocket.class.getName()).log(Level.INFO, "socket open wsSession: {0} httpSession: {1}",
                new Object[]{session.getId(), httpSession});
        sessionHandler.addSession(session, httpSession);
    }

    @OnClose
    public void onConnectionClose(Session session) {
        Logger.getLogger(ChatSocket.class.getName()).log(Level.INFO, "socket close session: {0}", session.getId());
        sessionHandler.removeSession(session);
    }

    @OnMessage
    public ResponseObject onMessage(MessageObject message, Session session) throws IOException, EncodeException {
        HttpSession httpSession = (HttpSession) sessionHandler.getSession(session);
        Logger.getLogger(ChatSocket.class.getName()).log(Level.INFO, "onMessage session: {0} message: {1} httpSession: {2}",
                new Object[]{session.getId(), message, httpSession});

        String type = message.getType();

        ResponseObject jsonResponse = new ResponseObject();
        jsonResponse.setType(type);

        if (message.isValidError()) {
            jsonResponse.setObject(message.getObject());
            jsonResponse.setResult("error");
            return jsonResponse;
        }
        if (httpSession != null) {

            switch (type) {
                case "ping": {
                    jsonResponse.setResult("success");
                    break;
                }
                case "getAllMessages": {
                    List<Chat> mss = chatEjb.getAllMessages();
                    StringBuilder res = new StringBuilder();
                    if (!mss.isEmpty()) {
                        for (Chat ms : mss) {
                            res.append(ms.getUser()).append(": <br />");
                            res.append(ms.getTexts()).append(" <br />");
                        }
                    } else {
                        res.append("empty chat");
                    }
                    jsonResponse.setResult("success");
                    jsonResponse.setObject(res.toString());
                    break;
                }
                case "sendMessage": {
                    NewMessage obj = (NewMessage) message.getObject();
                    chatEjb.addMessage(obj.getUser(), obj.getText());
                    jsonResponse.setResult("success");
                    break;
                }
            }
        }
        return jsonResponse;
    }
}
