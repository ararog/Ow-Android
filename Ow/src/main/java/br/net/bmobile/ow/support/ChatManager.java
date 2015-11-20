package br.net.bmobile.ow.support;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import br.net.bmobile.ow.event.ChatOfflineEvent;
import br.net.bmobile.ow.event.ChatOnlineEvent;
import br.net.bmobile.ow.event.NewNotificationEvent;
import br.net.bmobile.ow.event.SendNotificationEvent;
import br.net.bmobile.ow.event.StartChatEvent;
import br.net.bmobile.ow.event.UpdateContactsEvent;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.service.RoboService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import br.net.bmobile.ow.model.Contact;
import br.net.bmobile.ow.model.Notification;
import br.net.bmobile.ow.model.User;
import br.net.bmobile.ow.service.AlertService;
import br.net.bmobile.ow.service.ContactService;
import br.net.bmobile.ow.service.HistoryService;
import br.net.bmobile.ow.service.RestService;
import br.net.bmobile.ow.util.StringUtils;
import br.net.bmobile.websocketrails.WebSocketRailsChannel;
import br.net.bmobile.websocketrails.WebSocketRailsDataCallback;
import br.net.bmobile.websocketrails.WebSocketRailsDispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class ChatManager extends RoboService {

	@Inject
	RestService restService;

	@Inject
	ContactService contactService;

	@Inject
	HistoryService historyService;

	@Inject
	AlertService alertService;
	
	@Inject
	Session session;
	
	HashMap<String, WebSocketRailsChannel> connections;
	
	WebSocketRailsDispatcher dispatcher;

	ObjectMapper mapper;

	User user;
	
	Handler handler;
	
	Timer timer;
	
	TimerTask task;

    EventBus eventBus;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		connections = new HashMap<String, WebSocketRailsChannel>();
		
		handler = new Handler();
		
		mapper = new ObjectMapper();

		user = session.getUser();
		
		timer = new Timer();

        eventBus = new EventBus();

        eventBus.register(this);

		if(user != null) {

			startChat();
		}

		LocalBroadcastManager.getInstance(this).registerReceiver(connectivityReceiver,
			      new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));		
	}
	
	private void startChat() {
		
		try {
			dispatcher = new WebSocketRailsDispatcher(new URL("http://oww.herokuapp.com/websocket"));

			dispatcher.bind("connection_opened", new WebSocketRailsDataCallback() {
				
				@Override
				public void onDataAvailable(Object arg0) {
					startSync();
				}
			});
			
			dispatcher.bind("connection_closed", new WebSocketRailsDataCallback() {
				
				@Override
				public void onDataAvailable(Object arg0) {
					stopSync();
				}
			});
			
			dispatcher.bind("connection_error", new WebSocketRailsDataCallback() {
				
				@Override
				public void onDataAvailable(Object arg0) {
					stopSync();
				}
			});			
			
			dispatcher.connect();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void stopChat() {

        eventBus.post(new ChatOfflineEvent());

		for(String channel : connections.keySet())
			dispatcher.unsubscribe(channel);

        String phoneNumber = StringUtils.normalizePhoneNumber(
                user.getCountryCode(),
                user.getPhoneNumber());

        dispatcher.unsubscribe(phoneNumber);

		dispatcher.disconnect();
		
		dispatcher = null;
		
		connections.clear();
	}
	
	private void createRooms(List<Contact> contacts) {
		
		WebSocketRailsChannel channel;

		String phoneNumber;
		
		for(Contact contact : contacts) {
		
			phoneNumber = StringUtils.normalizePhoneNumber(
					contact.getCountryCode(), 
					contact.getPhoneNumber());
		
			if(connections.get(phoneNumber) == null) {	

				channel = dispatcher.subscribe(phoneNumber);
				connections.put(phoneNumber, channel);
			}
		}		
		
		phoneNumber = StringUtils.normalizePhoneNumber(
				user.getCountryCode(), 
				user.getPhoneNumber());

        if(! dispatcher.isSubscribed(phoneNumber)) {

            channel = dispatcher.subscribe(phoneNumber);

            channel.bind("notification_event", messageCallback);

            eventBus.post(new ChatOnlineEvent());

            dispatcher.trigger("connected_event", user);
        }
	}
	
	private void startSync() {
		
		task = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        
                    List<Contact> contacts = contactService.findDeviceContacts();
                    restService.syncContacts(user.getId(), contacts, syncCallback);
                    }
                });
            }
		};
           
        timer.schedule(task, 0, 300000);	
	}
	
	private void stopSync() {
		
		timer.cancel();
	}

    public void onEvent(StartChatEvent event) {

        user = event.getUser();
        startChat();
    }

    public void onEvent(SendNotificationEvent event) {

        String phoneNumber = StringUtils.normalizePhoneNumber(
                event.getNotification().getContact().getCountryCode(),
                event.getNotification().getContact().getPhoneNumber());

        WebSocketRailsChannel contactChannel = connections.get(phoneNumber);

        contactChannel.trigger("notification_event", event.getNotification());
    }

	BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			  
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                              activeNetwork.isConnectedOrConnecting();

        if(isConnected) {
            if(user != null && "connected".equals(dispatcher.getState()))
                startChat();
        }
        else {
            stopChat();
        }
		}
	};	

	Callback<List<Contact>> syncCallback = new Callback<List<Contact>>() {
		
		@Override
		public void success(List<Contact> contacts, Response arg1) {

            eventBus.post(new UpdateContactsEvent(contacts));

			contactService.saveAll(contacts);
			
			createRooms(contacts);
		}
		
		@Override
		public void failure(RetrofitError arg0) {

		}
	};
	
	WebSocketRailsDataCallback messageCallback = new WebSocketRailsDataCallback() {
		
		@Override
		public void onDataAvailable(Object messageData) {

        if(messageData instanceof Map) {

            Notification notification = (Notification) mapper
                    .convertValue(messageData, new TypeReference<Notification>(){});

            notification.setDate(new Date());
            notification.setRead(false);

            historyService.saveNotification(notification);

            alertService.executeAlertForNotification(notification, !session.isMute());

            eventBus.post(new NewNotificationEvent(notification));
        }
		}
	};
}
