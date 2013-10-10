package com.wot.shared;

import java.io.Serializable;
import java.util.List;


public class Clan implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9216784168895883666L;
	private String status;
	private String status_code;
	private XmlWiki wiki;
	
	/////////////////
	public XmlWiki getWiki() {
		return wiki;
	}

	public void setWiki(XmlWiki wiki) {
		this.wiki = wiki;
	}

//	private DataClan data;
//	
//    public DataClan getData() {
//		return data;
//	}
//
//	public void setData(DataClan data) {
//		this.data = data;
//	}

	/**
	 * 
	 */
	
	List<ItemsDataClan> data; //données du clan 

	public List<ItemsDataClan> getItems() {
		return data;
	}

	public void setItems(List<ItemsDataClan> items) {
		this.data = items;
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

