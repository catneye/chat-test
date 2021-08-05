/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.bean;

import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;

/**
 *
 * @author plintus
 */
@Startup
@Singleton
public class Threads {

    HashMap<Long, Thread> thrs = new HashMap();

    @PostConstruct
    void init() {
        StatThread th = new StatThread();
        th.start();
        thrs.put(th.getId(), th);

        Logger.getLogger(Threads.class.getName()).log(Level.INFO, "init : {0}", 0);
    }

    @PreDestroy
    public void close() {
        for (Long key : thrs.keySet()) {
            Logger.getLogger(Threads.class.getName()).log(Level.INFO, "destroy : {0}", key);
            thrs.get(key).interrupt();
        }
        Logger.getLogger(Threads.class.getName()).log(Level.INFO, "close : {0}", 0);
    }
}
