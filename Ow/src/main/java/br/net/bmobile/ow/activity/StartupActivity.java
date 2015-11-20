package br.net.bmobile.ow.activity;

import javax.inject.Inject;

import roboguice.activity.RoboActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import br.net.bmobile.ow.support.ChatManager;
import br.net.bmobile.ow.support.Session;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StartupActivity extends RoboActivity {

	@Inject
	Session session;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		session.loadData();
		
		boolean isLoggedIn = session.getUser() == null ? false : true;
		
		Intent intent;
		if(isLoggedIn) {
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		else {
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		
        Intent startServiceIntent = new Intent(this, ChatManager.class);
        startService(startServiceIntent);		
	}	
}
