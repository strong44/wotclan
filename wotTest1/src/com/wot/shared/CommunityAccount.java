package com.wot.shared;

import java.io.Serializable;

public class CommunityAccount implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1393222615715486990L;
	private String status;
	private String status_code;
	private String idUser;
	//private Date dateCommunityAccount;
	
	String nameAccount ;
	public String getName() {
		return nameAccount;
	}

	public void setName(String name) {
		this.nameAccount = name;
	}

	
	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String id) {
		this.idUser = id;
	}

//	public Date getDateCommunityAccount() {
//		return dateCommunityAccount;
//	}
//
//	public void setDateCommunityAccount(Date dateCommunityAccount) {
//		this.dateCommunityAccount = dateCommunityAccount;
//	}

	private DataCommunityAccount data;
	
    public DataCommunityAccount getData() {
		return data;
	}

	public void setData(DataCommunityAccount data) {
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

