package br.edu.ifce.ppd.chat.models;

/**
 * Created by andrecoelho on 5/17/16.
 */
public class UserWrapper {

    private User user;

    private Integer numberOfMessages = 0;
    private boolean hasNewMessages;
    private boolean online;

    public UserWrapper(User user) {
        this.user = user;
    }

    public UserWrapper(User user, boolean hasNewMessages) {
        this.user = user;
        this.hasNewMessages = hasNewMessages;
    }

    public boolean hasNewMessages() {
        return hasNewMessages;
    }

    public void setHasNewMessages(boolean hasNewMessages) {
        this.hasNewMessages = hasNewMessages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UserWrapper && user.equals(((UserWrapper) obj).getUser());
    }

    public void setNumberOfMessages(Integer numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public Integer getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return online;
    }

    @Override
    public String toString() {
        return user.nickname;
    }
}
