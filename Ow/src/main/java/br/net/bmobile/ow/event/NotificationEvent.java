package br.net.bmobile.ow.event;

import br.net.bmobile.ow.model.Notification;

/**
 * Created by Administrator on 23/09/14.
 */
public class NotificationEvent {

    private Notification notification;

    public NotificationEvent() { }

    public NotificationEvent(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
