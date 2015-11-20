package br.net.bmobile.ow.provider;

import javax.inject.Inject;

import br.net.bmobile.ow.service.RestService;

import com.google.inject.Provider;

import retrofit.RestAdapter;

public class RestServiceProvider implements Provider<RestService> {
	
    @Inject
    RestAdapter.Builder builder;

    @Override
    public RestService get() {
        return builder
                .setConverter(RestService.DATA_CONVERTER)
                .setEndpoint(RestService.SERVICE_ENDPOINT)
                .build()
                .create(RestService.class);
    }
}