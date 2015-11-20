package br.net.bmobile.ow.support;

import retrofit.RestAdapter;
import br.net.bmobile.ow.provider.AlertServiceProvider;
import br.net.bmobile.ow.provider.ContactServiceProvider;
import br.net.bmobile.ow.provider.CountryServiceProvider;
import br.net.bmobile.ow.provider.HistoryServiceProvider;
import br.net.bmobile.ow.provider.RestAdapterBuilderProvider;
import br.net.bmobile.ow.provider.RestServiceProvider;
import br.net.bmobile.ow.provider.SessionProvider;
import br.net.bmobile.ow.service.AlertService;
import br.net.bmobile.ow.service.ContactService;
import br.net.bmobile.ow.service.CountryService;
import br.net.bmobile.ow.service.HistoryService;
import br.net.bmobile.ow.service.RestService;

import com.google.inject.AbstractModule;

public class OwModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(Session.class)
			.toProvider(SessionProvider.class)
			.asEagerSingleton();

		bind(RestAdapter.Builder.class)
			.toProvider(RestAdapterBuilderProvider.class)
			.asEagerSingleton();
		
		bind(RestService.class)
			.toProvider(RestServiceProvider.class)
			.asEagerSingleton();
		
		bind(CountryService.class)
			.toProvider(CountryServiceProvider.class)
			.asEagerSingleton();		

		bind(ContactService.class)
			.toProvider(ContactServiceProvider.class);		

		bind(HistoryService.class)
			.toProvider(HistoryServiceProvider.class);		

		bind(AlertService.class)
			.toProvider(AlertServiceProvider.class);		
	}
}