package br.net.bmobile.ow.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Contacts")
public class Contact {

	@DatabaseField(id = true, columnName = "Id")
	@JsonProperty("id")
	private Integer id;
	
	@DatabaseField(columnName = "FirstName")
	@JsonProperty("first_name")
	private String firstName;
	
	@DatabaseField(columnName = "LastName")
	@JsonProperty("last_name")
	private String lastName;
	
	@DatabaseField(columnName = "CountryCode")	
	@JsonProperty("country_code")
	private String countryCode;
	
	@DatabaseField(columnName = "PhoneNumber")
	@JsonProperty("phone_number")
	private String phoneNumber;

	public Contact() {

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

    static public Contact create(String serializedData) {

    	ObjectMapper mapper = new ObjectMapper();
        try {
			return mapper.readValue(serializedData, Contact.class);
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
}
