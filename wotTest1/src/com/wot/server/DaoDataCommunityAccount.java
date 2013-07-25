package com.wot.server;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class DaoDataCommunityAccount implements Serializable {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

	private static final long serialVersionUID = -2305944422816143878L;
	
	@Persistent
	String name ;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Persistent
	DaoDataCommunityAccountAchievements achievements; //m�dailles du joueur
	
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
	@Persistent
	DaoDataCommunityAccountStats stats;
	
	public DaoDataCommunityAccountStats getStats() {
		return stats;
	}

	public void setStats(DaoDataCommunityAccountStats stats) {
		this.stats = stats;
	}

	public DaoDataCommunityAccountAchievements getAchievements() {
		return achievements;
	}

	public void setAchievements(DaoDataCommunityAccountAchievements achievements) {
		this.achievements = achievements;
	}

}
