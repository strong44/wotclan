package com.wot.shared;

import java.io.Serializable;

public class DataCommunityAccountStats implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2580719093758162023L;
	/**
	 * "stats": {
      "spotted": 6205, 
      "dropped_ctf_points": 3852, 
      "battle_avg_xp": 470, 
      "battles": 5334, 
      "damage_dealt": 2986570, 
      "battle_avg_performance": 52, 
      "integrated_rating": 13, 
      "frags": 4338, 
      "xp": 2505988, 
      "ctf_points": 6895, 
      "battle_wins": 2753
    }, */
	
	  private int spotted;
	  private int dropped_ctf_points;
	  private int battle_avg_xp; 
	  private int battles;
	  private int damage_dealt;
	  private int battle_avg_performance; 
	  private int integrated_rating; 
	  private int frags;
	  private int xp;
	  private int ctf_points;
	  private int battle_wins;
	  
	public int getSpotted() {
		return spotted;
	}
	public void setSpotted(int spotted) {
		this.spotted = spotted;
	}
	public int getDropped_ctf_points() {
		return dropped_ctf_points;
	}
	public void setDropped_ctf_points(int dropped_ctf_points) {
		this.dropped_ctf_points = dropped_ctf_points;
	}
	public int getBattle_avg_xp() {
		return battle_avg_xp;
	}
	public void setBattle_avg_xp(int battle_avg_xp) {
		this.battle_avg_xp = battle_avg_xp;
	}
	public int getBattles() {
		return battles;
	}
	public void setBattles(int battles) {
		this.battles = battles;
	}
	public int getDamage_dealt() {
		return damage_dealt;
	}
	public void setDamage_dealt(int damage_dealt) {
		this.damage_dealt = damage_dealt;
	}
	public int getBattle_avg_performance() {
		return battle_avg_performance;
	}
	public void setBattle_avg_performance(int battle_avg_performance) {
		this.battle_avg_performance = battle_avg_performance;
	}
	public int getIntegrated_rating() {
		return integrated_rating;
	}
	public void setIntegrated_rating(int integrated_rating) {
		this.integrated_rating = integrated_rating;
	}
	public int getFrags() {
		return frags;
	}
	public void setFrags(int frags) {
		this.frags = frags;
	}
	public int getXp() {
		return xp;
	}
	public void setXp(int xp) {
		this.xp = xp;
	}
	public int getCtf_points() {
		return ctf_points;
	}
	public void setCtf_points(int ctf_points) {
		this.ctf_points = ctf_points;
	}
	public int getBattle_wins() {
		return battle_wins;
	}
	public void setBattle_wins(int battle_wins) {
		this.battle_wins = battle_wins;
	}
	  
	  
	
}
