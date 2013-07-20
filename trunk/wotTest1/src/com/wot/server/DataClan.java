package com.wot.server;

import java.io.Serializable;
import java.util.List;

public class DataClan implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3833805721188019694L;
	List<ItemsDataClan> items; //médailles du joueur

	public List<ItemsDataClan> getItems() {
		return items;
	}

	public void setItems(List<ItemsDataClan> items) {
		this.items = items;
	}

	

}
