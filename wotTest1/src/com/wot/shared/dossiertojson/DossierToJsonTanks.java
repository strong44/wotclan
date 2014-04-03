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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wot.server.WotServiceImpl;
import com.wot.shared.Clan;


public class DossierToJsonTanks implements Serializable{
	public enum EnumOrder{
		asc,
		desc
	};
	
	public enum EnumAttrToSort {
		tankid, 
		countryid, 
		compDescr , 
		active , 
		type , 
		type_name , 
		tier , 
		premium , 
		title , 
		icon , 
		icon_orig 
	};
	
	
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

	private DossierToJson77 _7x7;
	
	//updated
	private double updated;
	
	//_15*15  "_15x15": {"spotted": 232, "experience_before_88": 114714, "capture_points": 258, "losses": 152, "survived": 95, "defence_points": 218, "original_experience": 43435, "hits": 1351, "battles": 291, "max_experience": 1554, "damage_dealt": 310259, "experience": 158291, "victories": 132, "damage_received": 354357, "max_frags": 8, "shots": 1772, "battles_before_88": 178, "tier8_frags": 121, "frags": 201, "max_damage": 3253, "survived_with_victory": 78}, 

	private DossierToJson1515 _15x15;
	
	//amounts
	
	//series
	
	//company
	
	//epics
	
	//id of tank
	private int id;
	
	//frag_counts "frag_counts": [[0, 12, 1], [0, 25, 1], [1, 25, 1], [1, 17, 1], [2, 13, 1], [5, 25, 1], [1, 61, 1], [2, 19, 1]], 
	
	//awards
	
	//country
	private int country;
	
	//play_time
	private double play_time;
	
	//last_time_played
	private double last_time_played;
	
	//medals
	
	
	//data from tankToJson (githuh !!)
	//{"tankid": 0, "countryid": 3, "compDescr": 49, "active": 1, "type": 2, "type_name": "MT", "tier": 8, "premium": 1, "title": "Type 59", 
	//"icon": "ch01_type59", "icon_orig": "Ch01_Type59"},

	//int tankid ; 
	//int countryid ; 
	private int compDescr ; 
	private int active ; 
	private int type ; 
	private String type_name ; 
	private int tier ; 
	private int premium ; 
	private String title; 
	private String icon ; 
	private String icon_orig ;

	
	
	
	public int getCountry() {
		return country;
	}
	public String getIcon() {
		return icon;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public DossierToJson77 get_7x7() {
		return _7x7;
	}

	public double getUpdated() {
		return updated;
	}

	public DossierToJson1515 get_15x15() {
		return _15x15;
	}


	public int getId() {
		return id;
	}


	public double getPlay_time() {
		return play_time;
	}

	public double getLast_time_played() {
		return last_time_played;
	}

	public int getCompDescr() {
		return compDescr;
	}


	public int getActive() {
		return active;
	}



	public int getType() {
		return type;
	}


	public String getType_name() {
		return type_name;
	}

	public int getTier() {
		return tier;
	}


	public int getPremium() {
		return premium;
	}

	public String getTitle() {
		return title;
	}

	public String getIcon_orig() {
		return icon_orig;
	}

	public void set_7x7(DossierToJson77 _7x7) {
		this._7x7 = _7x7;
	}


	public void setUpdated(double updated) {
		this.updated = updated;
	}




	public void set_15x15(DossierToJson1515 _15x15) {
		this._15x15 = _15x15;
	}


	public void setId(int id) {
		this.id = id;
	}


	public void setPlay_time(double play_time) {
		this.play_time = play_time;
	}

	public void setLast_time_played(double last_time_played) {
		this.last_time_played = last_time_played;
	}


	public void setCompDescr(int compDescr) {
		this.compDescr = compDescr;
	}



	public void setActive(int active) {
		this.active = active;
	}
	public void setType(int type) {
		this.type = type;
	}


	public void setType_name(String type_name) {
		this.type_name = type_name;
	}


	public void setTier(int tier) {
		this.tier = tier;
	}



	public void setPremium(int premium) {
		this.premium = premium;
	}

	public void setTitle(String title) {
		this.title = title;
	}



	public void setIcon_orig(String icon_orig) {
		this.icon_orig = icon_orig;
	}


	//////////////////
	public static void main(String[] args) {
		URL url = null ;
		//input = input.replace(" ", "%20");
		
		//to avoid SSL Protcole erreur ?
		System.setProperty("jsse.enableSNIExtension", "false");
		
		try {
			//5726971199750144 correspond à l'ID de mon dossier cache dans wot-dossier.appspot.com
			if(WotServiceImpl.lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				url = new URL(WotServiceImpl.proxy + "http://wot-dossier.appspot.com//dossier-data/5726971199750144");					
			}
			else {
				url = new URL("http://wot-dossier.appspot.com//dossier-data/5726971199750144" );		
			}
			
			//lecture de la rÃ©ponse recherche du clan
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
			DossierToJson dossierToJson = gson.fromJson(AllLines, DossierToJson.class );
			//System.out.println(dossierToJson.tanks);
			
			//il nous faut les descriptions des tanks pour chaque ID 
			//https://raw.githubusercontent.com/Phalynx/WoT-Dossier-Cache-to-JSON/master/tanks.json
			/*
			 * [
				{"tankid": 0, "countryid": 3, "compDescr": 49, "active": 1, "type": 2, "type_name": "MT", "tier": 8, "premium": 1, "title": "Type 59", "icon": "ch01_type59", "icon_orig": "Ch01_Type59"},
				{"tankid": 1, "countryid": 3, "compDescr": 305, "active": 1, "type": 1, "type_name": "LT", "tier": 7, "premium": 1, "title": "Type 62", "icon": "ch02_type62", "icon_orig": "Ch02_Type62"},
			 */
			
			if(WotServiceImpl.lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				url = new URL(WotServiceImpl.proxy + "https://raw.githubusercontent.com/Phalynx/WoT-Dossier-Cache-to-JSON/master/tanks.json");					
			}
			else {
				//NVS : 500006074
				url = new URL("https://raw.githubusercontent.com/Phalynx/WoT-Dossier-Cache-to-JSON/master/tanks.json" );		
			}
			
			//lecture de la rÃ©ponse recherche du clan
			conn = (HttpURLConnection)url.openConnection();
			conn.setReadTimeout(60000);
			conn.setConnectTimeout(60000);
			//conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			//BufferedReader reader = new BufferedReader(new InputStreamReader(urlClan.openStream(), "UTF-8"));
			line = "";
			AllLines = "";

			while ((line = reader.readLine()) != null) {
				AllLines = AllLines + line;
			}
			
			reader.close();

			gson = new Gson();
			//System.out.println("before " + AllLines);
			
			//parsing gson
			TankToJson[] listTanksToJson = gson.fromJson(AllLines,TankToJson[].class );
			System.out.println(listTanksToJson);
			//la clé correspond à l'id du tank + id country : idTank_idCountry
			HashMap<String, TankToJson> hmTanksJson =  new HashMap<String, TankToJson>();
					
			for(TankToJson tk : listTanksToJson) {
				//System.out.println(tk.getTankid() + ":" + tk.getTitle());
				hmTanksJson.put(tk.getTankid()+"_"+tk.getCountryid(), tk);
			}
			//copie all data of TankToJson in DossierToJson
			//{"tankid": 0, "countryid": 3, "compDescr": 49, "active": 1, "type": 2, "type_name": "MT", "tier": 8, "premium": 1, "title": "Type 59", 
			//"icon": "ch01_type59", "icon_orig": "Ch01_Type59"},
			
			for (DossierToJsonTanks dossierToJsonTanks :dossierToJson.tanks ) {
				String keyTankJson =  dossierToJsonTanks.getId()+"_"+dossierToJsonTanks.getCountry(); 
				//
				TankToJson tk = hmTanksJson.get(keyTankJson);
				//copie all data from TankToJson in DossierToJson
				dossierToJsonTanks.setActive(tk.getActive());
				dossierToJsonTanks.setCompDescr(tk.getCompDescr());
				dossierToJsonTanks.setIcon_orig(tk.getIcon_orig());
				dossierToJsonTanks.setIcon(tk.getIcon());
				dossierToJsonTanks.setPremium(tk.getPremium());
				dossierToJsonTanks.setTier(tk.getTier());
				dossierToJsonTanks.setTitle(tk.getTitle());
				dossierToJsonTanks.setType(tk.getType());
				dossierToJsonTanks.setType_name(tk.getType_name());
				dossierToJsonTanks.setTitle(tk.getTitle());
			}
			
			
			for (DossierToJsonTanks dossierToJsonTanks :dossierToJson.tanks ) {
				System.out.println("id:" + dossierToJsonTanks.getId() + " title:" + dossierToJsonTanks.getTitle() + " Type_name:"+dossierToJsonTanks.getType_name()+ " Type:"+dossierToJsonTanks.getType());
			}
			
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
