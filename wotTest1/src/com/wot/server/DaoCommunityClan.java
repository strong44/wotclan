package com.wot.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.wot.shared.DataCommunityClan;

@PersistenceCapable
public class DaoCommunityClan implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3853517466442689107L;
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	
	private String status;
	private String status_code;
	
	@Persistent
	private DaoDataCommunityClan data;
	
	
    public DaoDataCommunityClan getData() {
		return data;
	}

	public void setData(DaoDataCommunityClan data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus_code() {
		return status_code;
	}

	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}

	
	
	
	
	
	
	
	
	
}

