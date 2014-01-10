package com.wot.shared;

import java.io.Serializable;

public class DataPlayerTankRatingsStatistics implements Serializable{

	/**
	 * "statistics":{ 
		"clan"
		:{ 
		"spotted":0,"hits":0,"battle_avg_xp":0,"draws":0,"wins":0,"losses":0,"capture_points":0,"battles":0,"damage_dealt":0,"hits_percents":0,"damage_received":0,"shots":0,"xp":0,"frags":0,"survived_battles":0,"dropped_capture_points":0
		} 
		,"all"
		:{ 
		"spotted":0,"hits":0,"battle_avg_xp":0,"draws":0,"wins":816,"losses":0,"capture_points":0,"battles":1482,"damage_dealt":0,"hits_percents":0,"damage_received":0,"shots":0,"xp":0,"frags":0,"survived_battles":0,"dropped_capture_points":0
		} 
		,"max_xp":0,"wins":816,"company"
		:{ 
		"spotted":0,"hits":0,"battle_avg_xp":0,"draws":0,"wins":0,"losses":0,"capture_points":0,"battles":0,"damage_dealt":0,"hits_percents":0,"damage_received":0,"shots":0,"xp":0,"frags":0,"survived_battles":0,"dropped_capture_points":0
		} 
		,"battles":1482,"max_frags":0,"win_and_survived":0
		} 

	 */
	private static final long serialVersionUID = 6100888880210621451L;

	// clan
	
	DataPlayerTankRatingsStatisticsAll all;

	public DataPlayerTankRatingsStatisticsAll getAll() {
		return all;
	}

	public void setAll(DataPlayerTankRatingsStatisticsAll all) {
		this.all = all;
	}
	
	
	
}
