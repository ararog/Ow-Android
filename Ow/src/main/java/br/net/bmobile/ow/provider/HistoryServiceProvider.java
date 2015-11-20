package br.net.bmobile.ow.provider;

import java.sql.SQLException;

import br.net.bmobile.ow.model.Notification;
import br.net.bmobile.ow.service.HistoryService;
import br.net.bmobile.ow.support.DatabaseHelper;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

public class HistoryServiceProvider implements Provider<HistoryService> {

	Dao<Notification, Integer> dao;
	
	@Inject
	public HistoryServiceProvider(DatabaseHelper helper) {
		// TODO Auto-generated constructor stub
		try {
			dao = DaoManager.createDao(helper.getConnectionSource(), 
					Notification.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public HistoryService get() {
		// TODO Auto-generated method stub
		return new HistoryService(dao);
	}
}