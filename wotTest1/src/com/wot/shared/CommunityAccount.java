package com.wot.shared;

import java.io.Serializable;

import com.google.gwt.view.client.ProvidesKey;
import com.wot.client.ContactDatabase.ContactInfo;

public class CommunityAccount implements Serializable, Comparable<CommunityAccount>{

	
//	public CommunityAccount(String idUser, String nameAccount) {
//		
//		this.idUser = idUser;
//		this.nameAccount = nameAccount;
//	}

    public static final ProvidesKey<CommunityAccount> KEY_PROVIDER = new ProvidesKey<CommunityAccount>() {
        @Override
        public Object getKey(CommunityAccount item) {
          return item == null ? null : item.getIdUser();
        }
      };
	/**
	 * 
	 */
	private static final long serialVersionUID = -1393222615715486990L;
	private String status;
	private String status_code;
	private String idUser = "000000";
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

	@Override
	public int compareTo(CommunityAccount o) {
		// TODO Auto-generated method stub
		return (o == null || o.getName() == null) ? -1 : o.getName().compareTo(getName());
	}

	@Override
    public boolean equals(Object o) {
      if (o instanceof CommunityAccount) {
        return getIdUser().equalsIgnoreCase(((CommunityAccount) o).getIdUser());
      }
      return false;
    }
	
	
	
}

