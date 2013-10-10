package com.wot.shared;

import java.io.Serializable;


public class DataCommunityClanMembers implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7053292853390760673L;
	
	
	////////// not use =======
	private String account_id; 
    String created_at; 
    String updated_at; 
    String role_localised; 
    String role; 
    String account_name;
    
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	
}