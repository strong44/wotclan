package com.wot.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.view.client.ProvidesKey;

public class PlayerRatings implements Serializable, Comparable<PlayerRatings>{

	
//	public CommunityAccount(String idUser, String nameAccount) {
//		
//		this.idUser = idUser;
//		this.nameAccount = nameAccount;
//	}

    public static final ProvidesKey<PlayerRatings> KEY_PROVIDER = new ProvidesKey<PlayerRatings>() {
        @Override
        public Object getKey(PlayerRatings item) {
          return item == null ? null : item.getIdUser();
        }
      };
	/**
	 * 
	 */
	private static final long serialVersionUID = -1393222615715486990L;
	private String status;
	private Integer count;
	

	private String idUser = "000000";

	String nameAccount ;

	Map<String, DataCommunityAccountRatings> data;


	public List<String> listDates = new ArrayList<String>()  ;
	public List<Integer> listbattles = new ArrayList<Integer>()  ;
	public List<Integer> listBattlesWins = new ArrayList<Integer>();  
	
	public List<DataCommunityAccount> listBattlesTanks = new ArrayList<DataCommunityAccount>()  ;
	//public List<Integer> listBattlesTanksWins = new ArrayList<Integer>();  
	
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
	//private DataCommunityAccount data;
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	
	private List<DataCommunityAccountVehicules> listVehPlayedDay0;
	
    public List<DataCommunityAccountVehicules> getListVehPlayedDay0() {
		return listVehPlayedDay0;
	}

	public void setListVehPlayedDay0(List<DataCommunityAccountVehicules> listVehPlayed) {
		this.listVehPlayedDay0 = listVehPlayed;
	}

	private List<DataCommunityAccountVehicules> listVehPlayedDay1;
	
    public List<DataCommunityAccountVehicules> getListVehPlayedDay1() {
		return listVehPlayedDay1;
	}

	public void setListVehPlayedDay1(List<DataCommunityAccountVehicules> listVehPlayed) {
		this.listVehPlayedDay1 = listVehPlayed;
	}
	
	public Map<String, DataCommunityAccountRatings> getData() {
		return data;
	}

	public void setData(Map<String, DataCommunityAccountRatings> data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int compareTo(PlayerRatings o) {
		// TODO Auto-generated method stub
		return (o == null || o.getName() == null) ? -1 : o.getName().compareTo(getName());
	}

	@Override
    public boolean equals(Object o) {
      if (o instanceof PlayerRatings) {
        return getIdUser().equalsIgnoreCase(((PlayerRatings) o).getIdUser());
      }
      return false;
    }

	public void setListVehPlayedSincePreviousDay0(List<DataCommunityAccountVehicules> listVehPlayed) {
		// TODO Auto-generated method stub
		this.listVehPlayedDay0 = listVehPlayed;
	}
	
	public void setListVehPlayedSincePreviousDay1(List<DataCommunityAccountVehicules> listVehPlayed) {
		// TODO Auto-generated method stub
		this.listVehPlayedDay1 = listVehPlayed;
	}
	
}

