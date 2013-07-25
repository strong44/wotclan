package com.wot.shared;

import java.io.Serializable;


public class CommunityClan implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1244678860855383625L;
	private String status;
	private String status_code;
	
	private DataCommunityClan data;
	
    public DataCommunityClan getData() {
		return data;
	}

	public void setData(DataCommunityClan data) {
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

