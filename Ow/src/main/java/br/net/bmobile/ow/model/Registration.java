package br.net.bmobile.ow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Registration {

	@JsonProperty("country_code")
	private String countryCode;
	
	@JsonProperty("phone_number")
	private String phoneNumber;	
	
	@JsonProperty("code")
	private String code;

	public Registration() {
		// TODO Auto-generated constructor stub
	}
	
    public String serialize() {
    	ObjectMapper mapper = new ObjectMapper();

        try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return null;
    }

    static public Registration create(String serializedData) {

    	ObjectMapper mapper = new ObjectMapper();
        try {
			return mapper.readValue(serializedData, Registration.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return null;
    }

	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
