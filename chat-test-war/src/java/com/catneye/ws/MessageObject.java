/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.ws;


/**
 *
 * @author plintus
 */
public class MessageObject{
    
    private String type;
    private Object object;
    private boolean validError;

    public boolean isValidError() {
        return validError;
    }

    public void setValidError(boolean validError) {
        this.validError = validError;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the object
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(Object object) {
        this.object = object;
    }
    
    @Override
    public String toString(){
        return "type="+type+"; object="+object;
    }
}
