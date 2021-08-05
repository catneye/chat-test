/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.bean;

import com.catneye.db.Chat;
import com.catneye.db.Stats;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author plintus
 */

@Remote
public interface ChatBeanRemote {
    
    public List<Chat> getAllMessages();
    public void addMessage(String user, String text);
    public void sendQueueMessage(MessageInfo msg);
    public List<Stats> getStats();
}
