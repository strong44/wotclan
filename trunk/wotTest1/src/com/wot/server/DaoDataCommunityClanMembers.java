package com.wot.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class DaoDataCommunityClanMembers implements Serializable {

	private static final long serialVersionUID = 6538645333499345875L;
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	public Key getKey() {
		return key;
	}

//	public String getAccount_name() {
//		return account_name;
//	}
//
//	public void setAccount_name(String account_name) {
//		this.account_name = account_name;
//	}

	public void setKey(Key key) {
		this.key = key;
	}

//	@Persistent
//	private String account_id;
//	
//	@Persistent
//    String created_at;
//	
//	@Persistent
//    String updated_at;
//	
//	@Persistent
//    String role_localised;
//	
//	@Persistent
//    String role;
//	
//	@Persistent
//    String account_name;
	
	///ajout test
	@Persistent
	Map<String, DaoDataCommunityMembers> members;
	
	@Persistent
	Map<String, String> membersAdded = new HashMap<String, String>();
	
	@Persistent
	Map<String, String> membersDeleted = new HashMap<String, String>();
	
	
	public Map<String, String> getMembersAdded() {
		return membersAdded;
	}

	public Map<String, String> getMembersDeleted() {
		return membersDeleted;
	}

	public void setMembersAdded(Map<String, String> membersAdded) {
		this.membersAdded = membersAdded;
	}

	public void setMembersDeleted(Map<String, String> membersDeleted) {
		this.membersDeleted = membersDeleted;
	}

	public Map<String, DaoDataCommunityMembers> getMembers() {
		return members;
	}

	public void setMembers(Map<String, DaoDataCommunityMembers> members) {
		this.members = members;
	}

//	public String getAccount_id() {
//		return account_id;
//	}
//	public void setAccount_id(String account_id) {
//		this.account_id = account_id;
//	}
	
}