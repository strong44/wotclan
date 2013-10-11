package com.wot.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class DaoDataCommunityMembers implements Serializable {
	/**
	 * 
	 */
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
	
	private static final long serialVersionUID = -4956114545804795075L;
	////////// not use =======
	@Persistent
	String account_id;
	@Persistent
    String created_at;
	@Persistent
    String updated_at;
	@Persistent
    String role;
	@Persistent
    String account_name;
    
}
