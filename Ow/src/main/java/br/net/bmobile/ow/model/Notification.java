package br.net.bmobile.ow.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Notifications")
public class Notification {

	@DatabaseField(generatedId = true)
	@JsonProperty("id")
	private Integer id;
	
	@DatabaseField(columnName = "Type")	
	private Integer type;

	@DatabaseField(columnName = "Content")	
	private String content;

	@DatabaseField(columnName = "Date")	
	@JsonIgnore
	private Date date;

	@DatabaseField(columnName = "Read")	
	@JsonIgnore
	private boolean read;
	
	@DatabaseField(canBeNull = false, foreign = true)
	private Contact contact;
	
	public Notification() {
		
	}
	
    public String typeToString() {
    	
    	String text = "";
    	
        switch (type) {
            case 1:
                text = "vamo";
                break;
            case 2:
                text = "perai";
                break;
            case 3:
                text = "chegou";
                break;
            case 4:
                text = "maneiro";
                break;
            case 5:
                text = "tocomfome";
                break;
            case 6:
                text = "owkey";
                break;
            default:
                break;
        }
        
        return text;
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
