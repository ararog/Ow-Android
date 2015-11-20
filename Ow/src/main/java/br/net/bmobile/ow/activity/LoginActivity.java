package br.net.bmobile.ow.activity;

import javax.inject.Inject;

import br.net.bmobile.ow.event.StartChatEvent;
import br.net.bmobile.ow.support.Session;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.net.bmobile.ow.R;
import br.net.bmobile.ow.model.Country;
import br.net.bmobile.ow.model.Login;
import br.net.bmobile.ow.model.Registration;
import br.net.bmobile.ow.model.User;
import br.net.bmobile.ow.service.CountryService;
import br.net.bmobile.ow.service.RestService;
import br.net.bmobile.ow.support.Constants;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.walnutlabs.android.ProgressHUD;

public class LoginActivity extends RoboActivity {

	static final int COUNTRY_SELECTED = 200;
	
	@Inject
	RestService restService;

	@Inject
	CountryService countryService;

    @Inject
    Session session;

	@InjectView(R.id.countryImageView)
	ImageView countryImageView;

	@InjectView(R.id.countryNameLabel)
    TextView countryNameLabel;
	
	@InjectView(R.id.countryCodeField)
	EditText countryCodeField;

	@InjectView(R.id.phoneNumberField)
	EditText phoneNumberField;
	
	ProgressHUD progressHUD;

    EventBus eventBus;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(loginClickListener);

		LinearLayout countrySelectorView = (LinearLayout) findViewById(R.id.countrySelectorView);
		countrySelectorView.setOnClickListener(chooseCountryClickListener);

		String locale = getResources().getConfiguration().locale.getCountry();
		if(locale != null) {
			
			Country country = countryService.findByCode(locale);
			if(country != null) {
		    	String countryName = countryService.findTranslationByCode(country.getCode());
				
				countryNameLabel.setText(countryName);
				countryCodeField.setText(country.getDialCode());
				
		    	int resource = getResources().getIdentifier("flag_" + country.getCode().toLowerCase(), 
		    			"drawable", getPackageName());
		    	
		    	if(resource != 0)
		    		countryImageView.setImageResource(resource);
			}
		}
		
		if(phoneNumberField.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams
            		.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            countryCodeField.clearFocus();
        }
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == COUNTRY_SELECTED) {
			
			if(resultCode == RESULT_OK) {
				
				Country country = Country.create(data.getStringExtra(Constants.Country));
				if(country != null) {
			    	String countryName = countryService.findTranslationByCode(country.getCode());
				
					countryNameLabel.setText(countryName);
					countryCodeField.setText(country.getDialCode());
					
			    	int resource = getResources().getIdentifier("flag_" + country
			    			.getCode().toLowerCase(), "drawable", getPackageName());
			    	
			    	if(resource != 0)
			    		countryImageView.setImageResource(resource);
				}
			}
		}		
	}
	
	Callback<User> loginCallback = new Callback<User>() {

		@Override
		public void failure(RetrofitError arg0) {

			progressHUD.dismiss();

			Registration registration = new Registration();
			registration.setCountryCode(countryCodeField.getText().toString());
			registration.setPhoneNumber(phoneNumberField.getText().toString());
			
			Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
			intent.putExtra(Constants.Registration, registration.serialize());
			startActivity(intent);
			finish();
		}

		@Override
		public void success(User user, Response arg1) {

			progressHUD.dismiss();

            session.setUser(user);
            session.saveData();

            eventBus.post(new StartChatEvent(user));

            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);

            startActivity(intent);

            finish();
		}
	};
	
	View.OnClickListener loginClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			progressHUD = ProgressHUD.show(LoginActivity.this, 
					getResources().getText(R.string.wait), true, false, null);
			
			Login login = new Login();
			login.setCountryCode(countryCodeField.getText().toString());
			login.setPhoneNumber(phoneNumberField.getText().toString());
			
			restService.signin(login, loginCallback);
		}
	};
	
	View.OnClickListener chooseCountryClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Intent intent = new Intent(LoginActivity.this, CountryActivity.class);
			startActivityForResult(intent, COUNTRY_SELECTED);
		}
	};
}
