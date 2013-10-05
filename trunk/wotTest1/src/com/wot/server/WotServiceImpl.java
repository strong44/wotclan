package com.wot.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.bcel.generic.DALOAD;

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
import com.wot.shared.DataCommunityAccount;
import com.wot.shared.DataCommunityAccountAchievements;
import com.wot.shared.DataCommunityAccountRatings;
import com.wot.shared.DataCommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.FieldVerifier;
import com.wot.shared.ItemsDataClan;
import com.wot.shared.ObjectFactory;
import com.wot.shared.XmlAchievements;
import com.wot.shared.XmlDescription;
import com.wot.shared.XmlListAchievement;
import com.wot.shared.XmlListCategoryAchievement;
import com.wot.shared.XmlListSrcImg;
import com.wot.shared.XmlSrc;
import com.wot.shared.XmlWiki;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WotServiceImpl extends RemoteServiceServlet implements WotService {
	String lieu = "maison"; //boulot ou maison si boulot -> pedro proxy 
	boolean saveData = false;
	XmlWiki wiki =  null;
	@Override
	public Clan getClan(String input) throws IllegalArgumentException {
		
		//

		
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
			String inputTransform = input.replace(" ", "%20");
			//input = input.replace(" ", "%20");
			if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				urlClan = new URL("https://tractro.appspot.com//api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=" +  inputTransform + "&offset=0&limit=1");				
			}
			else {
				urlClan = new URL("http://api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=" +  inputTransform + "&offset=0&limit=1");		
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
			
			//requete dans notre base
			PersistenceManager pm = PMF.get().getPersistenceManager();
			//DaoItemsDataClan

			//si retour ok on met à jour les données du clan dans la base de wotachievement
			if(desClan.getStatus().equalsIgnoreCase("ok")) {
				//pm.newQuery(arg0, arg1);
				Query query = pm.newQuery(DaoItemsDataClan.class);
			    query.setFilter("name == nameParam");
			    //query.setOrdering("hireDate desc");
			    query.declareParameters("String nameParam");
			    List<DaoItemsDataClan> results = (List<DaoItemsDataClan>) query.execute(input);
			    
			    for (DaoItemsDataClan myDaoItemsDataClan : results ) {
			    	System.out.println("" + myDaoItemsDataClan.abbreviation);
			    	System.out.println("" + myDaoItemsDataClan.clan_emblem_url);
			    	System.out.println("" + myDaoItemsDataClan.member_count);
			    	System.out.println("" + myDaoItemsDataClan.name);
			    }
			    query.deletePersistentAll(input);
			    query.closeAll();
			
			//
			
				//on persiste si le clan n'existe pas en base   
				
		        try {
		        	//must transform before persist the objet clan
		        	pm.close();
		        	pm = PMF.get().getPersistenceManager();
		        	pm.currentTransaction().begin();
		        	DaoClan daoClan = TransformDtoObject.TransformClanToDaoClan(desClan);
		            pm.makePersistentAll(daoClan.getData().getItems());
		        	pm.currentTransaction().commit();
		        	System.out.println("key daclan " + daoClan.getKey());
		            
		        } finally {
		            pm.close();
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
	public AllCommunityAccount getAllMembersClanAndStats(String idClan,  List<String> listIdUser) {

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
		
		try {
	
		
			URL urlClan = null ;
			// recup des membres du clan NVS
			urlClan = null ;
			if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				urlClan = new URL("https://tractro.appspot.com/api.worldoftanks.eu/community/clans/" + idClan + "/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");				
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
			if (indexDes > 0) {
			AllLines = AllLines.substring(0,indexDes); 
			AllLines = AllLines + " \"description_html\":\"aa\"}}";
			}
			
			CommunityClan communityClan = gson.fromJson(AllLines, CommunityClan.class);
			communityClan.setIdClan(idClan);
			communityClan.setDateCommunityClan(new java.util.Date());
			//persist clan ?
			
			PersistenceManager pm =null;
			if (saveData){
				pm = PMF.get().getPersistenceManager();
		        try {
		        	//must transform before persist the objet clan
		        	pm.currentTransaction().begin();
		        	DaoCommunityClan daoCommunityClan = TransformDtoObject.TransformCommunityClanToDaoCommunityClan(communityClan);
		            pm.makePersistent(daoCommunityClan);
		        	pm.currentTransaction().commit();
		        	System.out.println("key Dao CommunityClan " + daoCommunityClan.getKey());
		            
		        } finally {
		            pm.close();
		        }
			}
	        
			if (communityClan != null) {
	
				DataCommunityClan myDataCommunityClan = communityClan.getData();
	
				List<DataCommunityClanMembers> listMembers = myDataCommunityClan.getMembers();
	
				for (DataCommunityClanMembers dataMember : listMembers) {
	
					//
					// String nameUser ="";
					String idUser = dataMember.getAccount_id();
	
					//si idUSer in listIdUser alaors on requ�te sinon rien
					boolean treatUser = false;
					if (listIdUser.size() != 0) {
						
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
						// recup des datas du USER
						// http://api.worldoftanks.eu/community/accounts/506486576/api/1.0/?source_token=WG-WoT_Assistant-1.3.2
						// URL url = new URL("http://api.worldoftanks.eu/uc/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
						URL url = null ;
						if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
							url = new URL("https://tractro.appspot.com/api.worldoftanks.eu/community/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
						}
						else {
							url = new URL("http://api.worldoftanks.eu/community/accounts/" + idUser + "/api/1.8/?source_token=WG-WoT_Assistant-1.3.2");
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
						account.setIdUser(idUser);
						account.setName(account.getData().getName());
						//account.setDateCommunityAccount(new java.util.Date());
						
						//make some calculation of stats 
						DataCommunityAccountRatings myDataCommunityAccountStats = account.getData().getStats();
						
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
						listCommunityAccount.add(account);
						
						//persist communityAccount ?
						
						if (saveData){
							pm = PMF.get().getPersistenceManager();
					        try {
					        	//must transform before persist the objet clan
					        	pm.currentTransaction().begin();
					        	DaoCommunityAccount daoCommunityAccount = TransformDtoObject.TransformCommunityAccountToDaoCommunityAccount(account);
					            pm.makePersistent(daoCommunityAccount);
					        	pm.currentTransaction().commit();
					        	System.out.println("key daoCommunityAccount " + daoCommunityAccount.getKey());
					            
					        } finally {
					            pm.close();
					        }
						}
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
	
		return myAllCommunityAccount;
	
	}

	@Override
	public Clan getClans(String input, int offset) throws IllegalArgumentException {
		URL urlAchievement = null;
		String AllLinesWot = "";
		try {
			if(lieu.equalsIgnoreCase("boulot")) //on passe par 1 proxy
				urlAchievement = new URL ("https://tractro.appspot.com/wiki.worldoftanks.com/achievements");
			else
				urlAchievement = new URL ("http://wiki.worldoftanks.com/achievements");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlAchievement.openStream()));
			String line = "";
			

			while ((line = reader.readLine()) != null) {
				AllLinesWot = AllLinesWot + line;
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(AllLinesWot);
		
		//entre  "Battle Hero Achievements" et "Commemorative Achievements" se positionner à la fin le la chaine "src="
		// et extraire depuis cette position jusqu'au début de  la chaine width 
		String cat1BattleHero =  "Battle Hero Achievements";
		String cat2Comm = "Commemorative Achievements";
		String cat3Epc = "Epic Achievements (medals)"; //avec <i> à la fin 
		String cat4Special = "Special Achievements (titles)";
		String cat5Step = "Step Achievements (medals)";
		String cat6Rise = "Rise of the Americas Achievements (medals)" ;
		String cat7Clan = "Clan Wars Campaigns Achievements (medals)";
		
		ObjectFactory objFactory = null;
		//wiki =  null;
		try {
			JAXBContext context = JAXBContext.newInstance(XmlWiki.class);
//			Marshaller m = context.createMarshaller();
//			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
//			
//			//création WIKI
			if (wiki== null) {
				objFactory = new ObjectFactory();
			
				wiki = objFactory.createXmlWiki();

				//A partir du XML instancié les classes !!
				Unmarshaller unmarshaller = context.createUnmarshaller();
				wiki = (XmlWiki) unmarshaller.unmarshal(new File("wotWiki.xml"));
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>unmarshaller wotWiki.xml");
				System.out.println(wiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT().get(0).getNAME());
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
				urlClan = new URL("https://tractro.appspot.com/api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=" +  input + "&offset="+ offset+ "&limit=" + limit);					
			}
			else {
				urlClan = new URL("http://api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=" +  input + "&offset="+ offset+ "&limit=" + limit);		
			}
			
			//lecture de la r�ponse recherche du clan
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlClan.openStream(), "UTF-8"));
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
	        
			try {
				//translatedText = Translate.execute("Bonjour le monde", Language.FRENCH, Language.ENGLISH);
				//System.out.println(translatedText);
				
				// Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
		        
		        //Detect returns a Language Enum representing the language code
//		        Language detectedLanguage = Detect.execute("Bonjour le monde");
		        
		        // Prints out the language code
//		        System.out.println(detectedLanguage);
		        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int nbTrad = 0;
			for (ItemsDataClan myItemsDataClan : clan.getData().getItems()) {
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
					
			        
			        // Prints out the language code
//					if (detectedLanguage != null && !detectedLanguage.getName(Language.FRENCH).equalsIgnoreCase("Français"))
//						myItemsDataClan.setMotto(motto + " (" + detectedLanguage.name() +") " + "--> traduction : " + translatedText);
					
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
	
	@Override
		public CommunityClan getAllMembersClan(String idClan) {
			CommunityClan communityClan = null;
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
			
			try {
			
				URL urlClan = null ;
				// recup des membres du clan NVS
				urlClan = null ;
				if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
					urlClan = new URL("https://tractro.appspot.com/api.worldoftanks.eu/community/clans/" + idClan + "/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");				
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
				
				int indexDes = AllLines.indexOf("\"description_html") ;
				if (indexDes > 0) {
				AllLines = AllLines.substring(0,indexDes); 
				AllLines = AllLines + " \"description_html\":\"aa\"}}";
				}
				
				communityClan = gson.fromJson(AllLines, CommunityClan.class);
				communityClan.setIdClan(idClan);
				communityClan.setDateCommunityClan(new java.util.Date());
				//persist clan ?
				
				PersistenceManager pm =null;
				if (saveData){
					pm = PMF.get().getPersistenceManager();
			        try {
			        	//must transform before persist the objet clan
			        	pm.currentTransaction().begin();
			        	DaoCommunityClan daoCommunityClan = TransformDtoObject.TransformCommunityClanToDaoCommunityClan(communityClan);
			            pm.makePersistent(daoCommunityClan);
			        	pm.currentTransaction().commit();
			            
			        } finally {
			            pm.close();
			        }
				}
			} catch (MalformedURLException e) {
				// ...
				e.printStackTrace();
			} catch (IOException e) {
				// ...
				e.printStackTrace();
			}
			 ///
			return communityClan;
		}

	static void main (String arg[]) {
		WotServiceImpl wot  = new WotServiceImpl();
		 wot.getClans("NOVA_SNAIL", 0);
		 System.exit(0);
		
	}

}
