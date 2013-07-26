package com.wot.server;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class DaoCommunityAccount implements Serializable{

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	private static final long serialVersionUID = -5612196058981975635L;
	
	@Persistent
	String name ;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Persistent
	private String status;
	
	@Persistent
	private String status_code;
	
	@Persistent
	private String idUser;
	
	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public Date getDateCommunityAccount() {
		return dateCommunityAccount;
	}

	public void setDateCommunityAccount(Date dateCommunityAccount) {
		this.dateCommunityAccount = dateCommunityAccount;
	}

	@Persistent
	private Date dateCommunityAccount;
	
	@Persistent
	private DaoDataCommunityAccount data;
	
    public DaoDataCommunityAccount getData() {
		return data;
	}

	public void setData(DaoDataCommunityAccount data) {
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

