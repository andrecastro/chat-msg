package br.edu.ifce.ppd.chat.models;

import net.jini.core.entry.Entry;

import java.io.Serializable;
import java.util.List;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class Chat implements Entry, Serializable {

    public String user1;
    public String user2;
    public List<Message> history;

    public Chat() {
    }

    public Chat(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public String uniqueName() {
        return user1 + "_" + user2;
    }
}
