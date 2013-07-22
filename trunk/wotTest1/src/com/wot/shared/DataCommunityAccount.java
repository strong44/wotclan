package com.wot.shared;

public class DataCommunityAccount {
	
	String name ;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	DataCommunityAccountAchievements achievements; //m�dailles du joueur
	
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
    }, 

	 * @return
	 */

	DataCommunityAccountStats stats;
	
	public DataCommunityAccountStats getStats() {
		return stats;
	}

	public void setStats(DataCommunityAccountStats stats) {
		this.stats = stats;
	}

	public DataCommunityAccountAchievements getAchievements() {
		return achievements;
	}

	public void setAchievements(DataCommunityAccountAchievements achievements) {
		this.achievements = achievements;
	}

}
