package com.wot.shared.dossiertojson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wot.server.WotServiceImpl;
import com.wot.shared.Clan;


public class DossierToJsonTanks implements Serializable{

	/**
	 * 
	 * "tanks": [{

	"clan": {"spotted": 0, "capture_points": 0, "losses": 0, "survived": 0, "defence_points": 0, "battles_count_before_89": 0, "hits": 0, "experience": 0, "damage_dealt": 0, "victories": 0, "xp_before_89": 0, 
	"battles": 0, "shots": 0, "damage_received": 0, "frags": 0}, 
	
	"_7x7": {"spotted": 0, "capture_points": 0, "survived": 0, "defence_points": 0, "losses": 0, "hits": 0, "battles": 0, "max_experience": 0, "damage_dealt": 0, "experience": 0, "victories": 0, "damage_received": 0, 
	"max_frags": 0,	"shots": 0, "original_experience": 0, "tier8_frags": 0, "frags": 0, "max_damage": 0, "survived_with_victory": 0}, 
	
	"updated": 1385418195, 
	
	"_15x15": {"spotted": 6, "experience_before_88": 0, "capture_points": 0, "losses": 2, "survived": 3, "defence_points": 0, "original_experience": 2014, "hits": 61, "battles": 6, "max_experience": 697, "damage_dealt": 1815, "experience": 2014, "victories": 4, "damage_received": 2260, "max_frags": 2, "shots": 98, "battles_before_88": 0, "tier8_frags": 0, "frags": 8, "max_damage": 155, "survived_with_victory": 2}, 
	
	"amounts": {"mileage": 6360, "trees_knocked_down": 4, "battles": 0, "patton_frags": 0, "beast_frags": 0, "sinai_frags": 0}, 
	
	"series": {
	"survivor_progress": 0, "master_gunner": 8, "survivor": 1, "invincible": 0, "sharpshooter": 15, "reaper": 1, "master_gunner_progress": 0, "invincible_progress": 0, "reaper_progress": 0, "sharpshooter_progress": 5}, 
	
	"company": {"spotted": 0, "hits": 0, "shots": 0, "defence_points": 0, "losses": 0, "capture_points": 0, "experience": 0, "damage_dealt": 0, "victories": 0, "xp_before_89": 0, "battles": 0, "survived": 0, "battles_before_89": 0, "damage_received": 0, "frags": 0}, 
	
	"epics": {
	"orlik": 0, "boelter": 0, "burda": 0, "tarczay": 0, "dumitru": 0, "kolobanov": 0, "fadin": 0, "halonen": 0, "billotte": 0, "nikolas": 0, "de_langlade": 0, "tamada_yoshio": 0, "radley_walters": 0, "pascucci": 0, "bruno_pietro": 0, "lehvaslaiho": 0, "lafayette_pool": 0, "heroes_of_raseiniai": 0, "oskin": 0}, 
	
	"id": 52, 
	"frag_counts": [[0, 12, 1], [0, 25, 1], [1, 25, 1], [1, 17, 1], [2, 13, 1], [5, 25, 1], [1, 61, 1], [2, 19, 1]], 
	
	"awards": {
	"brothers_in_arms": 0, "invader": 0, "mastery_mark": 3, "top_gun": 0, "steel_wall": 1, "lucky_devil": 0, "defender": 0, "crucial_contribution": 0, "raider": 0, "mouse_trap": 0, "kamikaze": 0, "sinai": 0, "sniper": 0, "spartan": 0, "hunter": 0, "ranger": 0, "scout": 0, "confederate": 0, "patton_valley": 0, "bombardier": 0, "cool_headed": 1, "battle_hero": 1, "patrol_duty": 0, "survivor": 0, "invincible": 0, "sharpshooter": 1, "reaper": 0}, 
	
	"country": 1, 
	"play_time": 1764, 
	"last_time_played": 1385418195, 
	"medals": {"abrams": 0, "carius": 0, "lavrinenko": 0, "poppel": 0, "knispel": 0, "ekins": 0, "kay": 4, "leclerk": 0}}, 
	 
	 */
	private static final long serialVersionUID = -4123989307870798435L; 

	//clan
	//DossierToJsonClan clan;
	
	//_7x7 "_7x7": {"spotted": 33, "capture_points": 175, "survived": 26, "defence_points": 47, "losses": 31, "hits": 266, "battles": 59, "max_experience": 1110, "damage_dealt": 68683, "experience": 23575, "victories": 28, "damage_received": 61792, "max_frags": 3, "shots": 334, "original_experience": 23575, "tier8_frags": 31, "frags": 39, "max_damage": 2949, "survived_with_victory": 23}, 

	DossierToJson77 _7x7;
	
	//updated
	double updated;
	
	//_15*15  "_15x15": {"spotted": 232, "experience_before_88": 114714, "capture_points": 258, "losses": 152, "survived": 95, "defence_points": 218, "original_experience": 43435, "hits": 1351, "battles": 291, "max_experience": 1554, "damage_dealt": 310259, "experience": 158291, "victories": 132, "damage_received": 354357, "max_frags": 8, "shots": 1772, "battles_before_88": 178, "tier8_frags": 121, "frags": 201, "max_damage": 3253, "survived_with_victory": 78}, 

	DossierToJson1515 _15x15;
	
	//amounts
	
	//series
	
	//company
	
	//epics
	
	//id of tank
	int id;
	
	//frag_counts "frag_counts": [[0, 12, 1], [0, 25, 1], [1, 25, 1], [1, 17, 1], [2, 13, 1], [5, 25, 1], [1, 61, 1], [2, 19, 1]], 
	
	//awards
	
	//country
	int country;
	
	//play_time
	double play_time;
	
	//last_time_played
	double last_time_played;
	
	//medals
	
	
	public static void main(String[] args) {
		URL urlClan = null ;
		//input = input.replace(" ", "%20");
		
		try {
			if(WotServiceImpl.lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				urlClan = new URL(WotServiceImpl.proxy + "http://wot-dossier.appspot.com//dossier-data/5726971199750144");					
			}
			else {
				//NVS : 500006074
				//urlClan = new URL("http://api.worldoftanks.eu/community/clans/500006074/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");
				//http://api.worldoftanks.eu/2.0/clan/list/?application_id=d0a293dc77667c9328783d489c8cef73&search=
				urlClan = new URL("http://wot-dossier.appspot.com//dossier-data/5726971199750144" );		
			}
			
			//lecture de la rÃ©ponse recherche du clan
			HttpURLConnection conn = (HttpURLConnection)urlClan.openConnection();
			conn.setReadTimeout(60000);
			conn.setConnectTimeout(60000);
			//conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			//BufferedReader reader = new BufferedReader(new InputStreamReader(urlClan.openStream(), "UTF-8"));
			String line = "";
			String AllLines = "";

			while ((line = reader.readLine()) != null) {
				AllLines = AllLines + line;
			}
			
			reader.close();

			Gson gson = new Gson();
			//System.out.println("before " + AllLines);
			
			//parsing gson
			DossierToJson clan = gson.fromJson(AllLines, DossierToJson.class );
			System.out.println(clan.tanks);
			
			//il nous faut les descriptions des tanks pour chaque ID 
			//https://raw.githubusercontent.com/Phalynx/WoT-Dossier-Cache-to-JSON/master/tanks.json
			/*
			 * [
				{"tankid": 0, "countryid": 3, "compDescr": 49, "active": 1, "type": 2, "type_name": "MT", "tier": 8, "premium": 1, "title": "Type 59", "icon": "ch01_type59", "icon_orig": "Ch01_Type59"},
				{"tankid": 1, "countryid": 3, "compDescr": 305, "active": 1, "type": 1, "type_name": "LT", "tier": 7, "premium": 1, "title": "Type 62", "icon": "ch02_type62", "icon_orig": "Ch02_Type62"},
			 */
			
			//ItemsDataClan  myItemsDataClan = null ;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		System.exit(0);
	}
	
	
	
	
	
}
