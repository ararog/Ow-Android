package br.net.bmobile.ow.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.net.bmobile.ow.model.Notification;

import com.j256.ormlite.dao.Dao;

public class HistoryService {

	Dao<Notification, Integer> dao;
	
	public HistoryService(Dao<Notification, Integer> dao) {

    	this.dao = dao;
	}	
	
	public List<Notification> findAllNotifications() {
		
		try {
			return dao.queryForAll();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ArrayList<Notification>();
	}
	
	public Integer countUnreadNotifications() {
		
		Integer count = 0;
		
		try {
			count = (int) dao.queryBuilder().where()
					.eq("read", 0).countOf();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	public void saveNotification(Notification notification) {
		
		try {
			dao.createOrUpdate(notification);
		}
		catch(Exception e) {

			e.printStackTrace();
		}
	}
	
	public void deleteAllNotifications() {
		
		try {
			
			dao.executeRawNoArgs("DELETE FROM Notifications");
		}
		catch(Exception e) {

			e.printStackTrace();
		}
	}
}
