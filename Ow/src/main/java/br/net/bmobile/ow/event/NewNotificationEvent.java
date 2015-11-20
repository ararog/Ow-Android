package br.net.bmobile.ow.event;

import br.net.bmobile.ow.model.Notification;

/**
 * Created by Administrator on 23/09/14.
 */
public class NewNotificationEvent extends NotificationEvent {

    public NewNotificationEvent(Notification notification) {

        super(notification);
    }
}
