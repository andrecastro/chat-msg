package br.edu.ifce.ppd.chat.models;

import br.edu.ifce.ppd.ws.UserWrapper;

/**
 * Created by andrecoelho on 5/23/16.
 */
public class UserAdmin {

    private UserWrapper userWrapper;

    public UserAdmin(UserWrapper userWrapper) {
        this.userWrapper = userWrapper;
    }

    public UserWrapper getUserWrapper() {
        return userWrapper;
    }

    @Override
    public String toString() {
        return userWrapper.getUser().getNickname();
    }
}
