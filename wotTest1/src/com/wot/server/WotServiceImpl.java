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
import java.util.logging.Logger;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.memetix.mst.detect.Detect;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
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
import com.wot.shared.FieldVerifier;
import com.wot.shared.ItemsDataClan;
import com.wot.shared.ObjectFactory;
import com.wot.shared.PlayerRatings;

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
	String lieu = "boulot"; //boulot ou maison si boulot -> pedro proxy 
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
	
	@Override
	protected void checkPermutationStrongName() throws SecurityException {
		return;
	}
	
	@Override
	public List<String> persistStats(String input, int indexBegin, int indexEnd, List<String> listIdUser) throws IllegalArgumentException {
		//
		log.warning("========lancement persistStats ============== " + input);
		
		//recup des stats des user du clan NVS -- pour l'instant le seul clan
		// a terme il faut récupérer les clan présents en base, et requeter les stats de leur joueurs 

		
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
		
		
		
		
		persistAllStats2( new Date(), listIdUser);
		//return resultAchievement;
		return listUsersPersisted;
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
		URL urlAchievement = null;
		String AllLinesWot = "";
		try {
			if(lieu.equalsIgnoreCase("boulot")) //on passe par 1 proxy
				urlAchievement = new URL ("https://pedro-proxy.appspot.com/wiki.worldoftanks.com/achievements");
			else
				urlAchievement = new URL ("http://wiki.worldoftanks.com/achievements");
			
			HttpURLConnection conn = (HttpURLConnection)urlAchievement.openConnection();
			conn.setReadTimeout(60000);
			conn.setConnectTimeout(60000);
			conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			//BufferedReader reader = new BufferedReader(new InputStreamReader(urlAchievement.openStream()));
			String line = "";
			

			while ((line = reader.readLine()) != null) {
				AllLinesWot = AllLinesWot + line;
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info(AllLinesWot);
		
		ObjectFactory objFactory = null;
		try {
			JAXBContext context = JAXBContext.newInstance(XmlWiki.class);
			
//			//création WIKI
			if (wiki== null) {
				objFactory = new ObjectFactory();
			
				wiki = objFactory.createXmlWiki();

				//A partir du XML instancié les classes !!
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
				      "name": "\"Белая гвардия\"", 
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
			
			//lecture de la réponse recherche du clan
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
		    
		    Translate.setClientId(idClient/* Enter your Windows Azure Client Id here */);
		    Translate.setClientSecret(secretClient/* Enter your Windows Azure Client Secret here */);

		    Detect.setClientId(idClient);
	        Detect.setClientSecret(secretClient);
	        
			int nbTrad = 0;
			for (ItemsDataClan myItemsDataClan : clan.getItems()) {
				translatedText = "Pas de traduction, Seules les 5 premieres lignes sont traduites";
				String motto = myItemsDataClan.getMotto();
				//detect lang motto
				try {
					Language detectedLanguage = null;
					
						detectedLanguage = Detect.execute(motto);
						
						if (detectedLanguage != null && !detectedLanguage.getName(Language.FRENCH).equalsIgnoreCase("Français") && nbTrad < 6  ) {
							translatedText = Translate.execute(motto, detectedLanguage, Language.FRENCH);
							myItemsDataClan.setMotto(motto + " (" + detectedLanguage.name() +") " + "--> traduction : " + translatedText);
							nbTrad++;
						}
					
				} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				}
			}
	
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
	 * getAllMembersClan : REcupére tous les menbres du clan
	 */
	@Override
	public CommunityClan getAllMembersClan(String idClan) {
		CommunityClan communityClan = null;
		
		DaoCommunityClan daoCommunityClan = null;
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
		      "description": "Закрытый клан, в состав которого входят лишь разработчики игры &quot;World of Tanks&quot;.\n\nЗаявки, посланные командиру клана через форум, НЕ РАССМАТРИВАЮТСЯ .", 
		      "description_html": "<p>Закрытый клан, в состав которого входят <i>лишь</i> разработчики игры &quot;World of Tanks&quot;.\n</p><p>\n<br/>Заявки, посланные командиру клана через форум, <i>НЕ РАССМАТРИВАЮТСЯ</i> .\n</p>", 
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
			      "motto": "Орлы! Орлицы!", 
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
			
			daoCommunityClan = gson.fromJson(AllLines, DaoCommunityClan.class);
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
	public AllCommunityAccount getAllMembersClanAndStats2(String idClan,  List<String> listIdUser) {
	
		//getStatsUsers(listIdUser);
		
		// Verify that the input is valid. //[502248407, 506128381]  
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
		PersistenceManager pm =null;
		
		try {
			pm = PMF.get().getPersistenceManager();
			
			URL urlClan = null ;
			// recup des membres du clan NVS
			urlClan = null ;
			if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				//        new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id=" + idClan );
				urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);				
			}
			else {
				//500006074
				//http://api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id=500006074
				//urlClan = new URL("http://api.worldoftanks.eu/community/clans/" + idClan + "/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");
				urlClan = new URL("http://api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);
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
			
			DaoCommunityClan daoCommunityClan = gson.fromJson(AllLines, DaoCommunityClan.class);
			daoCommunityClan.setIdClan(idClan);
			daoCommunityClan.setDateCommunityClan(new java.util.Date());

			CommunityClan communityClan = TransformDtoObject.TransformCommunityDaoCommunityClanToCommunityClan(daoCommunityClan);
			if (communityClan != null) {
	
				DataCommunityClan myDataCommunityClan = communityClan.getData();
	
				List<DataCommunityClanMembers> listClanMembers = myDataCommunityClan.getMembers();
	
				for (DataCommunityClanMembers dataClanMember : listClanMembers) {

					for (DataCommunityMembers member : dataClanMember.getMembers()) { 
						//
						// String nameUser ="";
						String idUser = member.getAccount_id();

						//si idUSer in listIdUser alaors on requ�te sinon rien
						boolean treatUser = false;
						if (listIdUser != null && listIdUser.size() != 0) {
							
							for(String idList :listIdUser) {
								if (idList.equalsIgnoreCase(idUser)) {
									treatUser = true;
									break;
								}
							}
						}else {
							treatUser = true;
						}
						
						if (treatUser) {
							// recup des datas du USER 506486576 (strong44)
							// http://api.worldoftanks.eu/community/accounts/506486576/api/1.0/?source_token=WG-WoT_Assistant-1.3.2
							// URL url = new URL("http://api.worldoftanks.eu/uc/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
							URL url = null ;
							
							String urlServer = urlServerEU +"/2.0/account/ratings/?application_id=" + applicationIdEU + "&account_id=";
							//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
							
							if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
								url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + idUser);
							}
							else {
								url = new URL(urlServer + idUser);
							}
							
							HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
							conn2.setReadTimeout(60000);
							conn2.setConnectTimeout(60000);
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
							log.info(AllLinesUser.substring(0, 200));
							PlayerRatings playerRatings = gsonUser.fromJson(AllLinesUser, PlayerRatings.class);
							playerRatings.setIdUser(idUser);
							//account.setName(account.getData().getName());
							//account.setDateCommunityAccount(new java.util.Date());
							
							//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sérialisation (pas de MAP !!))
							CommunityAccount communityAccount =  TransformDtoObject.TransformPlayerRatingsToCommunityAccount(playerRatings);
							
							
							//make some calculation of stats 
							DataCommunityAccountRatings myDataCommunityAccountStats = communityAccount.getData();
							
							//== WR calculated
							int battles = myDataCommunityAccountStats.getBattles();
							int battlesWin = myDataCommunityAccountStats.getBattle_wins();
							Double wrCal = (double) ((double)battlesWin/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							wrCal = wrCal * 100; //ex : 51,844444
							int intWrCal = (int) (wrCal * 100); //ex : 5184
							
							wrCal = (double)intWrCal / 100 ; //ex : 51,84
							myDataCommunityAccountStats.setBattle_avg_performanceCalc(wrCal);
							
							//== Ratio capture points calculated
							int ctfPoints = myDataCommunityAccountStats.getCtf_points();
							Double ctfPointsCal = (double) ((double)ctfPoints/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
							int intCtfPointsCal = (int) (ctfPointsCal * 100); //ex : 128,27
							
							ctfPointsCal = (double)intCtfPointsCal / 100 ; //ex : 1,28
							myDataCommunityAccountStats.setRatioCtfPoints(ctfPointsCal);
							
							//==Damage Ration calculated
							int damagePoints = myDataCommunityAccountStats.getDamage_dealt();
							Double ratioDamagePoints = (double) ((double)damagePoints/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
							int intRatioDamagePoints = (int) (ratioDamagePoints * 100); //ex : 128,27
							
							ratioDamagePoints = (double)intRatioDamagePoints / 100 ; //ex : 1,28
							myDataCommunityAccountStats.setRatioDamagePoints(ratioDamagePoints);
							
							
							//==Ratio Defense calculated
							int droppedCtfPoints = myDataCommunityAccountStats.getDropped_ctf_points();
							Double ratioDroppedCtfPoints = (double) ((double)droppedCtfPoints/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
							int intRatioDroppedCtfPoints = (int) (ratioDroppedCtfPoints * 100); //ex : 128,27
							
							ratioDroppedCtfPoints = (double)intRatioDroppedCtfPoints / 100 ; //ex : 1,28
							myDataCommunityAccountStats.setRatioDroppedCtfPoints(ratioDroppedCtfPoints);
							
							
							//==Ratio Destroyed calculated
							int destroyedPoints = myDataCommunityAccountStats.getFrags();
							Double ratiodestroyedPoints = (double) ((double)destroyedPoints/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
							int intRatiodestroyedPoints = (int) (ratiodestroyedPoints * 100); //ex : 128,27
							
							ratiodestroyedPoints = (double)intRatiodestroyedPoints / 100 ; //ex : 1,28
							myDataCommunityAccountStats.setRatioDestroyedPoints(ratiodestroyedPoints);
							
							//==Ratio Detected calculated
							int detectedPoints = myDataCommunityAccountStats.getSpotted();
							Double ratioDetectedPoints = (double) ((double)detectedPoints/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
							int intRatioDetectedPoints = (int) (ratioDetectedPoints * 100); //ex : 128,27
							
							ratioDetectedPoints = (double)intRatioDetectedPoints / 100 ; //ex : 1,28
							myDataCommunityAccountStats.setRatioDetectedPoints(ratioDetectedPoints);
							
							
							
							//add account
							listCommunityAccount.add(communityAccount);
							
							

						}//if treat 
					}

				}//for (DataCommunityClanMembers
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
		finally {
			pm.close();
		}
	
		return myAllCommunityAccount;
	
	}

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
//			DaoCommunityClan daoCommunityClan = gson.fromJson(AllLines, DaoCommunityClan.class);
//			daoCommunityClan.setIdClan(idClan);
//			daoCommunityClan.setDateCommunityClan(new java.util.Date());
	
//			CommunityClan communityClan = TransformDtoObject.TransformCommunityDaoCommunityClanToCommunityClan(daoCommunityClan);
//			if (communityClan != null) {
	
//				DataCommunityClan myDataCommunityClan = communityClan.getData();
//	
//				List<DataCommunityClanMembers> listClanMembers = myDataCommunityClan.getMembers();
	
			for(String idUser :listIdUser) {
	
//					for (DataCommunityMembers member : dataClanMember.getMembers()) { 
						//
						// String nameUser ="";
//						String idUser = member.getAccount_id();
	
						//si idUSer in listIdUser alaors on requ�te sinon rien
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
						
//						if (treatUser) {
							// recup des datas du USER 506486576 (strong44)
							// http://api.worldoftanks.eu/community/accounts/506486576/api/1.0/?source_token=WG-WoT_Assistant-1.3.2
							// URL url = new URL("http://api.worldoftanks.eu/uc/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
							URL url = null ;
							
							String urlServer = urlServerEU +"/2.0/account/ratings/?application_id=" + applicationIdEU + "&account_id=";
							//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
							
							if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
								url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + idUser);
							}
							else {
								url = new URL(urlServer + idUser);
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
							playerRatings.setIdUser(idUser);
							//account.setName(account.getData().getName());
							//account.setDateCommunityAccount(new java.util.Date());
							
							//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sérialisation (pas de MAP !!))
							CommunityAccount communityAccount =  TransformDtoObject.TransformPlayerRatingsToCommunityAccount(playerRatings);
							
							
							//make some calculation of stats 
							DataCommunityAccountRatings myDataCommunityAccountStats = communityAccount.getData();
							
							//== WR calculated
							int battles = myDataCommunityAccountStats.getBattles();
							int battlesWin = myDataCommunityAccountStats.getBattle_wins();
							Double wrCal = (double) ((double)battlesWin/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							wrCal = wrCal * 100; //ex : 51,844444
							int intWrCal = (int) (wrCal * 100); //ex : 5184
							
							wrCal = (double)intWrCal / 100 ; //ex : 51,84
							myDataCommunityAccountStats.setBattle_avg_performanceCalc(wrCal);
							
							//== Ratio capture points calculated
							int ctfPoints = myDataCommunityAccountStats.getCtf_points();
							Double ctfPointsCal = (double) ((double)ctfPoints/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
							int intCtfPointsCal = (int) (ctfPointsCal * 100); //ex : 128,27
							
							ctfPointsCal = (double)intCtfPointsCal / 100 ; //ex : 1,28
							myDataCommunityAccountStats.setRatioCtfPoints(ctfPointsCal);
							
							//==Damage Ration calculated
							int damagePoints = myDataCommunityAccountStats.getDamage_dealt();
							Double ratioDamagePoints = (double) ((double)damagePoints/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
							int intRatioDamagePoints = (int) (ratioDamagePoints * 100); //ex : 128,27
							
							ratioDamagePoints = (double)intRatioDamagePoints / 100 ; //ex : 1,28
							myDataCommunityAccountStats.setRatioDamagePoints(ratioDamagePoints);
							
							
							//==Ratio Defense calculated
							int droppedCtfPoints = myDataCommunityAccountStats.getDropped_ctf_points();
							Double ratioDroppedCtfPoints = (double) ((double)droppedCtfPoints/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
							int intRatioDroppedCtfPoints = (int) (ratioDroppedCtfPoints * 100); //ex : 128,27
							
							ratioDroppedCtfPoints = (double)intRatioDroppedCtfPoints / 100 ; //ex : 1,28
							myDataCommunityAccountStats.setRatioDroppedCtfPoints(ratioDroppedCtfPoints);
							
							
							//==Ratio Destroyed calculated
							int destroyedPoints = myDataCommunityAccountStats.getFrags();
							Double ratiodestroyedPoints = (double) ((double)destroyedPoints/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
							int intRatiodestroyedPoints = (int) (ratiodestroyedPoints * 100); //ex : 128,27
							
							ratiodestroyedPoints = (double)intRatiodestroyedPoints / 100 ; //ex : 1,28
							myDataCommunityAccountStats.setRatioDestroyedPoints(ratiodestroyedPoints);
							
							//==Ratio Detected calculated
							int detectedPoints = myDataCommunityAccountStats.getSpotted();
							Double ratioDetectedPoints = (double) ((double)detectedPoints/(double)battles);
							
							//on ne conserve que 2 digits après la virgule 
							//ctfPointsCal = ctfPointsCal * 100; //ex : 1,2827
							int intRatioDetectedPoints = (int) (ratioDetectedPoints * 100); //ex : 128,27
							
							ratioDetectedPoints = (double)intRatioDetectedPoints / 100 ; //ex : 1,28
							myDataCommunityAccountStats.setRatioDetectedPoints(ratioDetectedPoints);
							
							
							
							//add account
							listCommunityAccount.add(communityAccount);
							
							
	
//						}//if treat 
//					}
	
				}//for (DataCommunityClanMembers
//			} else {
//	
//				System.out.println("Erreur de parse");
//			}
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

	@Override
	public AllCommunityAccount getAllMembersClanAndStatsHistorised(String idClan, List<String> listIdUser) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * parse le HTML du wiki achieveemnt pour en extraire les noms de m�dailles , les src d'ic�nes et les descriptions
	 * @param AllLinesWot
	 * @param cat1Medal
	 * @param cat2Medal
	 * @param objFactory 
	 * @param wiki 
	 */
	void parseHtmlAchievement (String AllLinesWot, String cat1Medal, String cat2Medal, ObjectFactory objFactory, XmlWiki wiki) {
	
		//création category achievement
		XmlListCategoryAchievement myXmlListCategoryAchievement = objFactory.createXmlListCategoryAchievement();
		myXmlListCategoryAchievement.setNAME(cat1Medal);
		
		//création xlmDescrition category achievement
		XmlDescription myXmlDescription= objFactory.createXmlDescription();
		myXmlDescription.setVALUE("Desccription de la ctégorie de médailles");
		myXmlListCategoryAchievement.setDESCRIPTION(myXmlDescription);
		
		//Ajouter la catégorie achievement au wiki
		wiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT().add(myXmlListCategoryAchievement);
		
		//parse WIKI HTML
		int pos1 = AllLinesWot.indexOf(cat1Medal+"</span></div>" );
		int pos2 = -1; 
		if ("printfooter".equalsIgnoreCase(cat2Medal))
			pos2 = AllLinesWot.indexOf(cat2Medal );
		else
			pos2 = AllLinesWot.indexOf(cat2Medal+"</span></div>" );
		
		
		//selon les cat�gories de m�dailles, on doit aller rechercher la 2�me pour la 1�re ocurence du nom de la cat�gorie de m�dailles
		//donc on prend toujours la derni�re 
		
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
						System.out.println(src + "\t" + titleMedal + "\t"); //titre de la m�daille
					}
					pos1= posFinB;
					
					//la description de la m�daille se trouve entre </b> et le prochain "<"
					int posInf = AllLinesWot.indexOf("<", posFinB + "</b>".length());
					String descMedalWithB= AllLinesWot.substring(posDebutB-1 + "<".length(), posInf);
					System.out.println("\t" + descMedalWithB);
					
					
					//création d'un achievement
					XmlListAchievement myXmlListAchievement = objFactory.createXmlListAchievement();
					
					//set du nom de la médaille
					myXmlListAchievement.setNAME(titleMedal);
					
					//set description de la médaille
					//création xlmDescrition achievement
					myXmlDescription= objFactory.createXmlDescription();
					myXmlDescription.setVALUE(descMedalWithB);
					myXmlListAchievement.setDESCRIPTION(myXmlDescription);
					
					//set des src des icônes des médailles
					XmlListSrcImg myXmlListSrcImg = objFactory.createXmlListSrcImg();
					
					for (String src : listSrcImgMedal) {
						//création des src
						XmlSrc myXmlSrc = objFactory.createXmlSrc();
						myXmlSrc.setVALUE(src);
						
						//ajout à la liste des src de la médaille
						myXmlListSrcImg.getSRC().add(myXmlSrc);
					}
					
					myXmlListAchievement.setSRCIMG(myXmlListSrcImg);
					
					//ajouter listAchievement à Catégory achievement
					myXmlListCategoryAchievement.getACHIEVEMENT().add(myXmlListAchievement);
				}
				
				
			}
			
		}
		
		
	}

	//xx
	public HashMap<String, XmlListAchievement> BuidHashMapAchievement (XmlWiki xmlWiki) {
		HashMap<String, XmlListAchievement> hashMapAchievement = new HashMap<String, XmlListAchievement>();
		
		
		//parcours de toutes les cat�gories de m�dailles
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
		public List<String> persistAllStats(String idClan,  int indexBegin, int nbUsersToTreat , Date date, List<String> listIdUser) {
		
			//on raz la liste de joueurs persistés
			if (indexBegin == 0 ) {
				listUsersPersisted.clear();
			}
			
			//List<String> listUserPersisted = new ArrayList<String>() ;
			
			log.warning("persistAllStats index debut " + indexBegin + " nb Users To Treat " + nbUsersToTreat);
			for(String user : listIdUser) {
				log.warning("user to persist : " + user);
				
			}
			// Verify that the input is valid. //[502248407, 506128381]  
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
			PersistenceManager pm =null;
			pm = PMF.get().getPersistenceManager();
			try {
				
				
				URL urlClan = null ;
				// recup des membres du clan NVS
				urlClan = null ;
				if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
					urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);				
				}
				else {
					//500006074
					urlClan = new URL("http://api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);
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
				
				DaoCommunityClan daoCommunityClan = gson.fromJson(AllLines, DaoCommunityClan.class);
				daoCommunityClan.setIdClan(idClan);
				daoCommunityClan.setDateCommunityClan(date);
				//persist clan ?
				
				
				if (saveData && indexBegin == 0){ //on ne fait qu'une fois économie d'écriture)
					//pm = PMF.get().getPersistenceManager();
			        try {
			        	//must transform before persist the objet clan
			        	pm.currentTransaction().begin();
			        	
			        	//DaoCommunityClan daoCommunityClan = TransformDtoObject.TransformCommunityClanToDaoCommunityClan(communityClan);
			        	daoCommunityClan.setDateCommunityClan(date);
			        	//Map<String, DaoDataCommunityClanMembers> hashMap = daoCommunityClan.getData();
			        	
						//Collection<DaoDataCommunityClanMembers> listClanMembers = hashMap.values();
			
			            pm.makePersistent(daoCommunityClan);
			        	pm.currentTransaction().commit();
			        	log.warning("key Dao CommunityClan " + daoCommunityClan.getKey());
			        }
			        catch(Exception e){
			        	pm.currentTransaction().rollback();
			        }
			       finally {
			            //pm.close();
			        }
				}
				CommunityClan communityClan = TransformDtoObject.TransformCommunityDaoCommunityClanToCommunityClan(daoCommunityClan);
				if (communityClan != null) {
		
					log.warning("communityClan " + communityClan.getIdClan());
					DataCommunityClan myDataCommunityClan = communityClan.getData();
		
					List<DataCommunityClanMembers> listClanMembers = myDataCommunityClan.getMembers();
		
					for (DataCommunityClanMembers dataClanMember : listClanMembers) {
						log.warning("dataClanMember " + dataClanMember.getMembers_count());
						int nbMember = 0;
						for (DataCommunityMembers member : dataClanMember.getMembers()) {
							log.warning("membermember " + member.getAccount_name() + " " + member.getAccount_id() );
							boolean treatUser = false;
							if(listIdUser.contains(member.getAccount_id())) {
								nbMember++;	
								if (nbMember < nbUsersToTreat ) {
									
									treatUser = true;
								}else {
									treatUser = false;
								}
							}
							
							String idUser = member.getAccount_id();
	
							//si idUSer in listIdUser alaors on requ�te sinon rien
							
							
							
							if (treatUser) {
								log.warning("treatUser " + treatUser);
								// recup des datas du USER 506486576 (strong44)
								// http://api.worldoftanks.eu/community/accounts/506486576/api/1.0/?source_token=WG-WoT_Assistant-1.3.2
								// URL url = new URL("http://api.worldoftanks.eu/uc/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
								URL url = null ;
								if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
									url = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/community/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
								}
								else {
									url = new URL("http://api.worldoftanks.eu/community/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
								}
								HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
								conn2.setReadTimeout(60000);
								conn2.setConnectTimeout(60000);
								conn2.getInputStream();
								BufferedReader readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
								String lineUser = "";
								;
								String AllLinesUser = "";
				
								while ((lineUser = readerUser.readLine()) != null) {
									AllLinesUser = AllLinesUser + lineUser;
								}
								//System.out.println(AllLinesUser);
								//log.warning("AllLinesUser " + AllLinesUser);
								readerUser.close();
				
								Gson gsonUser = new Gson();
								CommunityAccount account = gsonUser.fromJson(AllLinesUser, CommunityAccount.class);
								account.setIdUser(idUser);
								//account.setName(account.getData().getName());
								//account.setDateCommunityAccount(new java.util.Date());
								
								//make some calculation of stats 
								DataCommunityAccountRatings myDataCommunityAccountStats = account.getData();
	//							
								int battles = myDataCommunityAccountStats.getBattles();
								log.warning(account.getName() + " " + battles);
								
								
								//persist communityAccount ?
						    	//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
	
								if (saveDataPlayer){
									//pm = PMF.get().getPersistenceManager();
							        try {
							        	//must transform before persist the objet clan
							        	pm.currentTransaction().begin();
							        	DaoCommunityAccount daoCommunityAccount = TransformDtoObject.TransformCommunityAccountToDaoCommunityAccount(account);
							        	
							        	//pour eviter trop de données en base 60 write OP 
							        	//daoCommunityAccount.getData().setAchievements(null);
							        	daoCommunityAccount.setDateCommunityAccount(date);
							        	//
							        	pm.makePersistent(daoCommunityAccount);
							        	pm.currentTransaction().commit();
							        	log.warning("vehicules daoCommunityAccount " + daoCommunityAccount.getData().statsVehicules.get(0).getName() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getBattle_count() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getWin_count());
							        	listUsersPersisted.add(account.getName());
							        	
							        }
								    catch(Exception e){
							        	pm.currentTransaction().rollback();
							        }
							        finally {
							            //pm.close();
							        }
								}
							}//if treat 
						}
	
					}//for (DataCommunityClanMembers
				} else {
		
					System.out.println("Erreur de parse");
				}
			} catch (MalformedURLException e) {
				// ...
				log.throwing("Persist stats", "", e);
				log.severe(e.getLocalizedMessage());
			} catch (IOException e) {
				// ...
				log.throwing("Persist stats", "", e);
				e.printStackTrace();
			} catch (Exception e) {
					// ...
				log.throwing("Persist stats", "", e);
				log.severe(e.getLocalizedMessage());
			}
			finally {
				if (pm != null)
					pm.close();
			}
		
			return listUsersPersisted;
		
		}

	/**
	 * get all historized stats of users with their id 
	 * @param idClan
	 * @param indexBegin
	 * @param indexEnd
	 * @return
	 */
	public List<CommunityAccount> getHistorizedStatsUsers(List<String> listIdUsers ) {
		
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
					Query query = pm.newQuery(DaoCommunityAccount.class);
				    query.setFilter("idUser == nameParam");
				    query.setOrdering("name desc");
				    query.setOrdering("dateCommunityAccount desc");
				    query.setRange(0, 6); //only 6 results 
				    //query.setOrdering("hireDate desc");
				    query.declareParameters("String nameParam");
				    List<DaoCommunityAccount> resultsTmp = (List<DaoCommunityAccount>) query.execute(user);
				    
				    if(resultsTmp.size() != 0 )
				    {
					    DaoCommunityAccount daoComAcc = resultsTmp.get(0);
					    CommunityAccount comAcc=  TransformDtoObject.TransformDaoCommunityAccountToCommunityAccount(daoComAcc);
					    String previousDate = "";
					    for (DaoCommunityAccount myDaoCommunityAccount : resultsTmp ) {
					    	//si 2 dates identiques se suivent on ne prend la deuxième
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
					Query query = pm.newQuery(DaoCommunityAccount.class);
				    query.setFilter("idUser == nameParam");
				    query.setOrdering("name desc");
				    query.setOrdering("dateCommunityAccount desc");
				    query.setRange(0, 6); //only 6 results 
				    //query.setOrdering("hireDate desc");
				    query.declareParameters("String nameParam");
				    List<DaoCommunityAccount> resultsTmp = (List<DaoCommunityAccount>) query.execute(user);
				    
				    if(resultsTmp.size() != 0 )
				    {
					    DaoCommunityAccount daoComAcc = resultsTmp.get(0);
					    CommunityAccount comAcc=  TransformDtoObject.TransformDaoCommunityAccountToCommunityAccount(daoComAcc);
					    String previousDate = "";
					    for (DaoCommunityAccount myDaoCommunityAccount : resultsTmp ) {
					    	//si 2 dates identiques se suivent on ne prend la deuxième
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
		
			return getHistorizedStatsUsers(listIdUser);
		
		}

	/**
		 * persist wot server's datas  in datastore appengine
		 * @param idClan
		 * @param indexBegin
	 * @param listIdUser 
		 * @param indexEnd
		 */
		public List<String> persistAllStats2(Date date, List<String> listIdUser) {
		

			
			//List<String> listUserPersisted = new ArrayList<String>() ;
			
			for(String user : listIdUser) {
				log.warning("persistAllStats user to persist : " + user);
				
			}

			List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
			AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
			myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
			PersistenceManager pm =null;
			pm = PMF.get().getPersistenceManager();
			try {
				
					for (String idUser : listIdUser) {
						log.warning("dataClanMember " + idUser);
						// recup des datas du USER 506486576 (strong44)
						// http://api.worldoftanks.eu/community/accounts/506486576/api/1.0/?source_token=WG-WoT_Assistant-1.3.2
						// URL url = new URL("http://api.worldoftanks.eu/uc/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
						URL url = null ;
						
						String urlServer = urlServerEU +"/2.0/account/ratings/?application_id=" + applicationIdEU + "&account_id=";
						//http://api.worldoftanks.ru/2.0/account/ratings/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
						
						if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
							url = new URL("https://pedro-proxy.appspot.com/"+urlServer.replaceAll("http://", "") + idUser);
						}
						else {
							url = new URL(urlServer + idUser);
						}
						//
						HttpURLConnection conn2 = (HttpURLConnection)url.openConnection();
						conn2.setReadTimeout(20000);
						conn2.setConnectTimeout(20000);
						conn2.getInputStream();
						BufferedReader readerUser = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
						String lineUser = "";
						;
						String AllLinesUser = "";
		
						while ((lineUser = readerUser.readLine()) != null) {
							AllLinesUser = AllLinesUser + lineUser;
						}
						//System.out.println(AllLinesUser);
						//log.warning("AllLinesUser " + AllLinesUser);
						readerUser.close();
		
						Gson gsonUser = new Gson();
						PlayerRatings playerRatings = gsonUser.fromJson(AllLinesUser, PlayerRatings.class);
						playerRatings.setIdUser(idUser);
						//account.setName(account.getData().getName());
						//account.setDateCommunityAccount(new java.util.Date());
						
						//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la sérialisation (pas de MAP !!))
						CommunityAccount communityAccount =  TransformDtoObject.TransformPlayerRatingsToCommunityAccount(playerRatings);
						
						//make some calculation of stats 
						DataCommunityAccountRatings myDataCommunityAccountStats = communityAccount.getData();
//							
						int battles = myDataCommunityAccountStats.getBattles();
						log.warning(communityAccount.getIdUser() + " " + battles);
						
						
						//persist communityAccount ?
				    	//java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");

						if (saveDataPlayer){
							//pm = PMF.get().getPersistenceManager();
					        try {
					        	//must transform before persist the objet clan
					        	pm.currentTransaction().begin();
					        	DaoCommunityAccount daoCommunityAccount = TransformDtoObject.TransformCommunityAccountToDaoCommunityAccount(communityAccount);
					        	
					        	//pour eviter trop de données en base 60 write OP 
					        	//daoCommunityAccount.getData().setAchievements(null);
					        	daoCommunityAccount.setDateCommunityAccount(date);
					        	//
					        	pm.makePersistent(daoCommunityAccount);
					        	pm.currentTransaction().commit();
					        	//log.warning("vehicules daoCommunityAccount " + daoCommunityAccount.getData().statsVehicules.get(0).getName() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getBattle_count() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getWin_count());
					        	listUsersPersisted.add(communityAccount.getIdUser());
					        	
					        }
						    catch(Exception e){
						    	log.severe(e.getLocalizedMessage());
					        	pm.currentTransaction().rollback();
					        }
					        finally {
					            //pm.close();
					        }
						}
	
					}//for (DataCommunityClanMembers

			} catch (MalformedURLException e) {
				// ...
				log.throwing("Persist stats", "", e);
				log.severe(e.getLocalizedMessage());
			} catch (IOException e) {
				// ...
				log.throwing("Persist stats", "", e);
				e.printStackTrace();
			} catch (Exception e) {
					// ...
				log.throwing("Persist stats", "", e);
				log.severe(e.getLocalizedMessage());
			}
			finally {
				if (pm != null)
					pm.close();
			}
		
			return listUsersPersisted;
		
		}

	static void main (String arg[]) {
		WotServiceImpl wot  = new WotServiceImpl();
		 wot.getClans("NOVA_SNAIL", 0);
		 System.exit(0);
		
	}



}
