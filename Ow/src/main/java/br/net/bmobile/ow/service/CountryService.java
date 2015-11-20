package br.net.bmobile.ow.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import br.net.bmobile.ow.model.Country;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CountryService {

    HashMap<String, Country> lookup;

    List<Country> countries;

    Context context;
    
    public CountryService(Context context) {

		ObjectMapper mapper = new ObjectMapper();

		this.context = context;
		
		try {
			if(countries == null) {
				String json = loadJSONFromAsset();
				if(json != null)
					countries = mapper.readValue(json, 
							new TypeReference<List<Country>>(){});

				lookup = new HashMap<String, Country>();
				
				for(Country c : countries)
					lookup.put(c.getCode(), c);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
    
    public List<Country> findAllCountries() {
    	
		return countries;
    }

    public List<Country> findByName(String name) {

    	List<Country> selectedCountries = new ArrayList<Country>();
		for(Country c : countries) {
			if(c.getName().toLowerCase()
					.contains(name.toLowerCase()))
				selectedCountries.add(c);
		}
		
		return selectedCountries;
    }
    
    public Country findByCode(String code) {
    	
    	return lookup.get(code);
    }
    
    public String findTranslationByCode(String code) {
    	
    	Locale locale = new Locale("", code);
    	
    	return locale.getDisplayCountry();
    }
    
	private String loadJSONFromAsset() {
	    String json = null;
	    try {
	        InputStream is = context.getAssets().open("countries.json");

	        int size = is.available();

	        byte[] buffer = new byte[size];

	        is.read(buffer);

	        is.close();

	        json = new String(buffer, "UTF-8");


	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }

	    return json;
	}    
}
