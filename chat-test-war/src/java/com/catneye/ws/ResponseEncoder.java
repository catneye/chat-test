/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.ws;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author plintus
 */
public class ResponseEncoder implements Encoder.Text<ResponseObject> {
    
    @Override
    public String encode(ResponseObject arg0) throws EncodeException {
        Logger.getLogger(ResponseEncoder.class.getName()).log(Level.INFO, "encode: {0}", arg0);
        JsonbConfig jsonconfig = new JsonbConfig().setProperty("jsonb.to.json.encoding", "UTF-8");
        Jsonb jsonb = JsonbBuilder.create(jsonconfig);
        
        if ((arg0.getObject() != null) && (!arg0.getObject().getClass().equals(String.class))) {
            arg0.setObject(jsonb.toJson(arg0.getObject()));
        }
        return jsonb.toJson(arg0);
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

}
