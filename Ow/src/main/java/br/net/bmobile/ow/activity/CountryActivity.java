package br.net.bmobile.ow.activity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import br.net.bmobile.ow.R;
import br.net.bmobile.ow.model.Country;
import br.net.bmobile.ow.service.CountryService;
import br.net.bmobile.ow.support.Constants;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CountryActivity extends RoboActivity {

	@Inject
	CountryService countryService;
	
    @InjectView(R.id.countrySearchField)
    EditText countrySearchField;

    @InjectView(R.id.countriesListView)
    ListView countriesListView;

    @InjectView(R.id.backImageView)
	ImageView backImageView;

	CountryAdapter adapter;
	
	List<Country> countries;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_country);
		
		Button cancelSearchButton = (Button) findViewById(R.id.cancelSearchButton);
		cancelSearchButton.setOnClickListener(cancelClickListener);
		
		countries = new ArrayList<Country>(countryService.findAllCountries());
		
		adapter = new CountryAdapter(this, countries);
		
		countriesListView.setAdapter(adapter);
		
		countriesListView.setOnItemClickListener(countrySelectedListener);
		
		countrySearchField.addTextChangedListener(countryTextChangeListener);
		
		backImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CountryActivity.this.finish();				
			}
		});
	}
	
	View.OnClickListener cancelClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			countrySearchField.setText("");

			List<Country> countriesFound = countryService.findAllCountries();

			countries.clear();
			
			countries.addAll(countriesFound);

			adapter.notifyDataSetChanged();
		}
	};
	
	OnItemClickListener countrySelectedListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			
			Intent data = new Intent();
			Country country = countries.get(position);
			data.putExtra(Constants.Country, country.serialize());
			setResult(RESULT_OK, data);
			
			finish();
		}
	};
	
	TextWatcher countryTextChangeListener = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
			List<Country> countriesFound = countryService.findByName(s.toString());

			countries.clear();
			
			countries.addAll(countriesFound);

			adapter.notifyDataSetChanged();
		}
	};	
	
	private static class ViewHolder {
        ImageView countryImageView;
        TextView countryCodeLabel;
        TextView countryNameLabel;
	}
	
	private class CountryAdapter extends ArrayAdapter<Country> {

		public CountryAdapter(Context context, List<Country> objects) {
			super(context, R.layout.item_country, objects);
		}
		
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {

	    	Country country = getItem(position);    

	    	ViewHolder viewHolder; // view lookup cache stored in tag
	    	if (convertView == null) {
	    		viewHolder = new ViewHolder();
	    		LayoutInflater inflater = LayoutInflater.from(getContext());
	    		convertView = inflater.inflate(R.layout.item_country, parent, false);
	    		viewHolder.countryImageView = (ImageView) convertView.findViewById(R.id.countryImageView);
	    		viewHolder.countryCodeLabel = (TextView) convertView.findViewById(R.id.countryCodeLabel);
	    		viewHolder.countryNameLabel = (TextView) convertView.findViewById(R.id.countryNameLabel);
	    		convertView.setTag(viewHolder);
	    	} 
	    	else {
	    		viewHolder = (ViewHolder) convertView.getTag();
	    	}
	    	
	    	int resource = getResources().getIdentifier("flag_" + country.getCode().toLowerCase(), 
	    			"drawable", getPackageName());
	    	
	    	if(resource != 0)
	    		viewHolder.countryImageView.setImageResource(resource);
	    	
	    	String countryName = countryService
	    			.findTranslationByCode(country.getCode());
	    	
	    	viewHolder.countryNameLabel.setText(countryName);
	    	viewHolder.countryCodeLabel.setText(country.getDialCode());

	    	return convertView;
	   }		
	}
}
