package com.wot.shared;

import java.io.Serializable;

public class CommunityAccount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5612196058981975635L;
	private String status;
	private String status_code;
	
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

