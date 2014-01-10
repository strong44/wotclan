package com.wot.shared;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PlayerTankRatings implements Serializable{

	
/*
 * {"status":"ok","count":1,"data"
:{ 
"506486576":[{"achievements"
:{ 
"medal_dumitru":0,"invader":0,"medal_lehvaslaiho":0,"warrior":0,"medal_halonen":0,"medal_pascucci":0,"medal_orlik":0,"medal_brothers_in_arms":0,"mousebane":0,"medal_bruno_pietro":0,"medal_delanglade":0,"lucky_devil":0,"defender":0,"armor_piercer":0,"medal_kay":0,"supporter":0,"steelwall":0,"max_sniper_series":0,"medal_knispel":0,"medal_boelter":0,"medal_ekins":0,"medal_heroes_of_rassenay":0,"medal_tamada_yoshio":0,"max_piercing_series":0,"medal_radley_walters":0,"kamikaze":0,"sinai":0,"sniper":0,"medal_tarczay":0,"scout":0,"medal_oskin":0,"medal_burda":0,"medal_billotte":0,"huntsman":0,"hand_of_death":0,"medal_fadin":0,"medal_lafayette_pool":0,"max_killing_series":0,"medal_lavrinenko":0,"medal_kolobanov":0,"patton_valley":0,"bombardier":0,"medal_abrams":0,"max_invincible_series":0,"medal_poppel":0,"medal_crucial_contribution":0,"raider":0,"max_diehard_series":0,"invincible":0,"lumberjack":0,"sturdy":0,"title_sniper":0,"iron_man":0,"diehard":0,"medal_carius":0,"medal_le_clerc":0,"beasthunter":0,"evileye":0,"medal_nikolas":0
} 
,"statistics"
:{ 
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
,"last_battle_time":0,"mark_of_mastery":4,"in_garage":0,"tank_id":2817
} 

 */


	/**
	 * 
	 */
	private static final long serialVersionUID = -9044481415634057416L;
	/**
	 * 
	 */
	private String status;
	private Integer count;
	
	Map<String, List<DataPlayerTankRatings>> data;


	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Map<String, List<DataPlayerTankRatings>> getData() {
		return data;
	}

	public void setData(Map<String, List<DataPlayerTankRatings>> data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

	
}

