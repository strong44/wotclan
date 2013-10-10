package com.wot.shared;

import java.io.Serializable;

import com.google.gwt.view.client.ProvidesKey;


public class ItemsDataClan implements Serializable, Comparable<ItemsDataClan>{

	/**
	 * 
	 */
	
	
	 public static final ProvidesKey<ItemsDataClan> KEY_PROVIDER = new ProvidesKey<ItemsDataClan>() {
	        @Override
	        public Object getKey(ItemsDataClan item) {
	          return item == null ? null : item.getId();
	        }
	      };
	      
	private static final long serialVersionUID = -5761913518505341471L;

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
	String abbreviation;
	
    String name;
	
    String created_at;
	
	 String members_count;
	
  String owner_name;
	
  String motto;
	
   String clan_emblem_url;
	
   String clan_id;
	
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
		return members_count;
	}
	public void setMember_count(String member_count) {
		this.members_count = member_count;
	}
	public String getOwner() {
		return owner_name;
	}
	public void setOwner(String owner) {
		this.owner_name = owner;
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
		return clan_id;
	}
	public void setId(String id) {
		this.clan_id = id;
	}
	public String getClan_color() {
		return clan_color;
	}
	public void setClan_color(String clan_color) {
		this.clan_color = clan_color;
	}
	
	@Override
	public int compareTo(ItemsDataClan o) {
		// TODO Auto-generated method stub
		return (o == null || o.getName() == null) ? -1 : o.getName().compareTo(getName());
	}

	@Override
    public boolean equals(Object o) {
      if (o instanceof ItemsDataClan) {
        return getId().equalsIgnoreCase(((ItemsDataClan) o).getId());
      }
      return false;
    }
	
	
}