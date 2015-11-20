package br.net.bmobile.ow.provider;

import android.content.Context;
import br.net.bmobile.ow.service.CountryService;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class CountryServiceProvider implements Provider<CountryService> {

	@Inject
	Context context;
	
	@Override
	public CountryService get() {
		// TODO Auto-generated method stub
		return new CountryService(context);
	}
}
