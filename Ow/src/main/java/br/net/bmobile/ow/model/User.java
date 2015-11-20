package br.net.bmobile.ow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class User {

    @JsonProperty("id")
	private Integer id;
	
	@JsonProperty("first_name")
	private String  firstName;
	
	@JsonProperty("last_name")
	private String  lastName;
	
	@JsonProperty("phone_number")
	private String  phoneNumber;
	
	@JsonProperty("country_code")
	private String  countryCode;
	
	public User() {
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
 
    static public User create(String serializedData) {
    	
    	ObjectMapper mapper = new ObjectMapper();
        try {
			return mapper.readValue(serializedData, User.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return null;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}	
}
