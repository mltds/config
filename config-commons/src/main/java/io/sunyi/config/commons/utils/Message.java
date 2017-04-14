package io.sunyi.config.commons.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Message {

    private String message;
    private Map<Object, Object> info = new LinkedHashMap<Object, Object>();
    private Exception e;

    private Message() {

    }

    public static Message newMessage(String message) {
        Message m = new Message();
        m.message = message;
        return m;
    }

    public Message info(Object infoName, Object infoValue) {
        info.put(infoName, infoName);
        return this;
    }

    public Message e(Exception e) {
        this.e = e;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(message);
        builder.append(".");
        Set<Map.Entry<Object, Object>> entries = info.entrySet();
        for (Map.Entry<Object, Object> entry : entries) {
            builder.append(entry.getKey());
            builder.append(":[");
            builder.append(entry.getValue());
            builder.append("]");
            builder.append(".");
        }

        if (e != null) {
            builder.append("Exception");
            builder.append(":[");
            builder.append(e.getMessage());
            builder.append("]");
            builder.append(".");
        }

        return builder.toString();
    }
}