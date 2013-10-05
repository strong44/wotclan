package com.wot.shared;

import java.io.Serializable;
import java.util.List;

public class DataCommunityClan implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2019183340585632262L;
	List<DataCommunityClanMembers> members; //m�dailles du joueur

	public List<DataCommunityClanMembers> getMembers() {
		return members;
	}

	public void setMembers(List<DataCommunityClanMembers> members) {
		this.members = members;
	}

}
