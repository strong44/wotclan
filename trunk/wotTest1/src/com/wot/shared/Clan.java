package com.wot.shared;

import java.io.Serializable;


public class Clan implements Serializable{

	private String status;
	private String status_code;
	
	private DataClan data;
	
    public DataClan getData() {
		return data;
	}

	public void setData(DataClan data) {
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

