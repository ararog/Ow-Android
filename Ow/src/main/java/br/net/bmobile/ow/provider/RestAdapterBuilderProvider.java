package br.net.bmobile.ow.provider;

import com.google.inject.Provider;

import retrofit.RestAdapter;

public class RestAdapterBuilderProvider implements Provider<RestAdapter.Builder> {
    
	@Override
    public RestAdapter.Builder get() {
        return new RestAdapter.Builder();
    }
}