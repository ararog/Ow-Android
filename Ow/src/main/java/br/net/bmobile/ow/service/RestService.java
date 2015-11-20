package br.net.bmobile.ow.service;

import java.util.List;

import retrofit.Callback;
import retrofit.converter.Converter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import br.net.bmobile.ow.model.Contact;
import br.net.bmobile.ow.model.Login;
import br.net.bmobile.ow.model.Registration;
import br.net.bmobile.ow.model.User;
import br.net.bmobile.ow.support.JacksonConverter;

public interface RestService {
	
	Converter DATA_CONVERTER = new JacksonConverter();
	String SERVICE_ENDPOINT = "http://oww.herokuapp.com";
	
	@Headers("Accept: */*")
	@POST("/api/v1/users/login")
	void signin(@Body Login request, Callback<User> callback);
	
	@Headers("Accept: */*")
	@POST("/api/v1/users/verify")
	void verify(@Body Registration request, Callback<User> callback);

	@GET("/api/v1/users/{id}/contacts")
	void loadContacts(@Path("id") int id, Callback<List<Contact>> callback);
	
	@Headers("Accept: */*")
	@POST("/api/v1/users/{id}/contacts/sync")
	void syncContacts(@Path("id") int id, @Body List<Contact> request, Callback<List<Contact>> callback);
}