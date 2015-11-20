package br.net.bmobile.ow.support;

import android.content.Context;
import android.content.SharedPreferences;
import br.net.bmobile.ow.model.User;

public class Session {

	private static String PREFERENCES_NAME = "Session";
	private static String USER_KEY = "user";
	 	
	private Context context;
	private User user;
	private boolean mute;
	
	public Session(Context context) {
	
		this.context = context;
	}
	
	public void loadData() {

		SharedPreferences preferences = context.
				getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		
		user = User.create(preferences.getString(USER_KEY, null));
	}

	public void saveData() {
		
		SharedPreferences preferences = context
				.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(USER_KEY, user.serialize());
		editor.commit();		
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isMute() {
		return mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}
}
