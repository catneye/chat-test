/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.bean;

import com.catneye.db.Chat;
import com.catneye.db.Stats;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author plintus
 */
@Stateless
public class ChatBean implements ChatBeanRemote {

    @PersistenceContext(unitName = "chat-ejbPU")
    private EntityManager em;

    @Override
    public List<Chat> getAllMessages() {
        Query query = em.createNamedQuery("Chat.findAllOrder");
        List<Chat> ret = query.getResultList();
        return ret;
    }

    @Override
    public void addMessage(String user, String text) {
        if (user != null && !user.isBlank() && text != null && !text.isBlank()) {
            Chat chat = new Chat();
            chat.setAdddate(LocalDateTime.now());
            chat.setTexts(text);
            chat.setUser(user);
            Logger.getLogger(ChatBean.class.getName()).log(Level.INFO, "addMessage Chat: {0} ", chat.toString());
            em.persist(chat);

            MessageInfo mi = new MessageInfo();
            mi.setResult("succes");
            mi.setType("newMessage");
            mi.setObject(chat);
            this.sendQueueMessage(mi);
        }
    }

    @Override
    public List<Stats> getStats() {
        Query query = em.createNamedQuery("Stats.findAll");
        List<Stats> ret = query.getResultList();
        return ret;
    }

    @Override
    public void sendQueueMessage(MessageInfo msg) {
        try {

            InitialContext ic = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) ic.lookup("ConnectionFactory");

            Destination destination = (Destination) ic.lookup(Constants.QUEUE_NAME);
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            ObjectMessage message = session.createObjectMessage();
            message.setObject(msg);
            producer.send(message);
            session.close();
            connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(ChatBean.class.getName()).log(Level.WARNING, "JMSException {0}", ex);
        } catch (NamingException ex) {
            Logger.getLogger(ChatBean.class.getName()).log(Level.WARNING, "NamingException {0}", ex);
        }
    }
}
