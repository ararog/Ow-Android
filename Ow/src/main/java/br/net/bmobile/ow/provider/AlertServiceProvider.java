package br.net.bmobile.ow.provider;

import android.content.Context;
import br.net.bmobile.ow.service.AlertService;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class AlertServiceProvider implements Provider<AlertService> {

	@Inject
	Context context;
	
	@Override
	public AlertService get() {
		// TODO Auto-generated method stub
		return new AlertService(context);
	}
}