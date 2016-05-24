package br.edu.ifce.ppd.chat.models;

import net.jini.core.entry.Entry;

/**
 * Created by andrecoelho on 5/15/16.
 */
public class User implements Entry {

    public String nickname;
    public Double y;
    public Double x;
    public Boolean on;

    public User(String nickname, Double x, Double y, Boolean on) {
        this.nickname = nickname;
        this.x = x;
        this.y = y;
        this.on = on;
    }

    public User() {
    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return nickname;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && nickname.equals(((User) obj).nickname);
    }
}
