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
	/* http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461,462
	 * {
	{"status":"ok","count":2,
	"data":{ 
		"461":{ 
		"spotted":{ "place":97640,"value":28440},
		"dropped_ctf_points":{"place":360995,"value":12639},
		"battle_avg_xp":{ 
	"place":94606,"value":650},"battles":{ 
	"place":288113,"value":18370},"damage_dealt":{ 
	"place":254542,"value":18500952},"frags":{ 
	"place":209141,"value":18488},"ctf_points":{ 
	"place":873803,"value":14755},"integrated_rating":{ 
	"place":173662,"value":18},"xp":{ 
	"place":147710,"value":11943200},"battle_avg_performance":{ 
	"place":295870,"value":54},"battle_wins":{ 
	"place":237633,"value":9886}},
	"462":{ 
	"spotted":{ 
	"place":165842,"value":24130},"dropped_ctf_points":{ 
	"place":364655,"value":12580},"battle_avg_xp":{ 
	"place":869330,"value":472},"battles":{ 
	"place":545346,"value":14467},"damage_dealt":{ 
	"place":475334,"value":13284143},"frags":{ 
	"place":347706,"value":14965},"ctf_points":{ 
	"place":186236,"value":29660},"integrated_rating":{ 
	"place":526707,"value":6},"xp":{ 
	"place":543957,"value":6829602},"battle_avg_performance":{ 
	"place":128047,"value":56},"battle_wins":{ 
	"place":420199,"value":8072}}}}
	 */

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

