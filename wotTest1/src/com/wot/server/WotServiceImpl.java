package com.wot.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.wot.client.WotService;
import com.wot.server.api.TransformDtoObject;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;
import com.wot.shared.CommunityClan;
import com.wot.shared.DataCommunityAccountRatings;
import com.wot.shared.DataCommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.DataCommunityMembers;
import com.wot.shared.DataPlayerTankRatings;
import com.wot.shared.DataTankEncyclopedia;
import com.wot.shared.FieldVerifier;
import com.wot.shared.ObjectFactory;
import com.wot.shared.PlayerRatings;
import com.wot.shared.PlayerTankRatings;
import com.wot.shared.TankEncyclopedia;
import com.wot.shared.XmlDescription;
import com.wot.shared.XmlListAchievement;
import com.wot.shared.XmlListCategoryAchievement;
import com.wot.shared.XmlListSrcImg;
import com.wot.shared.XmlSrc;
import com.wot.shared.XmlWiki;

/**
 * The server side implementation of the RPC service.
 * https://github.com/thunder-spb/wot-api-description/blob/master/README.md
 */
@SuppressWarnings("serial")
public class WotServiceImpl extends RemoteServiceServlet implements WotService {
	String lieu = "maison"; //boulot ou maison si boulot -> pedro proxy 
	boolean saveData = true;
	private boolean saveDataPlayer = true;
	XmlWiki wiki =  null;
	
	private String applicationIdEU = "d0a293dc77667c9328783d489c8cef73";
	private String urlServerEU =  "http://api.worldoftanks.eu";
	//
	static int indexBeginUser = 0;
	static int indexEndUser = 10;
	static int pas = 10;
	static int indexMaxUser = 100;
	static List<String> listUsersPersisted = new ArrayList<String>();
	
	private static final Logger log = Logger.getLogger(WotServiceImpl.class.getName());
	
	static TankEncyclopedia tankEncyclopedia;
	
	@Override
	protected void checkPermutationStrongName() throws SecurityException {
		return;
	}
	
	@Override
	public List<String> persistStats(String input, int indexBegin, int indexEnd, List<String> listIdUser) throws IllegalArgumentException {
		//
		log.warning("========lancement persistStats ============== " + input);
		
		//recup des stats des user du clan NVS -- pour l'instant le seul clan
		// a terme il faut rÃ©cupÃ©rer les clan prÃ©sents en base, et requeter les stats de leur joueurs 

		
		//getAllStats("500006074", indexBeginUser, indexEndUser); // 0 10 , 10 20 ,30 40 , 60 70, .. 90 100 
		
//		if (indexEndUser > indexMaxUser) {
//			indexBeginUser = 0; 
//			indexEndUser = pas;
//		}else {
//			indexBeginUser = indexBeginUser +  pas ;
//			indexEndUser = indexEndUser +  pas;
//		}
		
//		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
//		Queue queue = QueueFactory.getDefaultQueue();
//		try {
//		    Transaction txn = ds.beginTransaction();
//		    
//		    // http://wotachievement.appspot.com/wottest1/greet
//		    // 7|0|7|http://wotachievement.appspot.com/wottest1/|E03CD0D1B0EF18B0BD735F9C9BA22A2E|com.wot.client.WotService|getClans|java.lang.String/2004016611|I|NOVA SNAIL|1|2|3|4|2|5|6|7|0|
//		    //Content-Type	text/x-gwt-rpc; charset=utf-8
//		    HashMap hm= new HashMap<String, String>();
//		    hm.put("Content-Type", "text/x-gwt-rpc");
//		    
//		    TaskOptions to = TaskOptions.Builder.withUrl("/wottest1/greet").headers(hm);
//		    to.method(Method.POST);
//		    //to.payload("7|0|7|http://wotachievement.appspot.com/wottest1/|E03CD0D1B0EF18B0BD735F9C9BA22A2E|com.wot.client.WotService|getClan|java.lang.String/2004016611|I|NOVA SNAIL|1|2|3|4|2|5|6|7|0|");
//		    //http://wotachievement.appspot.com
//		    if (lieu.equalsIgnoreCase("boulot")) {
//		    	//local
//		    	to.payload("7|0|6|http://127.0.0.1:8888/wottest1/|E03CD0D1B0EF18B0BD735F9C9BA22A2E|com.wot.client.WotService|getClan|java.lang.String/2004016611|NOVA SNAIL|1|2|3|4|1|5|6|");	
//		    }else {
//		    	to.payload("7|0|6|http://http://wotachievement.appspot.com/wottest1/|E03CD0D1B0EF18B0BD735F9C9BA22A2E|com.wot.client.WotService|getClan|java.lang.String/2004016611|NOVA SNAIL|1|2|3|4|1|5|6|");
//		    }
//		    
//		    queue.add(to);
//
//		    // ...
//		    txn.commit();
//		} catch (DatastoreFailureException e) {
//			log.severe(e.getMessage());
//		}
		
		
		
		
		persistAllStats3( new Date(), listIdUser);
		//return resultAchievement;
		return listUsersPersisted;
	}

	@Override
		public String persistStatsForCron(String idClan) throws IllegalArgumentException {
			//
			log.warning("========lancement persistStatsForCron  ============== " + idClan);
			
			//recup des stats des user du clan NVS -- pour l'instant le seul clan
			// a terme il faut rÃ©cupÃ©rer les clan prÃ©sents en base, et requeter les stats de leur joueurs 
	
			
			//getAllStats("500006074", indexBeginUser, indexEndUser); // 0 10 , 10 20 ,30 40 , 60 70, .. 90 100 
			
	//		if (indexEndUser > indexMaxUser) {
	//			indexBeginUser = 0; 
	//			indexEndUser = pas;
	//		}else {
	//			indexBeginUser = indexBeginUser +  pas ;
	//			indexEndUser = indexEndUser +  pas;
	//		}
			
	//		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	//		Queue queue = QueueFactory.getDefaultQueue();
	//		try {
	//		    Transaction txn = ds.beginTransaction();
	//		    
	//		    // http://wotachievement.appspot.com/wottest1/greet
	//		    // 7|0|7|http://wotachievement.appspot.com/wottest1/|E03CD0D1B0EF18B0BD735F9C9BA22A2E|com.wot.client.WotService|getClans|java.lang.String/2004016611|I|NOVA SNAIL|1|2|3|4|2|5|6|7|0|
	//		    //Content-Type	text/x-gwt-rpc; charset=utf-8
	//		    HashMap hm= new HashMap<String, String>();
	//		    hm.put("Content-Type", "text/x-gwt-rpc");
	//		    
	//		    TaskOptions to = TaskOptions.Builder.withUrl("/wottest1/greet").headers(hm);
	//		    to.method(Method.POST);
	//		    //to.payload("7|0|7|http://wotachievement.appspot.com/wottest1/|E03CD0D1B0EF18B0BD735F9C9BA22A2E|com.wot.client.WotService|getClan|java.lang.String/2004016611|I|NOVA SNAIL|1|2|3|4|2|5|6|7|0|");
	//		    //http://wotachievement.appspot.com
	//		    if (lieu.equalsIgnoreCase("boulot")) {
	//		    	//local
	//		    	to.payload("7|0|6|http://127.0.0.1:8888/wottest1/|E03CD0D1B0EF18B0BD735F9C9BA22A2E|com.wot.client.WotService|getClan|java.lang.String/2004016611|NOVA SNAIL|1|2|3|4|1|5|6|");	
	//		    }else {
	//		    	to.payload("7|0|6|http://http://wotachievement.appspot.com/wottest1/|E03CD0D1B0EF18B0BD735F9C9BA22A2E|com.wot.client.WotService|getClan|java.lang.String/2004016611|NOVA SNAIL|1|2|3|4|1|5|6|");
	//		    }
	//		    
	//		    queue.add(to);
	//
	//		    // ...
	//		    txn.commit();
	//		} catch (DatastoreFailureException e) {
	//			log.severe(e.getMessage());
	//		}
			
			
			
			
			//persistAllStatsCron( new Date(), idClan);
			//return resultAchievement;
			return idClan;
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
	public Clan getClans(String input, int offset) throws IllegalArgumentException {
		//////////
		log.warning("========lancement getClans ==============");
		
		//getClan("");
		
		////////////////
//		URL urlAchievement = null;
//		String AllLinesWot = "";
//		try {
//			if(lieu.equalsIgnoreCase("boulot")) //on passe par 1 proxy
//				urlAchievement = new URL ("https://pedro-proxy.appspot.com/wiki.worldoftanks.com/achievements");
//			else
//				urlAchievement = new URL ("http://wiki.worldoftanks.com/achievements");
//			
//			HttpURLConnection conn = (HttpURLConnection)urlAchievement.openConnection();
//			conn.setReadTimeout(60000);
//			conn.setConnectTimeout(60000);
//			conn.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			
//			//BufferedReader reader = new BufferedReader(new InputStreamReader(urlAchievement.openStream()));
//			String line = "";
//			
//
//			while ((line = reader.readLine()) != null) {
//				AllLinesWot = AllLinesWot + line;
//			}
//			reader.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		log.info(AllLinesWot);
		
		ObjectFactory objFactory = null;
		try {
			JAXBContext context = JAXBContext.newInstance(XmlWiki.class);
			
//			//crÃ©ation WIKI
			if (wiki== null) {
				objFactory = new ObjectFactory();
			
				wiki = objFactory.createXmlWiki();

				//A partir du XML instanciÃ© les classes !!
				Unmarshaller unmarshaller = context.createUnmarshaller();
				wiki = (XmlWiki) unmarshaller.unmarshal(new File("wotWiki.xml"));
				log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>unmarshaller wotWiki.xml");
				log.info(wiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT().get(0).getNAME());
			}
			
		} catch (JAXBException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//////////////////////////////////////////////////////////////////
		Clan clan = null;
		//int offset = 0 ;
		int limit = 100;
		
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			log.info("arg not good : " + input);
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
				
				
				////////////////// NEW ///////////////////// 
				 * {
				  "status": "ok", 
				  "count": 48, 
				  "data": [
				    {
				      "members_count": 48, 
				      "name": "\"Ð‘ÐµÐ»Ð°Ñ� Ð³Ð²Ð°Ñ€Ð´Ð¸Ñ�\"", 
				      "created_at": 1335298122, 
				      "abbreviation": "WG1", 
				      "clan_id": 24810, 
				      "owner_id": 5095539
				    }, 
				    {
				      "members_count": 1, 
				      "name": "Cheerful coffin", 
				      "created_at": 1345383267, 
				      "abbreviation": "WGWWR", 
				      "clan_id": 35231, 
				      "owner_id": 5266248
				    }
				    ]
					}
				 * 
			 */
			URL urlClan = null ;
			input = input.replace(" ", "%20");
			if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/2.0/clan/list/?application_id=d0a293dc77667c9328783d489c8cef73&search=" +  input + "&offset="+ offset+ "&limit=" + limit);					
			}
			else {
				//NVS : 500006074
				//urlClan = new URL("http://api.worldoftanks.eu/community/clans/500006074/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");
				//http://api.worldoftanks.eu/2.0/clan/list/?application_id=d0a293dc77667c9328783d489c8cef73&search=
				urlClan = new URL("http://api.worldoftanks.eu/2.0/clan/list/?application_id=d0a293dc77667c9328783d489c8cef73&search=" +  input );		
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
			log.info(AllLines);
			reader.close();
	
			Gson gson = new Gson();
			//System.out.println("before " + AllLines);
			
			//parsing gson
			clan = gson.fromJson(AllLines, Clan.class );
			//ItemsDataClan  myItemsDataClan = null ;
			
			//translate the motto
			//https://www.googleapis.com/language/translate/v2/detect?{parameters}
			URL urlTranslate = null ;
			String key = "AIzaSyBVwNDYZ1d3ERToxoZSFhW7jQrOeEvJVMM";
			
			// Set your Windows Azure Marketplace client info - See http://msdn.microsoft.com/en-us/library/hh454950.aspx
		    
		    String translatedText = "";
		    //String idClient = "a41ecfea-8da4-41a9-8f66-be116476de4b";
		    //String secretClient = "LgIs7oBcmBUSZ0964xZxdqkZDMWSSSzvGHUZf1sUEMs";
		    
		    String idClient = "wotachievement";
		    String secretClient = "/upbsAfsZzh82dNC1ehpW8u8CVNR9afujtIko9ZW22E=";
		    
//		    Translate.setClientId(idClient/* Enter your Windows Azure Client Id here */);
//		    Translate.setClientSecret(secretClient/* Enter your Windows Azure Client Secret here */);
//
//		    Detect.setClientId(idClient);
//	        Detect.setClientSecret(secretClient);
	        
			int nbTrad = 0;
//			for (ItemsDataClan myItemsDataClan : clan.getItems()) {
//				translatedText = "Pas de traduction, Seules les 5 premieres lignes sont traduites";
//				String motto = myItemsDataClan.getMotto();
//				//detect lang motto
//				try {
//					Language detectedLanguage = null;
//					
//						detectedLanguage = Detect.execute(motto);
//						
//						if (detectedLanguage != null && !detectedLanguage.getName(Language.FRENCH).equalsIgnoreCase("FranÃ§ais") && nbTrad < 6  ) {
//							translatedText = Translate.execute(motto, detectedLanguage, Language.FRENCH);
//							myItemsDataClan.setMotto(motto + " (" + detectedLanguage.name() +") " + "--> traduction : " + translatedText);
//							nbTrad++;
//						}
//					
//				} catch (Exception e) {
//				// TODO Auto-generated catch block
//				System.out.println(e.getMessage());
//				}
//			}
	
		} catch (MalformedURLException e) {
			// ...
			e.printStackTrace();
		} catch (IOException e) {
			// ...
			e.printStackTrace();
		}
	
		//return resultAchievement;
		clan.setWiki(wiki);
		return clan;
	}
	
	/**
	 * getAllMembersClan : REcupÃ©re tous les menbres du clan
	 */
	@Override
	public CommunityClan getAllMembersClan(String idClan) {
		CommunityClan communityClan = null;
		
		DaoCommunityClan2 daoCommunityClan = null;
		Clan desClan =null;
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(idClan)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}
	
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
	
		// Escape data from the client to avoid cross-site script vulnerabilities.
		idClan = escapeHtml(idClan);
		userAgent = escapeHtml(userAgent);
		
		List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
		AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
		myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
		/**
		 * {
		  "status": "ok", 
		  "count": 1, 
		  "data": {
		    "1": {
		      "members_count": 100, 
		      "description": "Ð—Ð°ÐºÑ€Ñ‹Ñ‚Ñ‹Ð¹ ÐºÐ»Ð°Ð½, Ð² Ñ�Ð¾Ñ�Ñ‚Ð°Ð² ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾ Ð²Ñ…Ð¾Ð´Ñ�Ñ‚ Ð»Ð¸ÑˆÑŒ Ñ€Ð°Ð·Ñ€Ð°Ð±Ð¾Ñ‚Ñ‡Ð¸ÐºÐ¸ Ð¸Ð³Ñ€Ñ‹ &quot;World of Tanks&quot;.\n\nÐ—Ð°Ñ�Ð²ÐºÐ¸, Ð¿Ð¾Ñ�Ð»Ð°Ð½Ð½Ñ‹Ðµ ÐºÐ¾Ð¼Ð°Ð½Ð´Ð¸Ñ€Ñƒ ÐºÐ»Ð°Ð½Ð° Ñ‡ÐµÑ€ÐµÐ· Ñ„Ð¾Ñ€ÑƒÐ¼, Ð�Ð• Ð Ð�Ð¡Ð¡ÐœÐ�Ð¢Ð Ð˜Ð’Ð�Ð®Ð¢Ð¡Ð¯ .", 
		      "description_html": "<p>Ð—Ð°ÐºÑ€Ñ‹Ñ‚Ñ‹Ð¹ ÐºÐ»Ð°Ð½, Ð² Ñ�Ð¾Ñ�Ñ‚Ð°Ð² ÐºÐ¾Ñ‚Ð¾Ñ€Ð¾Ð³Ð¾ Ð²Ñ…Ð¾Ð´Ñ�Ñ‚ <i>Ð»Ð¸ÑˆÑŒ</i> Ñ€Ð°Ð·Ñ€Ð°Ð±Ð¾Ñ‚Ñ‡Ð¸ÐºÐ¸ Ð¸Ð³Ñ€Ñ‹ &quot;World of Tanks&quot;.\n</p><p>\n<br/>Ð—Ð°Ñ�Ð²ÐºÐ¸, Ð¿Ð¾Ñ�Ð»Ð°Ð½Ð½Ñ‹Ðµ ÐºÐ¾Ð¼Ð°Ð½Ð´Ð¸Ñ€Ñƒ ÐºÐ»Ð°Ð½Ð° Ñ‡ÐµÑ€ÐµÐ· Ñ„Ð¾Ñ€ÑƒÐ¼, <i>Ð�Ð• Ð Ð�Ð¡Ð¡ÐœÐ�Ð¢Ð Ð˜Ð’Ð�Ð®Ð¢Ð¡Ð¯</i> .\n</p>", 
		      "created_at": 1293024672, 
		      "updated_at": 1375930001, 
		      "name": "Wargaming.net", 
		      "abbreviation": "WG", 
		      "emblems": {
		        "large": "http://cw.worldoftanks.ru/media/clans/emblems/clans_1/1/emblem_64x64.png", 
		        "small": "http://cw.worldoftanks.ru/media/clans/emblems/clans_1/1/emblem_24x24.png", 
		        "medium": "http://cw.worldoftanks.ru/media/clans/emblems/clans_1/1/emblem_32x32.png", 
		        "bw_tank": "http://cw.worldoftanks.ru/media/clans/emblems/clans_1/1/emblem_64x64_tank.png"
		      }, 
		      "clan_id": 1, 
		      "members": {
		        "196632": {
		          "created_at": 1293126248, 
		          "role": "private", 
		          "updated_at": 1375930001, 
		          "account_id": 196632, 
		          "account_name": "Wrobel"
		        }, 
		        "18458": {
		          "created_at": 1360836543, 
		          "role": "diplomat", 
		          "updated_at": 1375930001, 
		          "account_id": 18458, 
		          "account_name": "alienraven"
		        }, 
			      "motto": "ÐžÑ€Ð»Ñ‹! ÐžÑ€Ð»Ð¸Ñ†Ñ‹!", 
			      "clan_color": "#e18000", 
			      "owner_id": 1277137
			    }
			  }
			}
		 */
		try {
		
			URL urlClan = null ;
			// recup des membres du clan NVS
			urlClan = null ;
			if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id=" + idClan );				
			}
			else {
				//500006074
				//http://api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id=500006074
				//urlClan = new URL("http://api.worldoftanks.eu/community/clans/" + idClan + "/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");
				urlClan = new URL("http://api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id=" + idClan );
			}
	
			HttpURLConnection conn = (HttpURLConnection)urlClan.openConnection();
			conn.setReadTimeout(60000);
			conn.setConnectTimeout(60000);
			conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			//BufferedReader reader = new BufferedReader(new InputStreamReader(urlClan.openStream()));
			String line = "";
			String AllLines = "";
	
			while ((line = reader.readLine()) != null) {
				AllLines = AllLines + line;
			}
			reader.close();
	
			Gson gson = new Gson();
			
			daoCommunityClan = gson.fromJson(AllLines, DaoCommunityClan2.class);
			daoCommunityClan.setIdClan(idClan);
			daoCommunityClan.setDateCommunityClan(new java.util.Date());
			//persist clan ?
			
		} catch (MalformedURLException e) {
			// ...
			e.printStackTrace();
		} catch (IOException e) {
			// ...
			e.printStackTrace();
		}
		 ///
		communityClan = TransformDtoObject.TransformCommunityDaoCommunityClanToCommunityClan(daoCommunityClan);
		return communityClan;
	}

	//@Override
//	public AllCommunityAccount getAllMembersClanAndStats2(String idClan,  List<String> listIdUser) {
//	
//		//getStatsUsers(listIdUser);
//		
//		// Verify that the input is valid. //[502248407, 506128381]  
//		if (!FieldVerifier.isValidName(idClan)) {
//			// If the input is not valid, throw an IllegalArgumentException back to
//			// the client.
//			throw new IllegalArgumentException("Name must be at least 4 characters long");
//		}
//	
//		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
//	
//		// Escape data from the client to avoid cross-site script vulnerabilities.
//		idClan = escapeHtml(idClan);
//		userAgent = escapeHtml(userAgent);
//		
//		List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
//		AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
//		myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
//		PersistenceManager pm =null;
//		
//		try {
//			pm = PMF.get().getPersistenceManager();
//			
//			URL urlClan = null ;
//			// recup des membres du clan NVS
//			urlClan = null ;
//			if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
//				//        new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id=" + idClan );
//				urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);				
//			}
//			else {
//				//500006074
//				//http://api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id=500006074
//				//urlClan = new URL("http://api.worldoftanks.eu/community/clans/" + idClan + "/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");
//				urlClan = new URL("http://api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);
//			}
//	
//			HttpURLConnection conn = (HttpURLConnection)urlClan.openConnection();
//			conn.setReadTimeout(60000);
//			conn.setConnectTimeout(60000);
//			conn.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//			//BufferedReader reader = new BufferedReader(new InputStreamReader(urlClan.openStream()));
//			String line = "";
//			String AllLines = "";
//	
//			while ((line = reader.readLine()) != null) {
//				AllLines = AllLines + line;
//			}
//			reader.close();
//	
//			Gson gson = new Gson();
//			
//			DaoCommunityClan2 daoCommunityClan = gson.fromJson(AllLines, DaoCommunityClan2.class);
//			daoCommunityClan.setIdClan(idClan);
//			daoCommunityClan.setDateCommunityClan(new java.util.Date());
//
//			CommunityClan communityClan = TransformDtoObject.TransformCommunityDaoCommunityClanToCommunityClan(daoCommunityClan);
//			if (communityClan != null) {
//	
//				DataCommunityClan myDataCommunityClan = communityClan.getData();
//	
//				List<DataCommunityClanMembers> listClanMembers = myDataCommunityClan.getMembers();
//	
//				for (DataCommunityClanMembers dataClanMember : listClanMembers) {
//
//					for (DataCommunityMembers member : dataClanMember.getMembers()) { 
//						//
//						// String nameUser ="";
//						String idUser = member.getAccount_id();
//
//						//si idUSer in listIdUser alaors on requï¿½te sinon rien
//						boolean treatUser = false;
//						if (listIdUser != null && listIdUser.size() != 0) {
//							
//							for(String idList :listIdUser) {
//								if (idList.equalsIgnoreCase(idUser)) {
//									treatUser = true;
//									break;
//								}
//							}
//						}else {
//							treatUser = true;
//						}
//						
//						if (treatUser) {
//							// recup des datas du USER 506486576 (strong44)
//							// http://api.worldoftanks.eu/community/accounts/506486576/api/1.0/?source_token=WG-WoT_Assistant-1.3.2
//							// URL url = new URL("http://api.worldoftanks.eu/uc/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
//							URL url = null ;
//							
//							String urlServer = urlServerEU +"/2.0/account/ratings/?application_id=" + applicationIdEU + "&account_id=";
//							//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
//							
//							if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
//								url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + idUser);
//							}
//							else {
//								url = new URL(urlServer + idUser);
//							}
//							
//							HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
//							conn2.setReadTimeout(60000);
//							conn2.setConnectTimeout(60000);
//							conn2.getInputStream();
//							BufferedReader readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
//
//							//BufferedReader readerUser = new BufferedReader(new InputStreamReader(url.openStream()));
//							String lineUser = "";
//							;
//							String AllLinesUser = "";
//			
//							while ((lineUser = readerUser.readLine()) != null) {
//								AllLinesUser = AllLinesUser + lineUser;
//							}
//							//System.out.println(AllLinesUser);
//							
//							readerUser.close();
//			
//							Gson gsonUser = new Gson();
//							log.info(AllLinesUser.substring(0, 200));
//							PlayerRatings playerRatings = gsonUser.fromJson(AllLinesUser, PlayerRatings.class);
//							playerRatings.setIdUser(idUser);
//							//account.setName(account.getData().getName());
//							//account.setDateCommunityAccount(new java.util.Date());
//							
//							//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sÃ©rialisation (pas de MAP !!))
//							CommunityAccount communityAccount =  TransformDtoObject.TransformPlayerRatingsToCommunityAccount(playerRatings);
//							
//							
//							//make some calculation of stats 
//							DataCommunityAccountRatings myDataCommunityAccountStats = communityAccount.getData();
//							
//							//== WR calculated
//							int battles = myDataCommunityAccountStats.getBattles();
//							int battlesWin = myDataCommunityAccountStats.getBattle_wins();
//							Double wrCal = (double) ((double)battlesWin/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							wrCal = wrCal * 100; //ex : 51,844444
//							int intWrCal = (int) (wrCal * 100); //ex : 5184
//							
//							wrCal = (double)intWrCal / 100 ; //ex : 51,84
//							myDataCommunityAccountStats.setBattle_avg_performanceCalc(wrCal);
//							
//							//== Ratio capture points calculated
//							int ctfPoints = myDataCommunityAccountStats.getCtf_points();
//							Double ctfPointsCal = (double) ((double)ctfPoints/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
//							int intCtfPointsCal = (int) (ctfPointsCal * 100); //ex : 128,27
//							
//							ctfPointsCal = (double)intCtfPointsCal / 100 ; //ex : 1,28
//							myDataCommunityAccountStats.setRatioCtfPoints(ctfPointsCal);
//							
//							//==Damage Ration calculated
//							int damagePoints = myDataCommunityAccountStats.getDamage_dealt();
//							Double ratioDamagePoints = (double) ((double)damagePoints/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
//							int intRatioDamagePoints = (int) (ratioDamagePoints * 100); //ex : 128,27
//							
//							ratioDamagePoints = (double)intRatioDamagePoints / 100 ; //ex : 1,28
//							myDataCommunityAccountStats.setRatioDamagePoints(ratioDamagePoints);
//							
//							
//							//==Ratio Defense calculated
//							int droppedCtfPoints = myDataCommunityAccountStats.getDropped_ctf_points();
//							Double ratioDroppedCtfPoints = (double) ((double)droppedCtfPoints/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
//							int intRatioDroppedCtfPoints = (int) (ratioDroppedCtfPoints * 100); //ex : 128,27
//							
//							ratioDroppedCtfPoints = (double)intRatioDroppedCtfPoints / 100 ; //ex : 1,28
//							myDataCommunityAccountStats.setRatioDroppedCtfPoints(ratioDroppedCtfPoints);
//							
//							
//							//==Ratio Destroyed calculated
//							int destroyedPoints = myDataCommunityAccountStats.getFrags();
//							Double ratiodestroyedPoints = (double) ((double)destroyedPoints/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
//							int intRatiodestroyedPoints = (int) (ratiodestroyedPoints * 100); //ex : 128,27
//							
//							ratiodestroyedPoints = (double)intRatiodestroyedPoints / 100 ; //ex : 1,28
//							myDataCommunityAccountStats.setRatioDestroyedPoints(ratiodestroyedPoints);
//							
//							//==Ratio Detected calculated
//							int detectedPoints = myDataCommunityAccountStats.getSpotted();
//							Double ratioDetectedPoints = (double) ((double)detectedPoints/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
//							int intRatioDetectedPoints = (int) (ratioDetectedPoints * 100); //ex : 128,27
//							
//							ratioDetectedPoints = (double)intRatioDetectedPoints / 100 ; //ex : 1,28
//							myDataCommunityAccountStats.setRatioDetectedPoints(ratioDetectedPoints);
//							
//							
//							
//							//add account
//							listCommunityAccount.add(communityAccount);
//							
//							
//
//						}//if treat 
//					}
//
//				}//for (DataCommunityClanMembers
//			} else {
//	
//				System.out.println("Erreur de parse");
//			}
//		} catch (MalformedURLException e) {
//			// ...
//			e.printStackTrace();
//		} catch (IOException e) {
//			// ...
//			e.printStackTrace();
//		}
//		finally {
//			pm.close();
//		}
//	
//		return myAllCommunityAccount;
//	
//	}

	//@Override
//	public AllCommunityAccount getAllMembersClanAndStats2( List<String> listIdUser) {
//	
//
//	
//		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
//	
//		// Escape data from the client to avoid cross-site script vulnerabilities.
//		userAgent = escapeHtml(userAgent);
//		
//		List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
//		AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
//		myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
//		PersistenceManager pm =null;
//		
//		try {
//			pm = PMF.get().getPersistenceManager();
//			
//			for(String idUser :listIdUser) {
//	
//						
//							URL url = null ;
//							
//							String urlServer = urlServerEU +"/2.0/account/ratings/?application_id=" + applicationIdEU + "&account_id=";
//							//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
//							
//							if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
//								url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + idUser);
//							}
//							else {
//								url = new URL(urlServer + idUser);
//							}
//							
//							HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
//							conn2.setReadTimeout(20000);
//							conn2.setConnectTimeout(20000);
//							conn2.getInputStream();
//							BufferedReader readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
//	
//							//BufferedReader readerUser = new BufferedReader(new InputStreamReader(url.openStream()));
//							String lineUser = "";
//							;
//							String AllLinesUser = "";
//			
//							while ((lineUser = readerUser.readLine()) != null) {
//								AllLinesUser = AllLinesUser + lineUser;
//							}
//							//System.out.println(AllLinesUser);
//							
//							readerUser.close();
//			
//							Gson gsonUser = new Gson();
//							//log.info(AllLinesUser.substring(0, 200));
//							PlayerRatings playerRatings = gsonUser.fromJson(AllLinesUser, PlayerRatings.class);
//							playerRatings.setIdUser(idUser);
//							//account.setName(account.getData().getName());
//							//account.setDateCommunityAccount(new java.util.Date());
//							
//							//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sÃ©rialisation (pas de MAP !!))
//							CommunityAccount communityAccount =  TransformDtoObject.TransformPlayerRatingsToCommunityAccount(playerRatings);
//							
//							
//							//make some calculation of stats 
//							DataCommunityAccountRatings myDataCommunityAccountStats = communityAccount.getData();
//							
//							//== WR calculated
//							int battles = myDataCommunityAccountStats.getBattles();
//							int battlesWin = myDataCommunityAccountStats.getBattle_wins();
//							Double wrCal = (double) ((double)battlesWin/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							wrCal = wrCal * 100; //ex : 51,844444
//							int intWrCal = (int) (wrCal * 100); //ex : 5184
//							
//							wrCal = (double)intWrCal / 100 ; //ex : 51,84
//							myDataCommunityAccountStats.setBattle_avg_performanceCalc(wrCal);
//							
//							//== Ratio capture points calculated
//							int ctfPoints = myDataCommunityAccountStats.getCtf_points();
//							Double ctfPointsCal = (double) ((double)ctfPoints/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
//							int intCtfPointsCal = (int) (ctfPointsCal * 100); //ex : 128,27
//							
//							ctfPointsCal = (double)intCtfPointsCal / 100 ; //ex : 1,28
//							myDataCommunityAccountStats.setRatioCtfPoints(ctfPointsCal);
//							
//							//==Damage Ration calculated
//							int damagePoints = myDataCommunityAccountStats.getDamage_dealt();
//							Double ratioDamagePoints = (double) ((double)damagePoints/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
//							int intRatioDamagePoints = (int) (ratioDamagePoints * 100); //ex : 128,27
//							
//							ratioDamagePoints = (double)intRatioDamagePoints / 100 ; //ex : 1,28
//							myDataCommunityAccountStats.setRatioDamagePoints(ratioDamagePoints);
//							
//							
//							//==Ratio Defense calculated
//							int droppedCtfPoints = myDataCommunityAccountStats.getDropped_ctf_points();
//							Double ratioDroppedCtfPoints = (double) ((double)droppedCtfPoints/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
//							int intRatioDroppedCtfPoints = (int) (ratioDroppedCtfPoints * 100); //ex : 128,27
//							
//							ratioDroppedCtfPoints = (double)intRatioDroppedCtfPoints / 100 ; //ex : 1,28
//							myDataCommunityAccountStats.setRatioDroppedCtfPoints(ratioDroppedCtfPoints);
//							
//							
//							//==Ratio Destroyed calculated
//							int destroyedPoints = myDataCommunityAccountStats.getFrags();
//							Double ratiodestroyedPoints = (double) ((double)destroyedPoints/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
//							int intRatiodestroyedPoints = (int) (ratiodestroyedPoints * 100); //ex : 128,27
//							
//							ratiodestroyedPoints = (double)intRatiodestroyedPoints / 100 ; //ex : 1,28
//							myDataCommunityAccountStats.setRatioDestroyedPoints(ratiodestroyedPoints);
//							
//							//==Ratio Detected calculated
//							int detectedPoints = myDataCommunityAccountStats.getSpotted();
//							Double ratioDetectedPoints = (double) ((double)detectedPoints/(double)battles);
//							
//							//on ne conserve que 2 digits aprÃ¨s la virgule 
//							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
//							int intRatioDetectedPoints = (int) (ratioDetectedPoints * 100); //ex : 128,27
//							
//							ratioDetectedPoints = (double)intRatioDetectedPoints / 100 ; //ex : 1,28
//							myDataCommunityAccountStats.setRatioDetectedPoints(ratioDetectedPoints);
//							
//							
//							
//							//add account
//							listCommunityAccount.add(communityAccount);
//							
//							
//	
////						}//if treat 
////					}
//	
//				}//for (DataCommunityClanMembers
////			} else {
////	
////				System.out.println("Erreur de parse");
////			}
//		} catch (MalformedURLException e) {
//			// ...
//			e.printStackTrace();
//		} catch (IOException e) {
//			// ...
//			e.printStackTrace();
//		}
//		finally {
//			pm.close();
//		}
//	
//		return myAllCommunityAccount;
//	
//	}

	@Override
	public AllCommunityAccount getAllMembersClanAndStatsHistorised(String idClan, List<String> listIdUser) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * parse le HTML du wiki achieveemnt pour en extraire les noms de mï¿½dailles , les src d'icï¿½nes et les descriptions
	 * @param AllLinesWot
	 * @param cat1Medal
	 * @param cat2Medal
	 * @param objFactory 
	 * @param wiki 
	 */
	void parseHtmlAchievement (String AllLinesWot, String cat1Medal, String cat2Medal, ObjectFactory objFactory, XmlWiki wiki) {
	
		//crÃ©ation category achievement
		XmlListCategoryAchievement myXmlListCategoryAchievement = objFactory.createXmlListCategoryAchievement();
		myXmlListCategoryAchievement.setNAME(cat1Medal);
		
		//crÃ©ation xlmDescrition category achievement
		XmlDescription myXmlDescription= objFactory.createXmlDescription();
		myXmlDescription.setVALUE("Desccription de la ctÃ©gorie de mÃ©dailles");
		myXmlListCategoryAchievement.setDESCRIPTION(myXmlDescription);
		
		//Ajouter la catÃ©gorie achievement au wiki
		wiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT().add(myXmlListCategoryAchievement);
		
		//parse WIKI HTML
		int pos1 = AllLinesWot.indexOf(cat1Medal+"</span></div>" );
		int pos2 = -1; 
		if ("printfooter".equalsIgnoreCase(cat2Medal))
			pos2 = AllLinesWot.indexOf(cat2Medal );
		else
			pos2 = AllLinesWot.indexOf(cat2Medal+"</span></div>" );
		
		
		//selon les catï¿½gories de mï¿½dailles, on doit aller rechercher la 2ï¿½me pour la 1ï¿½re ocurence du nom de la catï¿½gorie de mï¿½dailles
		//donc on prend toujours la derniï¿½re 
		
		if (pos1 == -1)
			pos1 = AllLinesWot.indexOf(cat1Medal+ " <i>" );
		
		
		if (pos2 == -1)
			pos2 = AllLinesWot.indexOf(cat2Medal+ " <i>" );
		
		
		System.out.println("=======>>>>>>>>>>>>>" + cat1Medal);
		if (pos1 != -1 && pos2 !=-1) {
			int posSrc = 0 ;
			while(posSrc != -1 && posSrc<pos2) {
				posSrc = AllLinesWot.indexOf("src=", pos1);
				int posSlashDiv = AllLinesWot.indexOf("</div>", posSrc); //on doit rechercher tous les src avant </div>
				
				if (posSrc != -1 && posSrc<pos2) {
					
					//on est dans les medailles en question
					String srcImgMedal = "";
					List<String> listSrcImgMedal = new ArrayList<String>();
					do {
						posSrc = AllLinesWot.indexOf("src=", pos1);
						posSrc=  posSrc+"src=".length();
						int posWidth = AllLinesWot.indexOf("width", posSrc);
						srcImgMedal = AllLinesWot.substring(posSrc+1, posWidth-2);
						listSrcImgMedal.add(srcImgMedal);
						
						pos1= posWidth;
						posSrc = AllLinesWot.indexOf("src=", pos1);
					}while(posSrc < posSlashDiv);
					
					int posDebutB = AllLinesWot.indexOf("<b>", pos1);
					int posFinB = AllLinesWot.indexOf("</b>", pos1);
					
					String titleMedal= AllLinesWot.substring(posDebutB+"<b>".length(), posFinB);
					for (String src : listSrcImgMedal) { 
						System.out.println(src + "\t" + titleMedal + "\t"); //titre de la mï¿½daille
					}
					pos1= posFinB;
					
					//la description de la mï¿½daille se trouve entre </b> et le prochain "<"
					int posInf = AllLinesWot.indexOf("<", posFinB + "</b>".length());
					String descMedalWithB= AllLinesWot.substring(posDebutB-1 + "<".length(), posInf);
					System.out.println("\t" + descMedalWithB);
					
					
					//crÃ©ation d'un achievement
					XmlListAchievement myXmlListAchievement = objFactory.createXmlListAchievement();
					
					//set du nom de la mÃ©daille
					myXmlListAchievement.setNAME(titleMedal);
					
					//set description de la mÃ©daille
					//crÃ©ation xlmDescrition achievement
					myXmlDescription= objFactory.createXmlDescription();
					myXmlDescription.setVALUE(descMedalWithB);
					myXmlListAchievement.setDESCRIPTION(myXmlDescription);
					
					//set des src des icÃ´nes des mÃ©dailles
					XmlListSrcImg myXmlListSrcImg = objFactory.createXmlListSrcImg();
					
					for (String src : listSrcImgMedal) {
						//crÃ©ation des src
						XmlSrc myXmlSrc = objFactory.createXmlSrc();
						myXmlSrc.setVALUE(src);
						
						//ajout Ã  la liste des src de la mÃ©daille
						myXmlListSrcImg.getSRC().add(myXmlSrc);
					}
					
					myXmlListAchievement.setSRCIMG(myXmlListSrcImg);
					
					//ajouter listAchievement Ã  CatÃ©gory achievement
					myXmlListCategoryAchievement.getACHIEVEMENT().add(myXmlListAchievement);
				}
				
				
			}
			
		}
		
		
	}

	//xx
	public HashMap<String, XmlListAchievement> BuidHashMapAchievement (XmlWiki xmlWiki) {
		HashMap<String, XmlListAchievement> hashMapAchievement = new HashMap<String, XmlListAchievement>();
		
		
		//parcours de toutes les catï¿½gories de mï¿½dailles
		for(XmlListCategoryAchievement listCatAch	:	xmlWiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT() ) {
			for (XmlListAchievement ach : listCatAch.getACHIEVEMENT()) {
				for (XmlSrc src : ach.getSRCIMG().getSRC()) {
					String srcValue = src.getVALUE();
					int posLastSlash  = srcValue.lastIndexOf("/");
					String nameFile = srcValue.substring(posLastSlash);
					hashMapAchievement.put(nameFile, ach);
				}
				
			}
		}
		
		return hashMapAchievement;
		
	}

	/**
		 * persist wot server's datas  in datastore appengine
		 * @param idClan
		 * @param indexBegin
	 * @param listIdUser 
		 * @param indexEnd
		 */
//		public List<String> persistAllStats(String idClan,  int indexBegin, int nbUsersToTreat , Date date, List<String> listIdUser) {
//		
//			//on raz la liste de joueurs persistÃ©s
//			if (indexBegin == 0 ) {
//				listUsersPersisted.clear();
//			}
//			
//			//List<String> listUserPersisted = new ArrayList<String>() ;
//			
//			log.warning("persistAllStats index debut " + indexBegin + " nb Users To Treat " + nbUsersToTreat);
//			for(String user : listIdUser) {
//				log.warning("user to persist : " + user);
//				
//			}
//			// Verify that the input is valid. //[502248407, 506128381]  
//			if (!FieldVerifier.isValidName(idClan)) {
//				// If the input is not valid, throw an IllegalArgumentException back to
//				// the client.
//				throw new IllegalArgumentException("Name must be at least 4 characters long");
//			}
//		
//			String userAgent = getThreadLocalRequest().getHeader("User-Agent");
//		
//			// Escape data from the client to avoid cross-site script vulnerabilities.
//			idClan = escapeHtml(idClan);
//			userAgent = escapeHtml(userAgent);
//			
//			List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
//			AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
//			myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
//			PersistenceManager pm =null;
//			pm = PMF.get().getPersistenceManager();
//			try {
//				
//				
//				URL urlClan = null ;
//				// recup des membres du clan NVS
//				urlClan = null ;
//				if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
//					urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);				
//				}
//				else {
//					//500006074
//					urlClan = new URL("http://api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);
//				}
//				
//				HttpURLConnection conn = (HttpURLConnection)urlClan.openConnection();
//				conn.setReadTimeout(60000);
//				conn.setConnectTimeout(60000);
//				conn.getInputStream();
//				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//				
//				//BufferedReader reader = new BufferedReader(new InputStreamReader(urlClan.openStream()));
//				String line = "";
//				String AllLines = "";
//		
//				while ((line = reader.readLine()) != null) {
//					AllLines = AllLines + line;
//				}
//				reader.close();
//		
//				Gson gson = new Gson();
//				
//				DaoCommunityClan2 daoCommunityClan = gson.fromJson(AllLines, DaoCommunityClan2.class);
//				daoCommunityClan.setIdClan(idClan);
//				daoCommunityClan.setDateCommunityClan(date);
//				//persist clan ?
//				
//				
//				if (saveData && indexBegin == 0){ //on ne fait qu'une fois Ã©conomie d'Ã©criture)
//					//pm = PMF.get().getPersistenceManager();
//			        try {
//			        	//must transform before persist the objet clan
//			        	pm.currentTransaction().begin();
//			        	
//			        	//DaoCommunityClan daoCommunityClan = TransformDtoObject.TransformCommunityClanToDaoCommunityClan(communityClan);
//			        	daoCommunityClan.setDateCommunityClan(date);
//			        	//Map<String, DaoDataCommunityClanMembers> hashMap = daoCommunityClan.getData();
//			        	
//						//Collection<DaoDataCommunityClanMembers> listClanMembers = hashMap.values();
//			
//			            pm.makePersistent(daoCommunityClan);
//			        	pm.currentTransaction().commit();
//			        	log.warning("key Dao CommunityClan " + daoCommunityClan.getKey());
//			        }
//			        catch(Exception e){
//			        	pm.currentTransaction().rollback();
//			        }
//			       finally {
//			            //pm.close();
//			        }
//				}
//				CommunityClan communityClan = TransformDtoObject.TransformCommunityDaoCommunityClanToCommunityClan(daoCommunityClan);
//				if (communityClan != null) {
//		
//					log.warning("communityClan " + communityClan.getIdClan());
//					DataCommunityClan myDataCommunityClan = communityClan.getData();
//		
//					List<DataCommunityClanMembers> listClanMembers = myDataCommunityClan.getMembers();
//		
//					for (DataCommunityClanMembers dataClanMember : listClanMembers) {
//						log.warning("dataClanMember " + dataClanMember.getMembers_count());
//						int nbMember = 0;
//						for (DataCommunityMembers member : dataClanMember.getMembers()) {
//							log.warning("membermember " + member.getAccount_name() + " " + member.getAccount_id() );
//							boolean treatUser = false;
//							if(listIdUser.contains(member.getAccount_id())) {
//								nbMember++;	
//								if (nbMember < nbUsersToTreat ) {
//									
//									treatUser = true;
//								}else {
//									treatUser = false;
//								}
//							}
//							
//							String idUser = member.getAccount_id();
//	
//							//si idUSer in listIdUser alaors on requï¿½te sinon rien
//							
//							
//							
//							if (treatUser) {
//								log.warning("treatUser " + treatUser);
//								// recup des datas du USER 506486576 (strong44)
//								// http://api.worldoftanks.eu/community/accounts/506486576/api/1.0/?source_token=WG-WoT_Assistant-1.3.2
//								// URL url = new URL("http://api.worldoftanks.eu/uc/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
//								URL url = null ;
//								if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
//									url = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/community/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
//								}
//								else {
//									url = new URL("http://api.worldoftanks.eu/community/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
//								}
//								HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
//								conn2.setReadTimeout(60000);
//								conn2.setConnectTimeout(60000);
//								conn2.getInputStream();
//								BufferedReader readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
//								String lineUser = "";
//								;
//								String AllLinesUser = "";
//				
//								while ((lineUser = readerUser.readLine()) != null) {
//									AllLinesUser = AllLinesUser + lineUser;
//								}
//								//System.out.println(AllLinesUser);
//								//log.warning("AllLinesUser " + AllLinesUser);
//								readerUser.close();
//				
//								Gson gsonUser = new Gson();
//								CommunityAccount account = gsonUser.fromJson(AllLinesUser, CommunityAccount.class);
//								account.setIdUser(idUser);
//								//account.setName(account.getData().getName());
//								//account.setDateCommunityAccount(new java.util.Date());
//								
//								//make some calculation of stats 
//								DataCommunityAccountRatings myDataCommunityAccountStats = account.getData();
//	//							
//								int battles = myDataCommunityAccountStats.getBattles();
//								log.warning(account.getName() + " " + battles);
//								
//								
//								//persist communityAccount ?
//						    	//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
//	
//								if (saveDataPlayer){
//									//pm = PMF.get().getPersistenceManager();
//							        try {
//							        	//must transform before persist the objet clan
//							        	pm.currentTransaction().begin();
//							        	DaoCommunityAccount2 daoCommunityAccount = TransformDtoObject.TransformCommunityAccountToDaoCommunityAccount(account);
//							        	
//							        	//pour eviter trop de donnÃ©es en base 60 write OP 
//							        	//daoCommunityAccount.getData().setAchievements(null);
//							        	daoCommunityAccount.setDateCommunityAccount(date);
//							        	//
//							        	pm.makePersistent(daoCommunityAccount);
//							        	pm.currentTransaction().commit();
//							        	log.warning("vehicules daoCommunityAccount " + daoCommunityAccount.getData().statsVehicules.get(0).getName() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getBattle_count() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getWin_count());
//							        	listUsersPersisted.add(account.getName());
//							        	
//							        }
//								    catch(Exception e){
//							        	pm.currentTransaction().rollback();
//							        }
//							        finally {
//							            //pm.close();
//							        }
//								}
//							}//if treat 
//						}
//	
//					}//for (DataCommunityClanMembers
//				} else {
//		
//					System.out.println("Erreur de parse");
//				}
//			} catch (MalformedURLException e) {
//				// ...
//				log.throwing("Persist stats", "", e);
//				log.severe(e.getLocalizedMessage());
//			} catch (IOException e) {
//				// ...
//				log.throwing("Persist stats", "", e);
//				e.printStackTrace();
//			} catch (Exception e) {
//					// ...
//				log.throwing("Persist stats", "", e);
//				log.severe(e.getLocalizedMessage());
//			}
//			finally {
//				if (pm != null)
//					pm.close();
//			}
//		
//			return listUsersPersisted;
//		
//		}

	/**
	 * get all historized stats of users with their id 
	 * @param range 
	 * @param idClan
	 * @param indexBegin
	 * @param indexEnd
	 * @return
	 */
	public List<CommunityAccount> getHistorizedStatsUsers(List<String> listIdUsers, int range ) {
		
		log.warning("getHistorizedStatsUsers for :" + listIdUsers.size() + " users");
		
		for (String user :  listIdUsers ) {
			log.warning("getHistorizedStatsUsers for user : " + user);
		}
		
		List<CommunityAccount> resultsFinal = new ArrayList<CommunityAccount>();
		

		PersistenceManager pm =null;
		
		try {
			pm = PMF.get().getPersistenceManager();
	    	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd hh:mm");

				
	        try {
	        	for (String user  : listIdUsers) {
	        	/// query
					Query query = pm.newQuery(DaoCommunityAccount2.class);
				    query.setFilter("idUser == nameParam");
				    query.setOrdering("name desc");
				    query.setOrdering("dateCommunityAccount desc");
				    query.setRange(0, range); //only 6 results 
				    //query.setOrdering("hireDate desc");
				    query.declareParameters("String nameParam");
				    List<DaoCommunityAccount2> resultsTmp = (List<DaoCommunityAccount2>) query.execute(user);
				    
				    if(resultsTmp.size() != 0 )
				    {
					    DaoCommunityAccount2 daoComAcc = resultsTmp.get(0);
					    CommunityAccount comAcc=  TransformDtoObject.TransformDaoCommunityAccountToCommunityAccount(daoComAcc);
					    String previousDate = "";
					    for (DaoCommunityAccount2 myDaoCommunityAccount : resultsTmp ) {
					    	//si 2 dates identiques se suivent on ne prend la deuxiÃ¨me
					    	String dateCurrent = "";
					    	if (myDaoCommunityAccount.getDateCommunityAccount() != null) { 
						    	dateCurrent = sdf.format(myDaoCommunityAccount.getDateCommunityAccount());
						    	if (!dateCurrent.equalsIgnoreCase(previousDate)) {
						    		comAcc.listDates.add(dateCurrent);
						    		comAcc.listbattles.add(myDaoCommunityAccount.getData().getStats().getBattles());
						    		
						    		comAcc.listBattlesWins.add(myDaoCommunityAccount.getData().getStats().getBattle_wins());
						    	}
					    	}
					    	previousDate = dateCurrent;
					    }
					    resultsFinal.add(comAcc);
				    }
				    query.closeAll();
			    
	        	}
			    
			    
	        }
		    catch(Exception e){
		    	log.severe(e.getLocalizedMessage());
	        	//pm.currentTransaction().rollback();
	        }
		} catch (Exception e) {
			e.printStackTrace();
			log.severe(e.getLocalizedMessage());
		} 
		finally {
			if(pm != null)
				pm.close();
		}
	
		return resultsFinal;
	
	}

	public Map<String, CommunityAccount> getMapHistorizedStatsUsers(List<String> listIdUsers, int range ) {
		
		log.warning("getHistorizedStatsUsers for :" + listIdUsers.size() + " users");
		
		for (String user :  listIdUsers ) {
			log.warning("getHistorizedStatsUsers for user : " + user);
		}
		
		Map<String, CommunityAccount> resultsFinal = new HashMap<String, CommunityAccount>();
		

		PersistenceManager pm =null;
		
		try {
			pm = PMF.get().getPersistenceManager();
	    	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd hh:mm");

				
	        try {
	        	for (String user  : listIdUsers) {
	        	/// query
					Query query = pm.newQuery(DaoCommunityAccount2.class);
				    query.setFilter("idUser == nameParam");
				    query.setOrdering("name desc");
				    query.setOrdering("dateCommunityAccount desc");
				    query.setRange(0, range); //only 6 results 
				    //query.setOrdering("hireDate desc");
				    query.declareParameters("String nameParam");
				    List<DaoCommunityAccount2> resultsTmp = (List<DaoCommunityAccount2>) query.execute(user);
				    
				    if(resultsTmp.size() != 0 )
				    {
					    DaoCommunityAccount2 daoComAcc = resultsTmp.get(0);
					    CommunityAccount comAcc=  TransformDtoObject.TransformDaoCommunityAccountToCommunityAccount(daoComAcc);
					    String previousDate = "";
					    for (DaoCommunityAccount2 myDaoCommunityAccount : resultsTmp ) {
					    	//si 2 dates identiques se suivent on ne prend la deuxiÃ¨me
					    	String dateCurrent = "";
					    	if (myDaoCommunityAccount.getDateCommunityAccount() != null) { 
						    	dateCurrent = sdf.format(myDaoCommunityAccount.getDateCommunityAccount());
						    	if (!dateCurrent.equalsIgnoreCase(previousDate)) {
						    		comAcc.listDates.add(dateCurrent);
						    		comAcc.listbattles.add(myDaoCommunityAccount.getData().getStats().getBattles());
						    		
						    		comAcc.listBattlesWins.add(myDaoCommunityAccount.getData().getStats().getBattle_wins());
						    	}
					    	}
					    	previousDate = dateCurrent;
					    }
					    resultsFinal.put(user, comAcc);
				    }
				    query.closeAll();
			    
	        	}
			    
			    
	        }
		    catch(Exception e){
		    	log.severe(e.getLocalizedMessage());
	        	//pm.currentTransaction().rollback();
	        }
		} catch (Exception e) {
			e.printStackTrace();
			log.severe(e.getLocalizedMessage());
		} 
		finally {
			if(pm != null)
				pm.close();
		}
	
		return resultsFinal;
	
	}

	/**
	 * get all historized stats tanks of users with their id 
	 * @param idClan
	 * @param indexBegin
	 * @param indexEnd
	 * @return
	 */
	public List<CommunityAccount> getHistorizedStatsTanksUsers(List<String> listIdUsers ) {
		
		log.warning("getHistorizedStatsTanksUsers for " + listIdUsers.size() + " users");
		List<CommunityAccount> resultsFinal = new ArrayList<CommunityAccount>();
		

		PersistenceManager pm =null;
		
		try {
			pm = PMF.get().getPersistenceManager();
	    	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd hh:mm");

				
	        try {
	        	for (String user  : listIdUsers) {
	        	/// query
					Query query = pm.newQuery(DaoCommunityAccount2.class);
				    query.setFilter("idUser == nameParam");
				    query.setOrdering("name desc");
				    query.setOrdering("dateCommunityAccount desc");
				    query.setRange(0, 6); //only 6 results 
				    //query.setOrdering("hireDate desc");
				    query.declareParameters("String nameParam");
				    List<DaoCommunityAccount2> resultsTmp = (List<DaoCommunityAccount2>) query.execute(user);
				    
				    if(resultsTmp.size() != 0 )
				    {
					    DaoCommunityAccount2 daoComAcc = resultsTmp.get(0);
					    CommunityAccount comAcc=  TransformDtoObject.TransformDaoCommunityAccountToCommunityAccount(daoComAcc);
					    String previousDate = "";
					    for (DaoCommunityAccount2 myDaoCommunityAccount : resultsTmp ) {
					    	//si 2 dates identiques se suivent on ne prend la deuxiÃ¨me
					    	String dateCurrent = "";
					    	if (myDaoCommunityAccount.getDateCommunityAccount() != null) { 
						    	dateCurrent = sdf.format(myDaoCommunityAccount.getDateCommunityAccount());
						    	if (!dateCurrent.equalsIgnoreCase(previousDate)) {
						    		CommunityAccount comAccForList=  TransformDtoObject.TransformDaoCommunityAccountToCommunityAccount(myDaoCommunityAccount);
						    		comAcc.listDates.add(dateCurrent);
						    		//comAcc.listBattlesTanks.add(comAccForList.g);  //bug now !!!!
						    		
						    		//comAcc.listBattlesTanksWins.add(myDaoCommunityAccount.getData().getStatsVehicules().getBattle_wins());
						    	}
					    	}
					    	previousDate = dateCurrent;
					    }
					    resultsFinal.add(comAcc);
				    }
				    query.closeAll();
			    
	        	}
			    
			    
	        }
		    catch(Exception e){
		    	log.severe(e.getLocalizedMessage());
	        	//pm.currentTransaction().rollback();
	        }
		} catch (Exception e) {
			e.printStackTrace();
			log.severe(e.getLocalizedMessage());
		} 
		finally {
			if(pm != null)
				pm.close();
		}
	
		return resultsFinal;
	
	}

	
	@Override
		public List<CommunityAccount> getHistorizedStatsTanks(  List<String> listIdUser) {
		
			return getHistorizedStatsTanksUsers(listIdUser);
		
		}
	
	@Override
		public List<CommunityAccount> getHistorizedStats(  List<String> listIdUser) {
		
			return getHistorizedStatsUsers(listIdUser, 6);
		
		}

	/**
		 * persist wot server's datas  in datastore appengine
		 * @param idClan
		 * @param indexBegin
	 * @param listIdUser 
		 * @param indexEnd
		 */
//		public List<String> persistAllStats2(Date date, List<String> listIdUser) {
//		
//
//			
//			//List<String> listUserPersisted = new ArrayList<String>() ;
//			
//			for(String user : listIdUser) {
//				log.warning("persistAllStats2 user to persist : " + user);
//				
//			}
//
//			List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
//			AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
//			myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
//			PersistenceManager pm =null;
//			pm = PMF.get().getPersistenceManager();
//			try {
//				
//					for (String idUser : listIdUser) {
//						log.warning("dataClanMember " + idUser);
//						// recup des datas du USER 506486576 (strong44)
//						// http://api.worldoftanks.eu/community/accounts/506486576/api/1.0/?source_token=WG-WoT_Assistant-1.3.2
//						// URL url = new URL("http://api.worldoftanks.eu/uc/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
//						URL url = null ;
//						
//						String urlServer = urlServerEU +"/2.0/account/ratings/?application_id=" + applicationIdEU + "&account_id=";
//						//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
//						
//						if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
//							url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + idUser);
//						}
//						else {
//							url = new URL(urlServer + idUser);
//						}
//						//
//						HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
//						conn2.setReadTimeout(20000);
//						conn2.setConnectTimeout(20000);
//						conn2.getInputStream();
//						BufferedReader readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
//						String lineUser = "";
//						;
//						String AllLinesUser = "";
//		
//						while ((lineUser = readerUser.readLine()) != null) {
//							AllLinesUser = AllLinesUser + lineUser;
//						}
//						//System.out.println(AllLinesUser);
//						//log.warning("AllLinesUser " + AllLinesUser);
//						readerUser.close();
//		
//						Gson gsonUser = new Gson();
//						PlayerRatings playerRatings = gsonUser.fromJson(AllLinesUser, PlayerRatings.class);
//						playerRatings.setIdUser(idUser);
//						//account.setName(account.getData().getName());
//						//account.setDateCommunityAccount(new java.util.Date());
//						
//						//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sÃ©rialisation (pas de MAP !!))
//						CommunityAccount communityAccount =  TransformDtoObject.TransformPlayerRatingsToCommunityAccount(playerRatings);
//						
//						//make some calculation of stats 
//						DataCommunityAccountRatings myDataCommunityAccountStats = communityAccount.getData();
////							
//						int battles = myDataCommunityAccountStats.getBattles();
//						log.warning(communityAccount.getIdUser() + " " + battles);
//						
//						
//						//persist communityAccount ?
//				    	//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
//
//						if (saveDataPlayer){
//							//pm = PMF.get().getPersistenceManager();
//					        try {
//					        	//must transform before persist the objet clan
//					        	pm.currentTransaction().begin();
//					        	DaoCommunityAccount2 daoCommunityAccount = TransformDtoObject.TransformCommunityAccountToDaoCommunityAccount(communityAccount);
//					        	
//					        	//pour eviter trop de donnÃ©es en base 60 write OP 
//					        	//daoCommunityAccount.getData().setAchievements(null);
//					        	daoCommunityAccount.setDateCommunityAccount(date);
//					        	//
//					        	pm.makePersistent(daoCommunityAccount);
//					        	pm.currentTransaction().commit();
//					        	//log.warning("vehicules daoCommunityAccount " + daoCommunityAccount.getData().statsVehicules.get(0).getName() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getBattle_count() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getWin_count());
//					        	listUsersPersisted.add(communityAccount.getIdUser());
//					        	
//					        }
//						    catch(Exception e){
//						    	log.log(Level.SEVERE, "Exception while saving daoCommunityAccount", e);
//					        	pm.currentTransaction().rollback();
//					        }
//					        finally {
//					            //pm.close();
//					        }
//						}
//	
//					}//for (DataCommunityClanMembers
//
//			} catch (MalformedURLException e) {
//				// ...
//				log.throwing("Persist stats", "", e);
//				log.severe(e.getLocalizedMessage());
//			} catch (IOException e) {
//				// ...
//				log.throwing("Persist stats", "", e);
//				e.printStackTrace();
//			} catch (Exception e) {
//					// ...
//				log.throwing("Persist stats", "", e);
//				log.severe(e.getLocalizedMessage());
//			}
//			finally {
//				if (pm != null)
//					pm.close();
//			}
//		
//			return listUsersPersisted;
//		
//		}

	//@Override
		public AllCommunityAccount getAllMembersClanAndStatsSAve( List<String> listIdUser) {
		
			String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		
			// Escape data from the client to avoid cross-site script vulnerabilities.
			userAgent = escapeHtml(userAgent);
			
			List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
			AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
			myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
			PersistenceManager pm =null;
			
			try {
				pm = PMF.get().getPersistenceManager();
				
				String AllIdUser ="";
				
				for(String idUser :listIdUser) {
					AllIdUser = AllIdUser + "," + idUser;
				}
				
		
							
				URL url = null ;
				
				String urlServer = urlServerEU +"/2.0/account/ratings/?application_id=" + applicationIdEU + "&account_id=";
				//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
				
				if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
					url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + AllIdUser);
				}
				else {
					url = new URL(urlServer + AllIdUser);
				}
			
				HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
				conn2.setReadTimeout(20000);
				conn2.setConnectTimeout(20000);
				conn2.getInputStream();
				BufferedReader readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));

				//BufferedReader readerUser = new BufferedReader(new InputStreamReader(url.openStream()));
				String lineUser = "";
				;
				String AllLinesUser = "";

				while ((lineUser = readerUser.readLine()) != null) {
					AllLinesUser = AllLinesUser + lineUser;
				}
				//System.out.println(AllLinesUser);
				
				readerUser.close();

				Gson gsonUser = new Gson();
				//log.info(AllLinesUser.substring(0, 200));
				PlayerRatings playerRatings = gsonUser.fromJson(AllLinesUser, PlayerRatings.class);
				//playerRatings.setIdUser(idUser);
				
				//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sÃ©rialisation (pas de MAP !!))
				List<CommunityAccount> listCommunityAccount1 =  TransformDtoObject.TransformPlayerRatingsToListCommunityAccount(playerRatings);
				
				
				//////////////////////////////////
				// ==API encyclopÃ©die des tanks - Pour obtenir le level des char (on doit calculier le tier moyen jouÃ©)
				// Dans les stats des joueurs nous avons le tank-id mais pas son level
				
				//http://api.worldoftanks.eu/2.0/encyclopedia/tanks/?application_id=d0a293dc77667c9328783d489c8cef73
				/*
				 * {"status":"ok",
				 * "count":336,
				 * "data":{ 
					"3073":{ 
					"nation_i18n":"U.S.S.R.","name":"#ussr_vehicles:T-46","level":3,"nation":"ussr","is_premium":false,"name_i18n":"T-46","type":"lightTank","tank_id":3073} 
					,"6417":{ 
					"nation_i18n":"Germany","name":"#germany_vehicles:PzIII_IV","level":5,"nation":"germany","is_premium":false,"name_i18n":"Pz.Kpfw. III\/IV","type":"mediumTank","tank_id":6417} 
				*/
				//=======================
				if (tankEncyclopedia == null) {
					urlServer = urlServerEU +"/2.0/encyclopedia/tanks/?application_id=" + applicationIdEU ;
					url = new URL(urlServer);
					
					conn2 = (HttpURLConnection)url.openConnection();
					conn2.setReadTimeout(20000);
					conn2.setConnectTimeout(20000);
					conn2.getInputStream();
					readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
	
					lineUser = "";
					AllLinesUser = "";
	
					while ((lineUser = readerUser.readLine()) != null) {
						AllLinesUser = AllLinesUser + lineUser;
					}
					readerUser.close();
	
					gsonUser = new Gson();
					tankEncyclopedia = gsonUser.fromJson(AllLinesUser, TankEncyclopedia.class);
				}
				if (tankEncyclopedia == null) {
					log.severe("tankEncyclopedia is null" );
					
				}
				else {
					log.warning("tankEncyclopedia is good" );
					if (tankEncyclopedia.getData() ==null ) {
						log.severe("tankEncyclopedia data is null" );
					}
					else {
						 Set<Entry<String, DataTankEncyclopedia>>  set = tankEncyclopedia.getData().entrySet();
						
//						for(Entry<String, DataTankEncyclopedia> entry : set) {
//							log.warning(entry.getValue().getName());
//							log.warning(entry.getKey());
//						}
						if (tankEncyclopedia.getData().get("6417") == null ){
							log.severe("tankEncyclopedia data get tank id 1 is null" );
						}
					}
							
				}
				
				//API stats des joueurs ========================
				//PB access denied sometimes
				//http://api.worldoftanks.eu/2.0/stats/accountbytime/?application_id=d0a293dc77667c9328783d489c8cef73&account_id=500423304&hours_ago=24
				//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
				
				// ==API stats des vÃ©hicules ==
				//http://api.worldoftanks.eu/2.0/account/tanks/?application_id=d0a293dc77667c9328783d489c8cef73&account_id=506486576,502674600
				urlServer = urlServerEU +"/2.0/account/tanks/?application_id=" + applicationIdEU + "&account_id=";
				if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
					url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + AllIdUser);
				}
				else {
					url = new URL(urlServer + AllIdUser);
				}
				//
				conn2 = (HttpURLConnection)url.openConnection();
				conn2.setReadTimeout(20000);
				conn2.setConnectTimeout(20000);
				conn2.getInputStream();
				readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
				lineUser = "";
				AllLinesUser = "";
				while ((lineUser = readerUser.readLine()) != null) {
					AllLinesUser = AllLinesUser + lineUser;
				}
				readerUser.close();
				gsonUser = new Gson();
				PlayerTankRatings playerTankRatings = gsonUser.fromJson(AllLinesUser, PlayerTankRatings.class);
				
				
				/////////////////////////////////
				
				//make some calculation of stats 
				for(CommunityAccount communityAccount : listCommunityAccount1) {
					String user_id = communityAccount.getIdUser();
//					
//					if(playerVehicles != null) {
//						if (playerVehicles.getData() != null ) {
//							for (DataPlayerVehicles myDataPlayerVehicles : playerVehicles.getData()) {
//								if ( user_id == myDataPlayerVehicles.getStat().getAccount_id() ) {
//									for (DataStatVehicle myDataStatVehicle : myDataPlayerVehicles.getStat().getVehicles() ) {
//										log.info(""+ myDataStatVehicle.getTank_id());
//									}
//								}
//							}
//						}else {
//							log.severe("playerVehicles.getData() is null ");
//						}
//					} else {
//						log.severe("playerVehicles is null ");
//					}
					
					//Rechercher dans playerTankRatings, les batailles sur tous les char du joueur ( pour caluler son tier moyen) 
					
					//
					Map<String,List<DataPlayerTankRatings>> mapDataPlayerTankRatings = playerTankRatings.getData();
					//Set<Entry<String, List<DataPlayerTankRatings>>>  setData = mapDataPlayerTankRatings.entrySet();
					//setData.
					List<DataPlayerTankRatings> listPlayerTanksRatings = mapDataPlayerTankRatings.get(user_id);
					
					//calcul du tier moyen
					Double nbBattles = 0.0;
					Double levelByBattles = 0.0 ; 
					Double averageLevelTank =0.0;
					
					for (DataPlayerTankRatings dataPlayerTankRatings : listPlayerTanksRatings) {
						int tankId= dataPlayerTankRatings.getTank_id() ;
						int battles = dataPlayerTankRatings.getStatistics().getAll().getBattles();
						//int wins = dataPlayerTankRatings.getStatistics().getAll().getWins();
						//
						//log.warning("tankId :" + tankId );
						if (tankEncyclopedia.getData().get(String.valueOf(tankId)) == null )
							log.severe ("tankEncyclopedia.getData().get(tankId) is null ");
						
						int levelTank = tankEncyclopedia.getData().get(String.valueOf(tankId)).getLevel();
						//
						nbBattles = nbBattles + battles;
						levelByBattles =levelByBattles + levelTank * battles;
					}
					averageLevelTank = levelByBattles/nbBattles;
					
					DataCommunityAccountRatings myDataCommunityAccountRatings = communityAccount.getData();
					
					//average level tank
					myDataCommunityAccountRatings.setAverageLevel(averageLevelTank);
					
					//log.info("averageLevelTank" + averageLevelTank);
					
					//== WR calculated
					int battles = myDataCommunityAccountRatings.getBattles();
					int battlesWin = myDataCommunityAccountRatings.getBattle_wins();
					Double wrCal = (double) ((double)battlesWin/(double)battles);
					
					//on ne conserve que 2 digits aprÃ¨s la virgule 
					wrCal = wrCal * 100; //ex : 51,844444
					int intWrCal = (int) (wrCal * 100); //ex : 5184
					
					wrCal = (double)intWrCal / 100 ; //ex : 51,84
					myDataCommunityAccountRatings.setBattle_avg_performanceCalc(wrCal);
					
					//== Ratio capture points calculated
					int ctfPoints = myDataCommunityAccountRatings.getCtf_points();
					Double ctfPointsCal = (double) ((double)ctfPoints/(double)battles);// 1,28 :1 = 1.28 
					
					//on ne conserve que 2 digits aprÃ¨s la virgule 
					//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
					int intCtfPointsCal = (int) (ctfPointsCal * 100); //ex intCtfPointsCal : 128,27 ctfPointsCal = 1.28
					
					ctfPointsCal = (double)intCtfPointsCal / 100 ; //ex ctfPointsCal : 1,28 intCtfPointsCal = 128
					myDataCommunityAccountRatings.setRatioCtfPoints(ctfPointsCal);
					
					//==Damage Ration calculated
					int damagePoints = myDataCommunityAccountRatings.getDamage_dealt();
					Double ratioDamagePoints = (double) ((double)damagePoints/(double)battles);
					
					//on ne conserve que 2 digits aprÃ¨s la virgule 
					//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
					int intRatioDamagePoints = (int) (ratioDamagePoints * 100); //ex : 128,27
					
					ratioDamagePoints = (double)intRatioDamagePoints / 100 ; //ex : 1,28
					myDataCommunityAccountRatings.setRatioDamagePoints(ratioDamagePoints);
					
					
					//==Ratio Defense calculated
					int droppedCtfPoints = myDataCommunityAccountRatings.getDropped_ctf_points();
					Double ratioDroppedCtfPoints = (double) ((double)droppedCtfPoints/(double)battles);
					
					//on ne conserve que 2 digits aprÃ¨s la virgule 
					//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
					int intRatioDroppedCtfPoints = (int) (ratioDroppedCtfPoints * 100); //ex : 128,27
					
					ratioDroppedCtfPoints = (double)intRatioDroppedCtfPoints / 100 ; //ex : 1,28
					myDataCommunityAccountRatings.setRatioDroppedCtfPoints(ratioDroppedCtfPoints);
					
					
					//==Ratio Destroyed calculated
					int destroyedPoints = myDataCommunityAccountRatings.getFrags();
					Double ratiodestroyedPoints = (double) ((double)destroyedPoints/(double)battles);
					
					//on ne conserve que 2 digits aprÃ¨s la virgule 
					//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
					int intRatiodestroyedPoints = (int) (ratiodestroyedPoints * 100); //ex : 128,27
					
					ratiodestroyedPoints = (double)intRatiodestroyedPoints / 100 ; //ex : 1,28
					myDataCommunityAccountRatings.setRatioDestroyedPoints(ratiodestroyedPoints);
					
					//==Ratio Detected calculated
					int detectedPoints = myDataCommunityAccountRatings.getSpotted();
					Double ratioDetectedPoints = (double) ((double)detectedPoints/(double)battles);
					
					//on ne conserve que 2 digits aprÃ¨s la virgule 
					//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
					int intRatioDetectedPoints = (int) (ratioDetectedPoints * 100); //ex : 128,27
					
					ratioDetectedPoints = (double)intRatioDetectedPoints / 100 ; //ex : 1,28
					myDataCommunityAccountRatings.setRatioDetectedPoints(ratioDetectedPoints);
				
				}
				//add account
				listCommunityAccount.addAll(listCommunityAccount1);
	

			} catch (MalformedURLException e) {
				// ...
				e.printStackTrace();
			} catch (IOException e) {
				// ...
				e.printStackTrace();
			}
			finally {
				pm.close();
			}
		
			return myAllCommunityAccount;
		
		}

	/**
			 * persist wot server's datas  in datastore appengine
			 * @param idClan
			 * @param indexBegin
		 * @param listIdClan 
			 * @param indexEnd
			 */
	public void persistAllStats3(Date date, List<String> listIdClan) {
	
		CronPersistPlayersStats.cronPersistAllStats (new Date(), "500006074");
	
	}

	/**
			 * persist wot server's datas  in datastore appengine
			 * @param idClan
			 * @param indexBegin
		 * @param listIdUser 
			 * @param indexEnd
			 */
	//		public List<String> persistAllStats2(Date date, List<String> listIdUser) {
	//		
	//
	//			
	//			//List<String> listUserPersisted = new ArrayList<String>() ;
	//			
	//			for(String user : listIdUser) {
	//				log.warning("persistAllStats2 user to persist : " + user);
	//				
	//			}
	//
	//			List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
	//			AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
	//			myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
	//			PersistenceManager pm =null;
	//			pm = PMF.get().getPersistenceManager();
	//			try {
	//				
	//					for (String idUser : listIdUser) {
	//						log.warning("dataClanMember " + idUser);
	//						// recup des datas du USER 506486576 (strong44)
	//						// http://api.worldoftanks.eu/community/accounts/506486576/api/1.0/?source_token=WG-WoT_Assistant-1.3.2
	//						// URL url = new URL("http://api.worldoftanks.eu/uc/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
	//						URL url = null ;
	//						
	//						String urlServer = urlServerEU +"/2.0/account/ratings/?application_id=" + applicationIdEU + "&account_id=";
	//						//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
	//						
	//						if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
	//							url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + idUser);
	//						}
	//						else {
	//							url = new URL(urlServer + idUser);
	//						}
	//						//
	//						HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
	//						conn2.setReadTimeout(20000);
	//						conn2.setConnectTimeout(20000);
	//						conn2.getInputStream();
	//						BufferedReader readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
	//						String lineUser = "";
	//						;
	//						String AllLinesUser = "";
	//		
	//						while ((lineUser = readerUser.readLine()) != null) {
	//							AllLinesUser = AllLinesUser + lineUser;
	//						}
	//						//System.out.println(AllLinesUser);
	//						//log.warning("AllLinesUser " + AllLinesUser);
	//						readerUser.close();
	//		
	//						Gson gsonUser = new Gson();
	//						PlayerRatings playerRatings = gsonUser.fromJson(AllLinesUser, PlayerRatings.class);
	//						playerRatings.setIdUser(idUser);
	//						//account.setName(account.getData().getName());
	//						//account.setDateCommunityAccount(new java.util.Date());
	//						
	//						//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sÃ©rialisation (pas de MAP !!))
	//						CommunityAccount communityAccount =  TransformDtoObject.TransformPlayerRatingsToCommunityAccount(playerRatings);
	//						
	//						//make some calculation of stats 
	//						DataCommunityAccountRatings myDataCommunityAccountStats = communityAccount.getData();
	////							
	//						int battles = myDataCommunityAccountStats.getBattles();
	//						log.warning(communityAccount.getIdUser() + " " + battles);
	//						
	//						
	//						//persist communityAccount ?
	//				    	//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
	//
	//						if (saveDataPlayer){
	//							//pm = PMF.get().getPersistenceManager();
	//					        try {
	//					        	//must transform before persist the objet clan
	//					        	pm.currentTransaction().begin();
	//					        	DaoCommunityAccount2 daoCommunityAccount = TransformDtoObject.TransformCommunityAccountToDaoCommunityAccount(communityAccount);
	//					        	
	//					        	//pour eviter trop de donnÃ©es en base 60 write OP 
	//					        	//daoCommunityAccount.getData().setAchievements(null);
	//					        	daoCommunityAccount.setDateCommunityAccount(date);
	//					        	//
	//					        	pm.makePersistent(daoCommunityAccount);
	//					        	pm.currentTransaction().commit();
	//					        	//log.warning("vehicules daoCommunityAccount " + daoCommunityAccount.getData().statsVehicules.get(0).getName() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getBattle_count() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getWin_count());
	//					        	listUsersPersisted.add(communityAccount.getIdUser());
	//					        	
	//					        }
	//						    catch(Exception e){
	//						    	log.log(Level.SEVERE, "Exception while saving daoCommunityAccount", e);
	//					        	pm.currentTransaction().rollback();
	//					        }
	//					        finally {
	//					            //pm.close();
	//					        }
	//						}
	//	
	//					}//for (DataCommunityClanMembers
	//
	//			} catch (MalformedURLException e) {
	//				// ...
	//				log.throwing("Persist stats", "", e);
	//				log.severe(e.getLocalizedMessage());
	//			} catch (IOException e) {
	//				// ...
	//				log.throwing("Persist stats", "", e);
	//				e.printStackTrace();
	//			} catch (Exception e) {
	//					// ...
	//				log.throwing("Persist stats", "", e);
	//				log.severe(e.getLocalizedMessage());
	//			}
	//			finally {
	//				if (pm != null)
	//					pm.close();
	//			}
	//		
	//			return listUsersPersisted;
	//		
	//		}
	
		@Override
			public AllCommunityAccount getAllMembersClanAndStats( List<String> listIdUser) {
			
				String userAgent = getThreadLocalRequest().getHeader("User-Agent");
			
				// Escape data from the client to avoid cross-site script vulnerabilities.
				userAgent = escapeHtml(userAgent);
				
				List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
				AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
				myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
				PersistenceManager pm =null;
				
				try {
					pm = PMF.get().getPersistenceManager();
					
					String AllIdUser ="";
					
					for(String idUser :listIdUser) {
						AllIdUser = AllIdUser + "," + idUser;
					}
					
			
								
					URL url = null ;
					
					String urlServer = urlServerEU +"/2.0/account/ratings/?application_id=" + applicationIdEU + "&account_id=";
					//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
					
					if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
						url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + AllIdUser);
					}
					else {
						url = new URL(urlServer + AllIdUser);
					}
				
					HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
					conn2.setReadTimeout(20000);
					conn2.setConnectTimeout(20000);
					conn2.getInputStream();
					BufferedReader readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
	
					//BufferedReader readerUser = new BufferedReader(new InputStreamReader(url.openStream()));
					String lineUser = "";
					;
					String AllLinesUser = "";
	
					while ((lineUser = readerUser.readLine()) != null) {
						AllLinesUser = AllLinesUser + lineUser;
					}
					//System.out.println(AllLinesUser);
					
					readerUser.close();
	
					Gson gsonUser = new Gson();
					//log.info(AllLinesUser.substring(0, 200));
					PlayerRatings playerRatings = gsonUser.fromJson(AllLinesUser, PlayerRatings.class);
					//playerRatings.setIdUser(idUser);
					
					//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sÃ©rialisation (pas de MAP !!))
					List<CommunityAccount> listCommunityAccount1 =  TransformDtoObject.TransformPlayerRatingsToListCommunityAccount(playerRatings);
					
					
					//////////////////////////////////
					// ==API encyclopÃ©die des tanks - Pour obtenir le level des char (on doit calculier le tier moyen jouÃ©)
					// Dans les stats des joueurs nous avons le tank-id mais pas son level
					
					//http://api.worldoftanks.eu/2.0/encyclopedia/tanks/?application_id=d0a293dc77667c9328783d489c8cef73
					/*
					 * {"status":"ok",
					 * "count":336,
					 * "data":{ 
						"3073":{ 
						"nation_i18n":"U.S.S.R.","name":"#ussr_vehicles:T-46","level":3,"nation":"ussr","is_premium":false,"name_i18n":"T-46","type":"lightTank","tank_id":3073} 
						,"6417":{ 
						"nation_i18n":"Germany","name":"#germany_vehicles:PzIII_IV","level":5,"nation":"germany","is_premium":false,"name_i18n":"Pz.Kpfw. III\/IV","type":"mediumTank","tank_id":6417} 
					*/
					
					
					//=======================
//					if (tankEncyclopedia == null) {
//						urlServer = urlServerEU +"/2.0/encyclopedia/tanks/?application_id=" + applicationIdEU ;
//						url = new URL(urlServer);
//						
//						conn2 = (HttpURLConnection)url.openConnection();
//						conn2.setReadTimeout(20000);
//						conn2.setConnectTimeout(20000);
//						conn2.getInputStream();
//						readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
//		
//						lineUser = "";
//						AllLinesUser = "";
//		
//						while ((lineUser = readerUser.readLine()) != null) {
//							AllLinesUser = AllLinesUser + lineUser;
//						}
//						readerUser.close();
//		
//						gsonUser = new Gson();
//						tankEncyclopedia = gsonUser.fromJson(AllLinesUser, TankEncyclopedia.class);
//					}
//					if (tankEncyclopedia == null) {
//						log.severe("tankEncyclopedia is null" );
//						
//					}
//					else {
//						log.warning("tankEncyclopedia is good" );
//						if (tankEncyclopedia.getData() ==null ) {
//							log.severe("tankEncyclopedia data is null" );
//						}
//						else {
//							 Set<Entry<String, DataTankEncyclopedia>>  set = tankEncyclopedia.getData().entrySet();
//							
//	//						for(Entry<String, DataTankEncyclopedia> entry : set) {
//	//							log.warning(entry.getValue().getName());
//	//							log.warning(entry.getKey());
//	//						}
//							if (tankEncyclopedia.getData().get("6417") == null ){
//								log.severe("tankEncyclopedia data get tank id 1 is null" );
//							}
//						}
//								
//					}
					//=========================
					
					
//					//API stats des joueurs ========================
//					//PB access denied sometimes
//					//http://api.worldoftanks.eu/2.0/stats/accountbytime/?application_id=d0a293dc77667c9328783d489c8cef73&account_id=500423304&hours_ago=24
//					//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
//					
//					// ==API stats des vÃ©hicules ==
//					//http://api.worldoftanks.eu/2.0/account/tanks/?application_id=d0a293dc77667c9328783d489c8cef73&account_id=506486576,502674600
//					urlServer = urlServerEU +"/2.0/account/tanks/?application_id=" + applicationIdEU + "&account_id=";
//					if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
//						url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + AllIdUser);
//					}
//					else {
//						url = new URL(urlServer + AllIdUser);
//					}
//					//
//					conn2 = (HttpURLConnection)url.openConnection();
//					conn2.setReadTimeout(20000);
//					conn2.setConnectTimeout(20000);
//					conn2.getInputStream();
//					readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
//					lineUser = "";
//					AllLinesUser = "";
//					while ((lineUser = readerUser.readLine()) != null) {
//						AllLinesUser = AllLinesUser + lineUser;
//					}
//					readerUser.close();
//					gsonUser = new Gson();
//					PlayerTankRatings playerTankRatings = gsonUser.fromJson(AllLinesUser, PlayerTankRatings.class);
					//=========================
					
					
					//on requete notre base les averageLevel y ont été stockés par le cron dans la table community Account
					Map <String, CommunityAccount> mapHistStatsUsers = getMapHistorizedStatsUsers(listIdUser, 6);
					
				

					
					
					/////////////////////////////////
					
					//make some calculation of stats 
					for(CommunityAccount communityAccount : listCommunityAccount1) {
						String user_id = communityAccount.getIdUser();
	//					
	//					if(playerVehicles != null) {
	//						if (playerVehicles.getData() != null ) {
	//							for (DataPlayerVehicles myDataPlayerVehicles : playerVehicles.getData()) {
	//								if ( user_id == myDataPlayerVehicles.getStat().getAccount_id() ) {
	//									for (DataStatVehicle myDataStatVehicle : myDataPlayerVehicles.getStat().getVehicles() ) {
	//										log.info(""+ myDataStatVehicle.getTank_id());
	//									}
	//								}
	//							}
	//						}else {
	//							log.severe("playerVehicles.getData() is null ");
	//						}
	//					} else {
	//						log.severe("playerVehicles is null ");
	//					}
						
						//Rechercher dans playerTankRatings, les batailles sur tous les char du joueur ( pour caluler son tier moyen) 
						
						//
						//Map<String,List<DataPlayerTankRatings>> mapDataPlayerTankRatings = playerTankRatings.getData();
						
						
						//Set<Entry<String, List<DataPlayerTankRatings>>>  setData = mapDataPlayerTankRatings.entrySet();
						//setData.
						log.warning("mapCommAcc " + mapHistStatsUsers);
						CommunityAccount  histStatUser= mapHistStatsUsers.get(user_id);
						
						//calcul du tier moyen
						//Double nbBattles = 0.0;
						//Double levelByBattles = 0.0 ; 
						log.warning("histStatUser " + histStatUser);
						log.warning("histStatUser.getData() " + histStatUser.getData());
						///log.warning("commAcc.getData().getAverageLevel " + commAcc.getData().getAverageLevel);
						
						Double averageLevelTank =histStatUser.getData().getAverageLevel(); 
						Double wn8 =histStatUser.getData().getWn8(); 
						
//						for (DataPlayerTankRatings dataPlayerTankRatings : listPlayerTanksRatings) {
//							int tankId= dataPlayerTankRatings.getTank_id() ;
//							int battles = dataPlayerTankRatings.getStatistics().getAll().getBattles();
//							//int wins = dataPlayerTankRatings.getStatistics().getAll().getWins();
//							//
//							//log.warning("tankId :" + tankId );
//							if (tankEncyclopedia.getData().get(String.valueOf(tankId)) == null )
//								log.severe ("tankEncyclopedia.getData().get(tankId) is null ");
//							
//							int levelTank = tankEncyclopedia.getData().get(String.valueOf(tankId)).getLevel();
//							//
//							nbBattles = nbBattles + battles;
//							levelByBattles =levelByBattles + levelTank * battles;
//						}
//						averageLevelTank = levelByBattles/nbBattles;
						
						DataCommunityAccountRatings myDataCommunityAccountRatings = communityAccount.getData();
						
						//average level tank
						myDataCommunityAccountRatings.setAverageLevel(averageLevelTank);
						
						myDataCommunityAccountRatings.setWn8(wn8);
						
						//log.info("averageLevelTank" + averageLevelTank);
						
						//== WR calculated
						int battles = myDataCommunityAccountRatings.getBattles();
						int battlesWin = myDataCommunityAccountRatings.getBattle_wins();
						Double wrCal = (double) ((double)battlesWin/(double)battles);
						
						//on ne conserve que 2 digits aprÃ¨s la virgule 
						wrCal = wrCal * 100; //ex : 51,844444
						int intWrCal = (int) (wrCal * 100); //ex : 5184
						
						wrCal = (double)intWrCal / 100 ; //ex : 51,84
						myDataCommunityAccountRatings.setBattle_avg_performanceCalc(wrCal);
						
						//== Ratio capture points calculated
						int ctfPoints = myDataCommunityAccountRatings.getCtf_points();
						Double ctfPointsCal = (double) ((double)ctfPoints/(double)battles);// 1,28 :1 = 1.28 
						
						//on ne conserve que 2 digits aprÃ¨s la virgule 
						//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
						int intCtfPointsCal = (int) (ctfPointsCal * 100); //ex intCtfPointsCal : 128,27 ctfPointsCal = 1.28
						
						ctfPointsCal = (double)intCtfPointsCal / 100 ; //ex ctfPointsCal : 1,28 intCtfPointsCal = 128
						myDataCommunityAccountRatings.setRatioCtfPoints(ctfPointsCal);
						
						//==Damage Ration calculated
						int damagePoints = myDataCommunityAccountRatings.getDamage_dealt();
						Double ratioDamagePoints = (double) ((double)damagePoints/(double)battles);
						
						//on ne conserve que 2 digits aprÃ¨s la virgule 
						//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
						int intRatioDamagePoints = (int) (ratioDamagePoints * 100); //ex : 128,27
						
						ratioDamagePoints = (double)intRatioDamagePoints / 100 ; //ex : 1,28
						myDataCommunityAccountRatings.setRatioDamagePoints(ratioDamagePoints);
						
						
						//==Ratio Defense calculated
						int droppedCtfPoints = myDataCommunityAccountRatings.getDropped_ctf_points();
						Double ratioDroppedCtfPoints = (double) ((double)droppedCtfPoints/(double)battles);
						
						//on ne conserve que 2 digits aprÃ¨s la virgule 
						//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
						int intRatioDroppedCtfPoints = (int) (ratioDroppedCtfPoints * 100); //ex : 128,27
						
						ratioDroppedCtfPoints = (double)intRatioDroppedCtfPoints / 100 ; //ex : 1,28
						myDataCommunityAccountRatings.setRatioDroppedCtfPoints(ratioDroppedCtfPoints);
						
						
						//==Ratio Destroyed calculated
						int destroyedPoints = myDataCommunityAccountRatings.getFrags();
						Double ratiodestroyedPoints = (double) ((double)destroyedPoints/(double)battles);
						
						//on ne conserve que 2 digits aprÃ¨s la virgule 
						//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
						int intRatiodestroyedPoints = (int) (ratiodestroyedPoints * 100); //ex : 128,27
						
						ratiodestroyedPoints = (double)intRatiodestroyedPoints / 100 ; //ex : 1,28
						myDataCommunityAccountRatings.setRatioDestroyedPoints(ratiodestroyedPoints);
						
						//==Ratio Detected calculated
						int detectedPoints = myDataCommunityAccountRatings.getSpotted();
						Double ratioDetectedPoints = (double) ((double)detectedPoints/(double)battles);
						
						//on ne conserve que 2 digits aprÃ¨s la virgule 
						//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
						int intRatioDetectedPoints = (int) (ratioDetectedPoints * 100); //ex : 128,27
						
						ratioDetectedPoints = (double)intRatioDetectedPoints / 100 ; //ex : 1,28
						myDataCommunityAccountRatings.setRatioDetectedPoints(ratioDetectedPoints);
					
					}
					//add account
					listCommunityAccount.addAll(listCommunityAccount1);
		
	
				} catch (MalformedURLException e) {
					// ...
					e.printStackTrace();
				} catch (IOException e) {
					// ...
					e.printStackTrace();
				}
				finally {
					pm.close();
				}
			
				return myAllCommunityAccount;
			
			}

	/**
				 * persist wot server's datas  in datastore appengine
				 * @param idClan
				 * @param indexBegin
			 * @param listIdUser 
				 * @param indexEnd
				 */
//		public List<String> persistAllStatsCron(Date date, String idClan) {
//		
//	
//	
//			List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
//			AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
//			myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
//			PersistenceManager pm =null;
//			pm = PMF.get().getPersistenceManager();
//			List<String> listIdUser = new ArrayList<String>();
//			
//			try {
//				///
//				
//				
//				URL urlClan = null ;
//				// recup des membres du clan NVS
//				urlClan = null ;
//				if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
//					urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);				
//				}
//				else {
//					//500006074
//					urlClan = new URL("http://api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);
//				}
//				
//				HttpURLConnection conn = (HttpURLConnection)urlClan.openConnection();
//				conn.setReadTimeout(60000);
//				conn.setConnectTimeout(60000);
//				conn.getInputStream();
//				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//				
//				String line = "";
//				String AllLines = "";
//		
//				while ((line = reader.readLine()) != null) {
//					AllLines = AllLines + line;
//				}
//				reader.close();
//				Gson gson = new Gson();
//				DaoCommunityClan2 daoCommunityClan = gson.fromJson(AllLines, DaoCommunityClan2.class);
//				daoCommunityClan.setIdClan(idClan);
//				daoCommunityClan.setDateCommunityClan(date);
//				//persist clan ?
//				
//				CommunityClan communityClan = TransformDtoObject.TransformCommunityDaoCommunityClanToCommunityClan(daoCommunityClan);
//				if (communityClan != null) {
//		
//					DataCommunityClan myDataCommunityClan = communityClan.getData();
//					List<DataCommunityClanMembers> listClanMembers = myDataCommunityClan.getMembers();
//		
//					for (DataCommunityClanMembers dataClanMember : listClanMembers) {
//						for (DataCommunityMembers member : dataClanMember.getMembers()) {
//							log.warning("membermember " + member.getAccount_name() + " " + member.getAccount_id() );
//							String idUser = member.getAccount_id();
//							//log.warning("treatUser " + treatUser);
//							listIdUser.add(idUser);
//						}
//	
//					}//for (DataCommunityClanMembers
//				} else {
//		
//					System.out.println("Erreur de parse");
//				}
//				////
//				String AllIdUser ="";
//				for(String idUser :listIdUser) {
//					AllIdUser = AllIdUser + "," + idUser;
//				}
//							
//				URL url = null ;
//				
//				String urlServer = urlServerEU +"/2.0/account/ratings/?application_id=" + applicationIdEU + "&account_id=";
//				//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
//				
//				if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
//					url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + AllIdUser);
//				}
//				else {
//					url = new URL(urlServer + AllIdUser);
//				}
//				
//				HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
//				conn2.setReadTimeout(20000);
//				conn2.setConnectTimeout(20000);
//				conn2.getInputStream();
//				BufferedReader readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
//	
//				//BufferedReader readerUser = new BufferedReader(new InputStreamReader(url.openStream()));
//				String lineUser = "";
//				;
//				String AllLinesUser = "";
//	
//				while ((lineUser = readerUser.readLine()) != null) {
//					AllLinesUser = AllLinesUser + lineUser;
//				}
//				
//				readerUser.close();
//				Gson gsonUser = new Gson();
//				PlayerRatings playerRatings = gsonUser.fromJson(AllLinesUser, PlayerRatings.class);
//				//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sÃ©rialisation (pas de MAP !!))
//				List<CommunityAccount> listCommunityAccount1 =  TransformDtoObject.TransformPlayerRatingsToListCommunityAccount(playerRatings);
//				
//				
//				/////////////////////
//				for(CommunityAccount communityAccount : listCommunityAccount1) {
//					
//						//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sÃ©rialisation (pas de MAP !!))
//						//communityAccount =  TransformDtoObject.TransformPlayerRatingsToCommunityAccount(playerRatings);
//						
//						//make some calculation of stats 
//						DataCommunityAccountRatings myDataCommunityAccountStats = communityAccount.getData();
//	//							
//						int battles = myDataCommunityAccountStats.getBattles();
//						log.warning("userId :" + communityAccount.getIdUser() + " battles : " + battles);
//	
//						if (saveDataPlayer){
//							//pm = PMF.get().getPersistenceManager();
//					        try {
//					        	//must transform before persist the objet clan
//					        	pm.currentTransaction().begin();
//					        	DaoCommunityAccount2 daoCommunityAccount = TransformDtoObject.TransformCommunityAccountToDaoCommunityAccount(communityAccount);
//					        	
//					        	//pour eviter trop de donnÃ©es en base 60 write OP 
//					        	//daoCommunityAccount.getData().setAchievements(null);
//					        	daoCommunityAccount.setDateCommunityAccount(date);
//					        	//
//					        	pm.makePersistent(daoCommunityAccount);
//					        	pm.currentTransaction().commit();
//					        	//log.warning("vehicules daoCommunityAccount " + daoCommunityAccount.getData().statsVehicules.get(0).getName() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getBattle_count() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getWin_count());
//					        	listUsersPersisted.add(communityAccount.getIdUser());
//					        	
//					        }
//						    catch(Exception e){
//						    	log.log(Level.SEVERE, "Exception while saving daoCommunityAccount", e);
//					        	pm.currentTransaction().rollback();
//					        }
//					        finally {
//					            //pm.close();
//					        }
//						}
//				}
//	//						}//for (DataCommunityClanMembers
//	
//			} catch (MalformedURLException e) {
//				// ...
//				log.throwing("Persist stats", "", e);
//				log.severe(e.getLocalizedMessage());
//			} catch (IOException e) {
//				// ...
//				log.throwing("Persist stats", "", e);
//				e.printStackTrace();
//			} catch (Exception e) {
//					// ...
//				log.throwing("Persist stats", "", e);
//				log.severe(e.getLocalizedMessage());
//			}
//			finally {
//				if (pm != null)
//					pm.close();
//			}
//		
//			return listUsersPersisted;
//		
//		}

	static void main (String arg[]) {
		WotServiceImpl wot  = new WotServiceImpl();
		 wot.getClans("NOVA_SNAIL", 0);
		 System.exit(0);
		
	}



}
