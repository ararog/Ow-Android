package br.net.bmobile.ow.fragment;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import br.net.bmobile.ow.R;
import br.net.bmobile.ow.activity.MainActivity;
import br.net.bmobile.ow.service.HistoryService;

import com.google.inject.Inject;

public class SettingsFragment extends RoboFragment {

	@InjectView(R.id.settingsListView)
	ListView settingsListView;
	
	@InjectView(R.id.contactsImageView)
	ImageView contactsImageView;
	
	@Inject
	HistoryService historyService;
	
	SettingsAdapter adapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(container == null)
			return null;
		
		return (LinearLayout) inflater.inflate(R.layout.fragment_settings, container, false);
	}	
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		adapter = new SettingsAdapter(this.getActivity());
		
		settingsListView.setAdapter(adapter);
		
		settingsListView.setOnItemClickListener(settingSelectedListener);
		
		contactsImageView.setOnClickListener(contactsClickListener);
	};	
	
	OnItemClickListener settingSelectedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {

			if(position == 0) {
			
				
				
			}
			if(position == 1) {
				
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.share_subject);
				sendIntent.putExtra(Intent.EXTRA_TEXT, R.string.share_text);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);				
			}
			else if(position == 3) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(SettingsFragment.this.getActivity());
				builder.setTitle(R.string.attention);
				builder.setMessage(R.string.are_you_really_notifications)
				       .setCancelable(false)
				       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   
				        	   dialog.cancel();
				           }
				       })
				       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   
				        	   historyService.deleteAllNotifications();
				        	   dialog.dismiss();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	};	
	
	View.OnClickListener contactsClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			((MainActivity) getActivity()).selectPage(1);
		}
	};
	
	private class SettingsAdapter extends BaseAdapter {

		Context context;
		
		public SettingsAdapter(Context context) {
			this.context = context;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
    		LayoutInflater inflater = LayoutInflater.from(context);
    		if(position == 0) {
    			convertView = inflater.inflate(R.layout.item_setting, parent, false);
    			TextView labelText = (TextView) convertView.findViewById(R.id.textLabel);
    			labelText.setText(R.string.about);
    		}
    		else if(position == 1) {
    			convertView = inflater.inflate(R.layout.item_setting, parent, false);
    			TextView labelText = (TextView) convertView.findViewById(R.id.textLabel);
    			labelText.setText(R.string.tell_to_friend);
    		}
    		else if(position == 2) {
    			convertView = inflater.inflate(R.layout.item_switch, parent, false);
    			TextView labelText = (TextView) convertView.findViewById(R.id.textLabel);
    			labelText.setText(R.string.vibrate_notify);
    			
    			ToggleButton valueSwitch = (ToggleButton) convertView.findViewById(R.id.valueSwitch);
    			valueSwitch.setChecked(true);	
    		}
    		else if(position == 3) {
    			convertView = inflater.inflate(R.layout.item_setting, parent, false);
    			TextView labelText = (TextView) convertView.findViewById(R.id.textLabel);
    			labelText.setText(R.string.delete_notifications);
    		}

    		return convertView;
		}
	}
}
