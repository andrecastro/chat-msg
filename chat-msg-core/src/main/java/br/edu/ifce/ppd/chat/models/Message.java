package br.edu.ifce.ppd.chat.models;

import net.jini.core.entry.Entry;

import java.util.UUID;

/**
 * Created by andrecoelho on 5/22/16.
 */
public class Message implements Entry {

    public String text;
    public String id;

    public Message() {
    }

    public Message(String text) {
        this.id = text + UUID.randomUUID().toString();
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Message) && id.equals(((Message) obj).id);
    }
}
