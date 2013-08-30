package com.wot.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class DaoItemsDataClan implements Serializable{

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	private static final long serialVersionUID = 2005352612781524708L;
	/*
	 * "abbreviation": "NVS", 
        "created_at": 1328978449.00, 
        "name": "NOVA SNAIL", 
        "member_count": 57, 
        "owner": "hentz44", 
        "motto": "Un escargot qui avale un obus a confiance en sa coquille.", 
        "clan_emblem_url": "http://cw.worldoftanks.eu/media/clans/emblems/clans_5/500006074/emblem_64x64.png", 
        "id": 500006074, 
        "clan_color": "#4a426c"
	 */
	@Persistent
	String abbreviation;
	
	@Persistent
    String name;
	
	@Persistent
    String created_at;
	
	@Persistent
    String member_count;
	
	@Persistent
    String owner;
	
	@Persistent
    String motto;
	
	@Persistent
    String clan_emblem_url;
	
	@Persistent
    String id;
	
	@Persistent
    String clan_color;
    
    
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getMember_count() {
		return member_count;
	}
	public void setMember_count(String member_count) {
		this.member_count = member_count;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getMotto() {
		return motto;
	}
	public void setMotto(String motto) {
		this.motto = motto;
	}
	public String getClan_emblem_url() {
		return clan_emblem_url;
	}
	public void setClan_emblem_url(String clan_emblem_url) {
		this.clan_emblem_url = clan_emblem_url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClan_color() {
		return clan_color;
	}
	public void setClan_color(String clan_color) {
		this.clan_color = clan_color;
	}
	
}