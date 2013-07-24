package com.wot.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jdo.PersistenceManager;

import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.wot.client.WotService;
import com.wot.server.api.TransformDtoObject;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;
import com.wot.shared.CommunityClan;
import com.wot.shared.DataCommunityAccount;
import com.wot.shared.DataCommunityAccountAchievements;
import com.wot.shared.DataCommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.FieldVerifier;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WotServiceImpl extends RemoteServiceServlet implements WotService {
	String lieu = "boulot";
	
	@Override
	public Clan getClan(String input) throws IllegalArgumentException {
		Clan desClan =null;
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		String resultAchievement = "";
		
		try {
			long maxAchievement = 0;
			String maxAchievementuserName = "";

			//recup id clan avec son son nom 
			// http://api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=NOVA%20SNAIL&offset=0&limit=1
			/**
			 * {
				  "status": "ok", 
				  "status_code": "NO_ERROR", 
				  "data": {
				    "items": [
				      {
				        "abbreviation": "NVS", 
				        "created_at": 1328978449.00, 
				        "name": "NOVA SNAIL", 
				        "member_count": 57, 
				        "owner": "hentz44", 
				        "motto": "Un escargot qui avale un obus a confiance en sa coquille.", 
				        "clan_emblem_url": "http://cw.worldoftanks.eu/media/clans/emblems/clans_5/500006074/emblem_64x64.png", 
				        "id": 500006074, 
				        "clan_color": "#4a426c"
				      }
				    ], 
				    "offset": 0, 
				    "filtered_count": 2
				  }
				}
			 */
			URL urlClan = null ;
			input = input.replace(" ", "%20");
			if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=" +  input + "&offset=0&limit=1");				
			}
			else {
				urlClan = new URL("http://api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=" +  input + "&offset=0&limit=1");		
			}
			
			//lecture de la r�ponse recherche du clan
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlClan.openStream()));
			String line = "";
			String AllLines = "";

			while ((line = reader.readLine()) != null) {
				AllLines = AllLines + line;
			}
			reader.close();

			Gson gson = new Gson();
			//System.out.println("before " + AllLines);
			
			//parsing gson
			desClan = gson.fromJson(AllLines, Clan.class);
			//ItemsDataClan  myItemsDataClan = null ;
			PersistenceManager pm = PMF.get().getPersistenceManager();
	        try {
	        	//must transform before persist the objet clan
	        	DaoClan daoClan = TransformDtoObject.TransformClanToDaoClan(desClan);
	        	
	            pm.makePersistent(daoClan);
	        } finally {
	            pm.close();
	        }
	
		} catch (MalformedURLException e) {
			// ...
			e.printStackTrace();
		} catch (IOException e) {
			// ...
			e.printStackTrace();
		}

		//return resultAchievement;
		return desClan;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	@Override
	public String getMembersClan(String idClan) {

	Clan desClan =null;
	// Verify that the input is valid.
	if (!FieldVerifier.isValidName(idClan)) {
		// If the input is not valid, throw an IllegalArgumentException back to
		// the client.
		throw new IllegalArgumentException("Name must be at least 4 characters long");
	}

	String serverInfo = getServletContext().getServerInfo();
	String userAgent = getThreadLocalRequest().getHeader("User-Agent");

	// Escape data from the client to avoid cross-site script vulnerabilities.
	idClan = escapeHtml(idClan);
	userAgent = escapeHtml(userAgent);
	String resultAchievement = "";
	
	
	try {
		long maxAchievement = 0;
		String maxAchievementuserName = "";

	
		URL urlClan = null ;
		// recup des membres du clan NVS
		urlClan = null ;
		if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
			urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu%2Fcommunity%2Fclans%2F" + idClan + "%2Fapi%2F1.0%2F%3Fsource_token%3DWG-WoT_Assistant-1.3.2&hl=0000000001");				
		}
		else {
			//500006074
			urlClan = new URL("http://api.worldoftanks.eu/community/clans/" + idClan + "/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(urlClan.openStream()));
		String line = "";
		String AllLines = "";

		while ((line = reader.readLine()) != null) {
			AllLines = AllLines + line;
		}
		reader.close();

		Gson gson = new Gson();
		//System.out.println("before " + AllLines);
		
		int indexDes = AllLines.indexOf("\"description_html") ;
		AllLines = AllLines.substring(0,indexDes); 
		AllLines = AllLines + " \"description_html\":\"aa\"}}";
		//System.out.println("after " + AllLines);
		
		//AllLines = "{  \"status\": \"ok\",   \"status_code\": \"NO_ERROR\",   \"data\": {    \"leader_id\": 502116599, \"description\": \"* Age minimum : 16ans et +* Vocal, mais non obligatoire * PRESENCE REGULIERES sur le canal,aux compagnies et sur le forum ( 1 à 2 fois par semaines )* pas de language SMS  * Période d'essai de 3 semaines Toutes les candidatures se font via un formulaire de candidature sur notre forum ici. Si vous désirez plus d’informations sur les clans, prenez contact avec hentz44[NVS], il se fera un plaisir de vous répondre !. Au plaisir de vous revoir sur le champ de bataille !\",     \"color\": \"#4a426c\",    \"updated_at\": 1374095166.00,     \"abbreviation\": \"NVS\",    \"members\": [      {        \"account_id\": 503224066,         \"created_at\": 1362947902.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"recruiter\",         \"role\": \"recruiter\",         \"account_name\": \"voleurbleu\"      },       {        \"account_id\": 503777028,         \"created_at\": 1369331057.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"mwa2\"      },       {        \"account_id\": 506450710,         \"created_at\": 1359560935.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"treasurer\",         \"role\": \"treasurer\",         \"account_name\": \"regnald77\"      },       {        \"account_id\": 500423304,         \"created_at\": 1330525514.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"JAPPY\"      },       {        \"account_id\": 505539337,         \"created_at\": 1362770042.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"u71\"      },       {        \"account_id\": 502360074,         \"created_at\": 1354486401.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Lt_Staller\"      },       {        \"account_id\": 504390799,         \"created_at\": 1351765257.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Mercenaries14\"      },       {        \"account_id\": 502840082,         \"created_at\": 1352661268.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Tristelune\"      },       {        \"account_id\": 504713110,         \"created_at\": 1350678840.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"commander\",         \"role\": \"commander\",         \"account_name\": \"stadetoulousain\"      },       {        \"account_id\": 500561530,         \"created_at\": 1332775251.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Lemoinefou9060\"      },       {        \"account_id\": 505609886,         \"created_at\": 1358527959.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"zvolaidge\"      },       {        \"account_id\": 506772357,         \"created_at\": 1366938640.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"cinespace\"      },       {        \"account_id\": 505552680,         \"created_at\": 1353513272.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"huggy47\"      },       {        \"account_id\": 502055721,         \"created_at\": 1330555977.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Damalakian\"      },       {        \"account_id\": 502867883,         \"created_at\": 1369163487.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"alekdur\"      },       {        \"account_id\": 506763437,         \"created_at\": 1365855037.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"gexman47\"      },       {        \"account_id\": 506486576,         \"created_at\": 1367338177.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"strong44\"      },       {        \"account_id\": 505899592,         \"created_at\": 1374095165.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"recruit\",         \"role\": \"recruit\",         \"account_name\": \"xstorm84\"      },       {        \"account_id\": 505262665,         \"created_at\": 1353971319.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Ghost_days\"      },       {        \"account_id\": 501611576,         \"created_at\": 1335467584.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"diplomat\",         \"role\": \"diplomat\",         \"account_name\": \"DeadMagnum\"      },       {        \"account_id\": 501230780,         \"created_at\": 1333796203.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Arnbjorn\"      },       {        \"account_id\": 502236863,         \"created_at\": 1349469439.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"fabgend\"      },       {        \"account_id\": 503203137,         \"created_at\": 1346253118.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"vice_leader\",         \"role\": \"vice_leader\",         \"account_name\": \"willy34blv\"      },       {        \"account_id\": 502202306,         \"created_at\": 1352228188.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"benyamamoto\"      },       {        \"account_id\": 507519427,         \"created_at\": 1366304476.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Kamarades\"      },       {        \"account_id\": 505364421,         \"created_at\": 1354375664.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Nozerti\"      },       {        \"account_id\": 503588936,         \"created_at\": 1367845028.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"RANUBIS1\"      },       {        \"account_id\": 506269385,         \"created_at\": 1357988893.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"ArMenManutor\"      },       {        \"account_id\": 502116599,         \"created_at\": 1330211964.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"leader\",         \"role\": \"leader\",         \"account_name\": \"hentz44\"      },       {        \"account_id\": 506395724,         \"created_at\": 1367411139.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Roparzh71\"      },       {        \"account_id\": 506261582,         \"created_at\": 1358194527.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Kaiwo\"      },       {        \"account_id\": 504014927,         \"created_at\": 1353267426.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"recruiter\",         \"role\": \"recruiter\",         \"account_name\": \"FitzArthur\"      },       {        \"account_id\": 503365816,         \"created_at\": 1350679284.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Doann\"      },       {        \"account_id\": 504197330,         \"created_at\": 1371053879.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"titethan\"      },       {        \"account_id\": 502419795,         \"created_at\": 1367517017.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"jms57\"      },       {        \"account_id\": 500278612,         \"created_at\": 1335610575.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"vice_leader\",         \"role\": \"vice_leader\",         \"account_name\": \"GladXX\"      },       {        \"account_id\": 502248407,         \"created_at\": 1358421020.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"AltairV\"      },       {        \"account_id\": 501024472,         \"created_at\": 1346952434.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"commander\",         \"role\": \"commander\",         \"account_name\": \"Defoliant\"      },       {        \"account_id\": 503467228,         \"created_at\": 1364694644.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"firefreez\"      },       {        \"account_id\": 505130205,         \"created_at\": 1371724884.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"recruit\",         \"role\": \"recruit\",         \"account_name\": \"Choupy_\"      },       {        \"account_id\": 500543966,         \"created_at\": 1366876355.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"clem300\"      },       {        \"account_id\": 501298149,         \"created_at\": 1329235791.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"samuel52\"      },       {        \"account_id\": 503573601,         \"created_at\": 1362072465.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"valains\"      },       {        \"account_id\": 504974693,         \"created_at\": 1359906898.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Zask\"      },       {        \"account_id\": 506128060,         \"created_at\": 1358264970.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"reg84\"      },       {        \"account_id\": 503196140,         \"created_at\": 1357252448.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"yeplefou\"      },       {        \"account_id\": 502018286,         \"created_at\": 1336223273.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Florever\"      },       {        \"account_id\": 504850159,         \"created_at\": 1353000717.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Lenoxian13\"      },       {        \"account_id\": 502674600,         \"created_at\": 1356872663.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"chawax\"      },       {        \"account_id\": 504723314,         \"created_at\": 1366221984.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Wilfried76\"      },       {        \"account_id\": 501416948,         \"created_at\": 1372412989.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"recruit\",         \"role\": \"recruit\",         \"account_name\": \"eltrobadore\"      },       {        \"account_id\": 506138870,         \"created_at\": 1368733254.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Le_loup_blanc\"      },       {        \"account_id\": 501193784,         \"created_at\": 1334578073.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"riconine\"      },       {        \"account_id\": 500378232,         \"created_at\": 1357325363.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Machmalo\"      },       {        \"account_id\": 508809978,         \"created_at\": 1371039888.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Jeu_Titille\"      },       {        \"account_id\": 506128381,         \"created_at\": 1362764473.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"private\",         \"role\": \"private\",         \"account_name\": \"Aonbar\"      },       {        \"account_id\": 500435070,         \"created_at\": 1373292927.00,         \"updated_at\": 1374095166.00,         \"role_localised\": \"recruit\",         \"role\": \"recruit\",         \"account_name\": \"braeburn\"      }    ], \"motto\": \"Un escargot qui avale un obus a confiance en sa coquille.\", \"members_count\": 57, \"name\": \"NOVA SNAIL\", \"description_html\": \"aa\", \"created_at\": 1328978449.00  }}";
		
		CommunityClan clan = gson.fromJson(AllLines, CommunityClan.class);
		if (clan != null) {

			DataCommunityClan myDataCommunityClan = clan.getData();

			List<DataCommunityClanMembers> listMembers = myDataCommunityClan.getMembers();

			for (DataCommunityClanMembers dataMember : listMembers) {

				//
				// String nameUser ="";
				String idUser = dataMember.getAccount_id();

				// recup des datas du USER
				// http://api.worldoftanks.eu/community/accounts/506486576/api/1.0/?source_token=WG-WoT_Assistant-1.3.2
				// URL url = new URL("http://api.worldoftanks.eu/uc/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
				URL url = null ;
				if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
					url = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/community/accounts/" + idUser + "/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");
				}
				else {
					url = new URL("http://api.worldoftanks.eu/community/accounts/" + idUser + "/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");
				}
				BufferedReader readerUser = new BufferedReader(new InputStreamReader(url.openStream()));
				String lineUser = "";
				;
				String AllLinesUser = "";

				while ((lineUser = readerUser.readLine()) != null) {
					AllLinesUser = AllLinesUser + lineUser;
				}
				//System.out.println(AllLinesUser);
				
				readerUser.close();

				Gson gsonUser = new Gson();
				CommunityAccount account = gsonUser.fromJson(AllLinesUser, CommunityAccount.class);

				Map<String, Integer> mapAchievement = new HashMap<String, Integer>();

				if (account != null) {
					DataCommunityAccount myDataCommunityAccount = account.getData();

					DataCommunityAccountAchievements myDataCommunityAccountAchievements = myDataCommunityAccount.getAchievements();
					mapAchievement.put("Beasthunter", myDataCommunityAccountAchievements.getBeasthunter());
					mapAchievement.put("Defender", myDataCommunityAccountAchievements.getDefender());
					mapAchievement.put("Diehard", myDataCommunityAccountAchievements.getDiehard());
					mapAchievement.put("Invader", myDataCommunityAccountAchievements.getInvader());
					mapAchievement.put("Lumberjack", myDataCommunityAccountAchievements.getLumberjack());
					mapAchievement.put("MedalAbrams", myDataCommunityAccountAchievements.getMedalAbrams());
					mapAchievement.put("MedalBillotte", myDataCommunityAccountAchievements.getMedalBillotte());
					mapAchievement.put("MedalBurda", myDataCommunityAccountAchievements.getMedalBurda());
					mapAchievement.put("MedalCarius", myDataCommunityAccountAchievements.getMedalCarius());
					mapAchievement.put("MedalEkins", myDataCommunityAccountAchievements.getMedalEkins());
					mapAchievement.put("MedalFadin", myDataCommunityAccountAchievements.getMedalFadin());
					mapAchievement.put("MedalHalonen", myDataCommunityAccountAchievements.getMedalHalonen());
					mapAchievement.put("MedalKay", myDataCommunityAccountAchievements.getMedalKay());
					mapAchievement.put("MedalKnispel", myDataCommunityAccountAchievements.getMedalKnispel());
					mapAchievement.put("MedalKolobanov", myDataCommunityAccountAchievements.getMedalKolobanov());
					mapAchievement.put("MedalLavrinenko", myDataCommunityAccountAchievements.getMedalLavrinenko());
					mapAchievement.put("MedalLeClerc", myDataCommunityAccountAchievements.getMedalLeClerc());
					mapAchievement.put("MedalOrlik", myDataCommunityAccountAchievements.getMedalOrlik());
					mapAchievement.put("MedalOskin", myDataCommunityAccountAchievements.getMedalOskin());
					mapAchievement.put("MedalPoppel", myDataCommunityAccountAchievements.getMedalPoppel());
					mapAchievement.put("MedalWittmann", myDataCommunityAccountAchievements.getMedalWittmann());
					mapAchievement.put("Mousebane", myDataCommunityAccountAchievements.getMousebane());
					mapAchievement.put("Raider", myDataCommunityAccountAchievements.getRaider());
					mapAchievement.put("Scout", myDataCommunityAccountAchievements.getScout());
					mapAchievement.put("Sniper", myDataCommunityAccountAchievements.getSniper());
					mapAchievement.put("Supporter", myDataCommunityAccountAchievements.getSupporter());
					mapAchievement.put("TankExpert", myDataCommunityAccountAchievements.getTankExpert());
					mapAchievement.put("TitleSniper", myDataCommunityAccountAchievements.getTitleSniper());
					mapAchievement.put("Warrior", myDataCommunityAccountAchievements.getWarrior());

					Set<Entry<String, Integer>> setEntryAchievement = mapAchievement.entrySet();
					long nbAch = 0;
					for (Entry<String, Integer> entry : setEntryAchievement) {
						// System.out.println(myDataCommunityAccount.getName()+ ";" +entry.getKey() + ";" + entry.getValue()+";"+nbAch);
						nbAch = nbAch + entry.getValue();

					}
					long battles = 0;

					if (myDataCommunityAccount.getStats() != null) {
						battles = myDataCommunityAccount.getStats().getBattles();
					}
					float moy = battles / nbAch;

					// le resultat envoy� au client
					//resultAchievement = resultAchievement + "\\n\\r" + myDataCommunityAccount.getName() + ";" + battles + "\\n\r";
					resultAchievement = resultAchievement + myDataCommunityAccount.getName() + ";" + battles + " ";

					System.out.println(myDataCommunityAccount.getName() + ";" + moy);

					if (nbAch > maxAchievement) {
						maxAchievement = nbAch;
						maxAchievementuserName = myDataCommunityAccount.getName();
					}
					// System.out.println("--");

				} else {

					System.out.println("Erreur de parse");
				}

			}

			System.out.println("maxAchievementuserName   " + maxAchievementuserName + " maxAchievement " + maxAchievement);

		} else {

			System.out.println("Erreur de parse");
		}
	} catch (MalformedURLException e) {
		// ...
		e.printStackTrace();
	} catch (IOException e) {
		// ...
		e.printStackTrace();
	}

	return resultAchievement;
	
	}

	@Override
	public Clan getClans(String input, int offset) throws IllegalArgumentException {
		Clan clan = null;
		//int offset = 0 ;
		int limit = 100;
		
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Name must be at least 1 characters long");
		}
	
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
	
		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);
		String resultAchievement = "";
		
		try {
			long maxAchievement = 0;
			String maxAchievementuserName = "";
	
			//recup id clan avec son son nom 
			// http://api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=NOVA%20SNAIL&offset=0&limit=1
			/**
			 * {
				  "status": "ok", 
				  "status_code": "NO_ERROR", 
				  "data": {
				    "items": [
				      {
				        "abbreviation": "NVS", 
				        "created_at": 1328978449.00, 
				        "name": "NOVA SNAIL", 
				        "member_count": 57, 
				        "owner": "hentz44", 
				        "motto": "Un escargot qui avale un obus a confiance en sa coquille.", 
				        "clan_emblem_url": "http://cw.worldoftanks.eu/media/clans/emblems/clans_5/500006074/emblem_64x64.png", 
				        "id": 500006074, 
				        "clan_color": "#4a426c"
				      }
				    ], 
				    "offset": 0, 
				    "filtered_count": 2
				  }
				}
			 */
			URL urlClan = null ;
			input = input.replace(" ", "%20");
			if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				urlClan = new URL("http://redblouse.info/index.php?q=http://api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=" +  input + "&offset="+ offset+ "&limit=" + limit);					
			}
			else {
				urlClan = new URL("http://api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=" +  input + "&offset="+ offset+ "&limit=" + limit);		
			}
			
			//lecture de la r�ponse recherche du clan
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlClan.openStream()));
			String line = "";
			String AllLines = "";
	
			while ((line = reader.readLine()) != null) {
				AllLines = AllLines + line;
			}
			reader.close();
	
			Gson gson = new Gson();
			//System.out.println("before " + AllLines);
			
			//parsing gson
			clan = gson.fromJson(AllLines, Clan.class );
			//ItemsDataClan  myItemsDataClan = null ;
			
	
		} catch (MalformedURLException e) {
			// ...
			e.printStackTrace();
		} catch (IOException e) {
			// ...
			e.printStackTrace();
		}
	
		//return resultAchievement;
		return clan;
	}

}
