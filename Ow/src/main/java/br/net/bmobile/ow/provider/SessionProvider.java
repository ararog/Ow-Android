package br.net.bmobile.ow.provider;

import android.content.Context;
import br.net.bmobile.ow.support.Session;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SessionProvider implements Provider<Session> {

	@Inject
	Context context;
	
	@Override
	public Session get() {
		// TODO Auto-generated method stub
		return new Session(context);
	}

}
