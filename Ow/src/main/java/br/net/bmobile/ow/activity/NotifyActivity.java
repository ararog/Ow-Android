package br.net.bmobile.ow.activity;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import br.net.bmobile.ow.R;
import br.net.bmobile.ow.model.Contact;
import br.net.bmobile.ow.model.Notification;
import br.net.bmobile.ow.model.NotificationType;
import br.net.bmobile.ow.support.Constants;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotifyActivity extends RoboActivity {

	@InjectView(R.id.notificationTypesListView)
	ListView notificationTypesListView;

	@InjectView(R.id.backImageView)
	ImageView backImageView;
	
	NotificationTypesAdapter adapter;
	
	Contact contact;
	
	List<NotificationType> types;

    EventBus eventBus;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notify);
		
		types = new ArrayList<NotificationType>();
		
		NotificationType notiticationType = new NotificationType();
		notiticationType.setId(1);
		notiticationType.setName("vamo");
		notiticationType.setText("Vamo?");
		types.add(notiticationType);
		
		notiticationType = new NotificationType();
		notiticationType.setId(2);
		notiticationType.setName("perai");
		notiticationType.setText("Peraí");
		types.add(notiticationType);		

		notiticationType = new NotificationType();
		notiticationType.setId(3);
		notiticationType.setName("chegou");
		notiticationType.setText("Chego");
		types.add(notiticationType);		

		notiticationType = new NotificationType();
		notiticationType.setId(4);
		notiticationType.setName("maneiro");
		notiticationType.setText("Maneiro");
		types.add(notiticationType);		

		notiticationType = new NotificationType();
		notiticationType.setId(5);
		notiticationType.setName("tocomfome");
		notiticationType.setText("Tô com fome");
		types.add(notiticationType);		

		notiticationType = new NotificationType();
		notiticationType.setId(6);
		notiticationType.setName("owkey");
		notiticationType.setText("Ok!");
		types.add(notiticationType);		
		
		adapter = new NotificationTypesAdapter(this, types);
		
		notificationTypesListView.setAdapter(adapter);
		
		notificationTypesListView.setOnItemClickListener(typeSelectedListener);
		
		contact = Contact.create(getIntent().getStringExtra(Constants.Contact));
		
		backImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NotifyActivity.this.finish();				
			}
		});
	}
	
	OnItemClickListener typeSelectedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			
			NotificationType type = types.get(position);

			Notification notification = new Notification();
			notification.setType(type.getId());
			notification.setContact(contact);

            eventBus.post(notification);

			finish();
		}
	};	
	
	private static class ViewHolder {
        TextView typeLabel;
	}
	
	private class NotificationTypesAdapter extends ArrayAdapter<NotificationType> {

		public NotificationTypesAdapter(Context context, List<NotificationType> objects) {
			super(context, R.layout.item_notification_type, objects);
		}
		
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {

	    	NotificationType type = getItem(position);    

	    	ViewHolder viewHolder; // view lookup cache stored in tag
	    	if (convertView == null) {
	    		viewHolder = new ViewHolder();
	    		LayoutInflater inflater = LayoutInflater.from(getContext());
	    		convertView = inflater.inflate(R.layout.item_notification_type, parent, false);
	    		viewHolder.typeLabel = (TextView) convertView.findViewById(R.id.typeLabel);
	    		convertView.setTag(viewHolder);
	    	} 
	    	else {
	    		viewHolder = (ViewHolder) convertView.getTag();
	    	}
	    	
	    	viewHolder.typeLabel.setText(type.getText());

	    	return convertView;
	   }		
	}
	
}
