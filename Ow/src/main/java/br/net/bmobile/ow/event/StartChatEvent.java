package br.net.bmobile.ow.event;

import br.net.bmobile.ow.model.User;

/**
 * Created by Administrator on 23/09/14.
 */
public class StartChatEvent {

    private User user;

    public StartChatEvent(User user) {

        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
