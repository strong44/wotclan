package com.wot.shared;

import java.io.Serializable;
import java.util.List;

public class DataClan implements Serializable {
	
	List<ItemsDataClan> items; //données du clan 

	public List<ItemsDataClan> getItems() {
		return items;
	}

	public void setItems(List<ItemsDataClan> items) {
		this.items = items;
	}

	

}
