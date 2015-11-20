package br.net.bmobile.ow.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import br.net.bmobile.ow.model.Contact;

import com.j256.ormlite.dao.Dao;

public class ContactService {

    Context context;
	Dao<Contact, Integer> dao;
    
    public ContactService(Context context, Dao<Contact, Integer> dao) {

    	this.context = context;
    	this.dao = dao;
	}	
    
    public List<Contact> findDeviceContacts() {
    	
    	List<Contact> contacts = new ArrayList<Contact>();
    	
		ContentResolver contentResolver = context.getContentResolver();

		Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);	

		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {

			String firstName = null;
			String lastName = null;
			String phoneNumber = null;

			while (cursor.moveToNext()) {

				String contact_id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				
				int hasPhoneNumber = Integer.parseInt(
						cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

				if (hasPhoneNumber > 0) {

					Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
					    
						int phoneType = phoneCursor.getInt(phoneCursor.getColumnIndex(Phone.TYPE));
					    
					    if (phoneType == Phone.TYPE_MOBILE)						
					    	phoneNumber = phoneCursor.getString(phoneCursor
					    			.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}

					phoneCursor.close();
				}
				
				String[] projection = new String[] {
						ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, 
						ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, 
						ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME};


			    String where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"; 
			    String[] whereParameters = new String[]{contact_id, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};

			    Cursor nameCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, where, whereParameters, null);

			    if (nameCursor.moveToFirst()) { 

			    	firstName = nameCursor.getString(nameCursor
			    			.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));

			    	lastName = nameCursor.getString(nameCursor
			    			.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
			    } 
			    
			    nameCursor.close();				
				
				if(firstName != null && phoneNumber != null)
				{
					Contact contact = new Contact();
					contact.setFirstName(firstName);
					contact.setLastName(lastName);
					contact.setPhoneNumber(phoneNumber);
					contacts.add(contact);
				}
			}
		}    	
    	
    	return contacts;
    }
    
    public List<Contact> findAllContacts() {
    	
    	try {
			return dao.queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return new ArrayList<Contact>();
    }
    
    public void saveAll(List<Contact> contacts) {

    	for(Contact contact : contacts) {
			try {
				dao.createIfNotExists(contact);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
