/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.bean;

import com.catneye.db.Stats;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author plintus
 */
public class StatThread extends Thread {

    @Override
    public void run() {
        try {
            InitialContext ic = new InitialContext();
            ChatBeanRemote chatEjb = (ChatBeanRemote) ic.lookup("java:global/chat-test/chat-test-ejb/ChatBean!com.catneye.bean.ChatBeanRemote");

            while (true) {
                List<Stats> list = chatEjb.getStats();
                MessageInfo msi = new MessageInfo();
                msi.setType("getAllStats");
                msi.setResult("succes");
                msi.setObject(list);
                chatEjb.sendQueueMessage(msi);
                Logger.getLogger(StatThread.class.getName()).log(Level.INFO, "Send stats : {0}", 0);
                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
