package br.net.bmobile.ow.provider;

import java.sql.SQLException;

import android.content.Context;
import br.net.bmobile.ow.model.Contact;
import br.net.bmobile.ow.service.ContactService;
import br.net.bmobile.ow.support.DatabaseHelper;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

public class ContactServiceProvider implements Provider<ContactService> {

	Dao<Contact, Integer> dao;
	
	@Inject
	Context context;
	
	@Inject
	public ContactServiceProvider(DatabaseHelper helper) {
		// TODO Auto-generated constructor stub
		try {
			dao = DaoManager.createDao(helper.getConnectionSource(), 
					Contact.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Override
	public ContactService get() {
		// TODO Auto-generated method stub
		return new ContactService(context, dao);
	}
}