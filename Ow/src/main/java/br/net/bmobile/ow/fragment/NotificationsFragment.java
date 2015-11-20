package br.net.bmobile.ow.fragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.net.bmobile.ow.event.NewNotificationEvent;
import de.greenrobot.event.EventBus;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import br.net.bmobile.ow.R;
import br.net.bmobile.ow.activity.MainActivity;
import br.net.bmobile.ow.model.Notification;
import br.net.bmobile.ow.service.HistoryService;
import br.net.bmobile.ow.support.Constants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class NotificationsFragment extends RoboFragment {

	@InjectView(R.id.notificationsListView)
	ListView notificationsListView;

	@InjectView(R.id.contactsImageView)
	ImageView contactsImageView;

	@Inject
	HistoryService historyService;
	
	NotificationAdapter adapter;
	
	List<Notification> notifications;

    EventBus eventBus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		if(container == null)
			return null;
		
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.fragment_notifications, container, false);

		return layout;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		notifications = new ArrayList<Notification>();
		
		notifications.addAll(historyService.findAllNotifications());
		
		adapter = new NotificationAdapter(this.getActivity(), notifications);
		
		notificationsListView.setAdapter(adapter);
		
		notificationsListView.setOnItemClickListener(notificationSelectedListener);
		
		contactsImageView.setOnClickListener(contactsClickListener);

        eventBus = new EventBus();

        eventBus.register(this);
	};
	
	BroadcastReceiver updateHistoryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			NotificationsFragment fragment = NotificationsFragment.this;

			ObjectMapper mapper = new ObjectMapper();
			
			String data = intent.getStringExtra(Constants.Notifications);
			if(data != null) {
				try {
					List<Notification> notifications = mapper.readValue(data, new TypeReference<List<Notification>>() {});
					
					fragment.notifications.clear();
					fragment.notifications.addAll(notifications);
					fragment.adapter.notifyDataSetChanged();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

    public void onEvent(NewNotificationEvent event) {

        notifications.add(0, event.getNotification());
        adapter.notifyDataSetChanged();
    }

	OnItemClickListener notificationSelectedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			
		}
	};	
	
	View.OnClickListener contactsClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			((MainActivity) getActivity()).selectPage(1);
		}
	};
	
	private static class ViewHolder {
        TextView nameLabel;
        TextView dateLabel;
        TextView descriptionLabel;
	}
	
	private class NotificationAdapter extends ArrayAdapter<Notification> {

		public NotificationAdapter(Context context, List<Notification> objects) {
			super(context, R.layout.item_notification, objects);
		}
		
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {

	    	Notification notification = getItem(position);    

	    	ViewHolder viewHolder;
	    	if (convertView == null) {
	    		viewHolder = new ViewHolder();
	    		LayoutInflater inflater = LayoutInflater.from(getContext());
	    		convertView = inflater.inflate(R.layout.item_notification, parent, false);
	    		viewHolder.nameLabel = (TextView) convertView.findViewById(R.id.nameLabel);
	    		viewHolder.dateLabel = (TextView) convertView.findViewById(R.id.dateLabel);
	    		viewHolder.descriptionLabel = (TextView) convertView.findViewById(R.id.descriptionLabel);
	    		convertView.setTag(viewHolder);
	    	} 
	    	else {
	    		viewHolder = (ViewHolder) convertView.getTag();
	    	}
	    	
	    	Locale locale = getResources().getConfiguration().locale;
	    	DateFormat f = DateFormat.getDateInstance(DateFormat.SHORT, locale);

	    	viewHolder.nameLabel.setText(notification.getContact().getFirstName());
	    	viewHolder.dateLabel.setText(f.format(notification.getDate()));
	    	viewHolder.descriptionLabel.setText(notification.typeToString());
	    	
	    	return convertView;
	   }		
	}
}
