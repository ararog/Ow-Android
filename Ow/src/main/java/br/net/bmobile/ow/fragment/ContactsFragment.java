package br.net.bmobile.ow.fragment;

import java.util.ArrayList;
import java.util.List;

import br.net.bmobile.ow.event.ChatOfflineEvent;
import br.net.bmobile.ow.event.ChatOnlineEvent;
import br.net.bmobile.ow.event.NewNotificationEvent;
import br.net.bmobile.ow.event.UpdateContactsEvent;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import br.net.bmobile.ow.R;
import br.net.bmobile.ow.activity.MainActivity;
import br.net.bmobile.ow.activity.NotifyActivity;
import br.net.bmobile.ow.model.Contact;
import br.net.bmobile.ow.service.ContactService;
import br.net.bmobile.ow.service.HistoryService;
import br.net.bmobile.ow.support.Constants;
import br.net.bmobile.ow.support.Session;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class ContactsFragment extends RoboFragment {

	@InjectView(R.id.contactsListView)
	ListView contactsListView;
	
	@InjectView(R.id.settingsImageView)
	ImageView settingsImageView;
	
	@InjectView(R.id.addImageView)
	ImageView addImageView;

	@InjectView(R.id.notificationsImageView)
	ImageView notificationsImageView;

	@InjectView(R.id.soundImageView)
	ImageView soundImageView;

    @InjectView(R.id.indicatorView)
    ProgressBar indicatorView;

    @InjectView(R.id.titleLabel)
    TextView titleLabel;

    @InjectView(R.id.countLabel)
	TextView countLabel;
	
	@Inject
	ContactService contactService;
	
	@Inject
	HistoryService historyService;
	
	@Inject
	Session session;
	
	ContactAdapter adapter;
	
	List<Contact> contacts;

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
				R.layout.fragment_contacts, container, false);

		return layout;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		contacts = new ArrayList<Contact>();
		
		contacts.addAll(contactService.findAllContacts());
		
		adapter = new ContactAdapter(this.getActivity(), contacts);
		
		contactsListView.setAdapter(adapter);
		
		contactsListView.setOnItemClickListener(contactSelectedListener);
		
		settingsImageView.setOnClickListener(settingsClickListener);

		notificationsImageView.setOnClickListener(notificationsClickListener);
		
		addImageView.setOnClickListener(addClickListener);
		
		soundImageView.setOnClickListener(soundClickListener);
		
		if(session.isMute()) 
			soundImageView.setImageResource(R.drawable.button_mute);
		else 
			soundImageView.setImageResource(R.drawable.button_unmute);

        eventBus = new EventBus();

        eventBus.register(this);
	};

    public void onEvent(UpdateContactsEvent event) {

        contacts.clear();
        contacts.addAll(contacts);
        adapter.notifyDataSetChanged();
    }

    public void onEvent(NewNotificationEvent event) {

        Integer count = historyService.countUnreadNotifications();
        if(count == 0) {
            countLabel.setVisibility(View.GONE);
        }
        else {
            countLabel.setVisibility(View.VISIBLE);
            countLabel.setText(String.valueOf(count));
        }
    }

    public void onEvent(ChatOnlineEvent event) {

        titleLabel.setText(R.string.contacts);
        indicatorView.setVisibility(View.GONE);

        contactsListView.setEnabled(true);
    }

    public void onEvent(ChatOfflineEvent event) {

        titleLabel.setText(R.string.wait);
        indicatorView.setVisibility(View.VISIBLE);

        contactsListView.setEnabled(false);
    }

	OnItemClickListener contactSelectedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			
			Contact contact = contacts.get(position);
			
			Intent intent = new Intent(ContactsFragment.this.getActivity(), 
					NotifyActivity.class);
			intent.putExtra(Constants.Contact, contact.serialize());
			startActivity(intent);
		}
	};	
	
	View.OnClickListener settingsClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			((MainActivity) getActivity()).selectPage(0);
		}
	};

	View.OnClickListener notificationsClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			((MainActivity) getActivity()).selectPage(2);
		}
	};
	
	View.OnClickListener soundClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(session.isMute()) {
				soundImageView.setImageResource(R.drawable.button_unmute);
				session.setMute(false);
			}
			else {
				soundImageView.setImageResource(R.drawable.button_mute);
				session.setMute(true);
			}
		}
	};
	
	View.OnClickListener addClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private static class ViewHolder {
        TextView nameLabel;
	}
	
	private class ContactAdapter extends ArrayAdapter<Contact> {

		public ContactAdapter(Context context, List<Contact> objects) {
			super(context, R.layout.item_contact, objects);
		}
		
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {

	    	Contact contact = getItem(position);    

	    	ViewHolder viewHolder;
	    	if (convertView == null) {
	    		viewHolder = new ViewHolder();
	    		LayoutInflater inflater = LayoutInflater.from(getContext());
	    		convertView = inflater.inflate(R.layout.item_contact, parent, false);
	    		viewHolder.nameLabel = (TextView) convertView.findViewById(R.id.nameLabel);
	    		convertView.setTag(viewHolder);
	    	} 
	    	else {
	    		viewHolder = (ViewHolder) convertView.getTag();
	    	}
	    	
	    	if(contact.getLastName() == null)
		    	viewHolder.nameLabel.setText(contact.getFirstName());
	    	else
		    	viewHolder.nameLabel.setText(
		    			contact.getFirstName() + " " + contact.getLastName());

	    	return convertView;
	   }		
	}
}
