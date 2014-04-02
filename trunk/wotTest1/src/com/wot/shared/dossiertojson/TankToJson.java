package com.wot.shared.dossiertojson;

import java.io.Serializable;

public class TankToJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6377301940120234997L;
	
	
	
	/**
	 * 			{"tankid": 0, "countryid": 3, "compDescr": 49, "active": 1, "type": 2, "type_name": "MT", "tier": 8, "premium": 1, "title": "Type 59", "icon": "ch01_type59", "icon_orig": "Ch01_Type59"},
				{"tankid": 1, "countryid": 3, "compDescr": 305, "active": 1, "type": 1, "type_name": "LT", "tier": 7, "premium": 1, "title": "Type 62", "icon": "ch02_type62", "icon_orig": "Ch02_Type62"},

	 */
	
	public int tankid ; 
	public int countryid ; 
	public int compDescr ; 
	public int active ; 
	public int type ; 
	public String type_name; 
	public int tier ; 
	public int premium ; 
	public String title ; 
	public String icon; 
	public String icon_orig;

}
