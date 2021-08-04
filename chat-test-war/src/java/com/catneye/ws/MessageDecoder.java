/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.ws;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author plintus
 */
public class MessageDecoder implements Decoder.Text<MessageObject> {

    JsonbConfig jsonconfig;
    Jsonb jsonb;
    
    @Override
    public void init(EndpointConfig ec) {
        jsonconfig = new JsonbConfig().withNullValues(true).setProperty("jsonb.from.json.encoding", "UTF-8");
        jsonb = JsonbBuilder.create(jsonconfig);
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean willDecode(String string) {
        Json.createReader(new StringReader(string));
        return true;
    }

    @Override
    public MessageObject decode(String arg0) throws DecodeException {

        JsonReader jsonReader = Json.createReader(new StringReader(arg0));
        JsonObject baseJson = jsonReader.readObject();

        MessageObject jsonMessage = new MessageObject();
        String type = baseJson.getString("type");
        jsonMessage.setType(type);
        jsonMessage.setValidError(false);
        Logger.getLogger(MessageDecoder.class.getName()).log(Level.INFO, "type: {0} ", type);

        switch (type) {
            case "getAllMessages": {
                break;
            }
            case "ping": {
                break;
            }
            case "sendMessage": {
                JsonObject obj = baseJson.getJsonObject("object");
                NewMessage ui = new NewMessage();
                ui.setText(obj.getString("text"));
                ui.setUser(obj.getString("user"));
                jsonMessage.setObject(ui);
                break;
            }
        }
        return jsonMessage;
    }

}
