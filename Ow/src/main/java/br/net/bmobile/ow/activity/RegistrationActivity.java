package br.net.bmobile.ow.activity;

import javax.inject.Inject;

import com.walnutlabs.android.ProgressHUD;

import br.net.bmobile.ow.event.StartChatEvent;
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
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import br.net.bmobile.ow.R;
import br.net.bmobile.ow.model.Registration;
import br.net.bmobile.ow.model.User;
import br.net.bmobile.ow.service.RestService;
import br.net.bmobile.ow.support.Constants;
import br.net.bmobile.ow.support.Session;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegistrationActivity extends RoboActivity {

	@Inject
	RestService service;

	@Inject
	Session session;
	
	@InjectView(R.id.codeField)
	EditText codeField;
	
	Registration registration;
	
	ProgressHUD progressHUD;

    EventBus eventBus;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		Button confirmButton = (Button) findViewById(R.id.confirmButton);
		confirmButton.setOnClickListener(confirmClickListener);
		
		registration = Registration.create(
				getIntent().getStringExtra(Constants.Registration));
	}
	
	Callback<User> confirmationCallback = new Callback<User>() {

		@Override
		public void failure(RetrofitError arg0) {

			progressHUD.dismiss();
			
			arg0.printStackTrace();
		}

		@Override
		public void success(User user, Response arg1) {
			
			progressHUD.dismiss();
			
			session.setUser(user);
			session.saveData();

            eventBus.post(new StartChatEvent(user));

            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	View.OnClickListener confirmClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			progressHUD = ProgressHUD.show(RegistrationActivity.this, 
					getResources().getText(R.string.wait), true, false, null);
			
			Editable value = codeField.getText();
			if(value != null)
				registration.setCode(value.toString());
			
			service.verify(registration, confirmationCallback);			
		}
	};	
}
