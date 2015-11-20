package br.net.bmobile.ow.event;

import java.util.List;

import br.net.bmobile.ow.model.Contact;

/**
 * Created by Administrator on 23/09/14.
 */
public class UpdateContactsEvent {

    private List<Contact> contacts;

    public UpdateContactsEvent(List<Contact> contacts) {

        this.contacts = contacts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
