/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.ws;

import com.catneye.bean.MessageInfo;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author plintus
 */
@MessageDriven(mappedName = "jms/Queue", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "/jms/queue/ChatQueue"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
@ApplicationScoped
public class JmsQueue implements MessageListener {

    @Inject
    private WSHandler ws;

    @Override
    public void onMessage(Message message) {
        try {
            Logger.getLogger(JmsQueue.class.getName()).log(Level.INFO, "message {0}", message);
            ObjectMessage msg = (ObjectMessage) message;

            MessageInfo me = (MessageInfo) msg.getObject();
            Logger.getLogger(JmsQueue.class.getName()).log(Level.INFO, "me.getType {0}", me.getType());

            InitialContext ic = new InitialContext();

            if (me.getType().equals("newMessage")) {
                ws.sendToAllConnectedSessions(me);
            } else if (me.getType().equals("getAllStats")) {
                ws.sendToAllConnectedSessions(me);
            }
        } catch (JMSException ex) {
            Logger.getLogger(JmsQueue.class.getName()).log(Level.WARNING, "JMSException {0}", ex);
        } catch (NamingException ex) {
            Logger.getLogger(JmsQueue.class.getName()).log(Level.WARNING, "NamingException {0}", ex);
        }
    }

    @PostConstruct
    public void initialize() {
        Logger.getLogger(JmsQueue.class.getName()).log(Level.INFO, "initialize");
    }
}
