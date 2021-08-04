/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.ws;

import com.catneye.bean.MessageInfo;
import com.catneye.db.Chat;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 *
 * @author plintus
 */
@ApplicationScoped
public class WSHandler {

    private final HashMap<Session, HttpSession> sessions = new HashMap();
    private final HashMap<Session, HashMap> sessionsvars = new HashMap();

    public void addSession(Session session, HttpSession httpsession) {
        sessions.put(session, httpsession);
        sessionsvars.put(session, new HashMap());
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "addSession {0} httpSession: {1}",
                new Object[]{session.getId(), httpsession});
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "sessions count {0}", sessions.size());
    }

    public void removeSession(Session session) {
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "removeSession {0}", session);
        sessions.remove(session);
        sessionsvars.remove(session);
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "sessions count {0}", sessions.size());
    }

    public HttpSession getSession(Session session) {
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "getSession sessions count {0}", sessions.size());
        return sessions.get(session);
    }

    public void addSessionVar(Session session, String key, Object value) {
        sessionsvars.get(session).put(key, value);
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "addSessionVar {0} key: {1} value: {1}",
                new Object[]{session.getId(), key, value});
    }

    public void removeSessionVar(Session session, String key) {
        sessionsvars.get(session).remove(key);
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "removeSessionVar {0} key: {1}",
                new Object[]{session.getId(), key});
    }

    public Object getSessionVar(Session session, String key) {
        return sessionsvars.get(session).get(key);
    }

    public void sendToAllConnectedSessions(MessageInfo me) {
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "sendToAllConnectedSessions sessions count {0}", sessions.size());
        for (Session key : sessions.keySet()) {
            HttpSession http = sessions.get(key);
            if (http != null) {
                try {
                    ResponseObject ro = new ResponseObject();
                    ro.setType(me.getType());
                    ro.setResult((String) me.getResult());

                    //MessageInfo mi = new MessageInfo();
                    Chat chat = (Chat) me.getObject();
                    StringBuilder res = new StringBuilder();
                    res.append(chat.getUser()).append(": <br />");
                    res.append(chat.getTexts()).append(" <br />");
                    ro.setObject(res.toString());
                    //ro.setObject(me.getObject());
                    //ro.setObject(me.getObject());
                    key.getBasicRemote().sendObject(ro);
                } catch (IOException ex) {
                    Logger.getLogger(WSHandler.class.getName()).log(Level.WARNING, "IOException {0}", ex);
                } catch (EncodeException ex) {
                    Logger.getLogger(WSHandler.class.getName()).log(Level.WARNING, "EncodeException {0}", ex);
                }
            }
        }
    }
}

/*public void fireMessageInfo(MessageInfo me) {
        Logger.getLogger(WSHandler.class.getName()).log(Level.INFO, "fireMessageInfo {0}", me);
        for (Session key : sessions.keySet()) {
            try {
                HttpSession http = sessions.get(key);
                if (http != null) {
                    Users u = (Users) http.getAttribute("userinfo");
                    if ((u != null) && (u.getId().equals(me.getIdusers()))) {
                        ResponseObject ro = new ResponseObject();
                        ro.setType(me.getType());
                        ro.setResult((String) me.getResult());
                        key.getBasicRemote().sendObject(ro);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(WSHandler.class.getName()).log(Level.WARNING, "IOException {0}", ex);
            } catch (EncodeException ex) {
                Logger.getLogger(WSHandler.class.getName()).log(Level.WARNING, "EncodeException {0}", ex);
            }
        }
    }*/
