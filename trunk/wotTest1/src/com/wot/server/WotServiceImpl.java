package com.wot.server;

import java.io.BufferedReader;
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
import com.wot.shared.DataCommunityAccountStats;
import com.wot.shared.DataCommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.FieldVerifier;
import com.wot.shared.ItemsDataClan;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WotServiceImpl extends RemoteServiceServlet implements WotService {
	String lieu = "boulot"; //ou maison 
	boolean saveData = false;
	
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
			String inputTransform = input.replace(" ", "%20");
			//input = input.replace(" ", "%20");
			if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=" +  inputTransform + "&offset=0&limit=1");				
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
	public AllCommunityAccount getMembersClan(String idClan) {

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
		
		List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
		AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
		myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
		
		try {
			long maxAchievement = 0;
			String maxAchievementuserName = "";
	
		
			URL urlClan = null ;
			// recup des membres du clan NVS
			urlClan = null ;
			if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
				urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/community/clans/" + idClan + "/api/1.0/?source_token=WG-WoT_Assistant-1.3.2");				
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
					account.setIdUser(idUser);
					account.setName(account.getData().getName());
					//account.setDateCommunityAccount(new java.util.Date());
					
					//make some calculation of stats 
					DataCommunityAccountStats myDataCommunityAccountStats = account.getData().getStats();
					
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
					myDataCommunityAccountStats.setCtf_pointsCalc(ctfPointsCal);
					
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
			        
					Map<String, Integer> mapAchievement = new HashMap<String, Integer>();
	
//					if (account != null) {
//						DataCommunityAccount myDataCommunityAccount = account.getData();
//	
//						DataCommunityAccountAchievements myDataCommunityAccountAchievements = myDataCommunityAccount.getAchievements();
//						mapAchievement.put("Beasthunter", myDataCommunityAccountAchievements.getBeasthunter());
//						mapAchievement.put("Defender", myDataCommunityAccountAchievements.getDefender());
//						mapAchievement.put("Diehard", myDataCommunityAccountAchievements.getDiehard());
//						mapAchievement.put("Invader", myDataCommunityAccountAchievements.getInvader());
//						mapAchievement.put("Lumberjack", myDataCommunityAccountAchievements.getLumberjack());
//						mapAchievement.put("MedalAbrams", myDataCommunityAccountAchievements.getMedalAbrams());
//						mapAchievement.put("MedalBillotte", myDataCommunityAccountAchievements.getMedalBillotte());
//						mapAchievement.put("MedalBurda", myDataCommunityAccountAchievements.getMedalBurda());
//						mapAchievement.put("MedalCarius", myDataCommunityAccountAchievements.getMedalCarius());
//						mapAchievement.put("MedalEkins", myDataCommunityAccountAchievements.getMedalEkins());
//						mapAchievement.put("MedalFadin", myDataCommunityAccountAchievements.getMedalFadin());
//						mapAchievement.put("MedalHalonen", myDataCommunityAccountAchievements.getMedalHalonen());
//						mapAchievement.put("MedalKay", myDataCommunityAccountAchievements.getMedalKay());
//						mapAchievement.put("MedalKnispel", myDataCommunityAccountAchievements.getMedalKnispel());
//						mapAchievement.put("MedalKolobanov", myDataCommunityAccountAchievements.getMedalKolobanov());
//						mapAchievement.put("MedalLavrinenko", myDataCommunityAccountAchievements.getMedalLavrinenko());
//						mapAchievement.put("MedalLeClerc", myDataCommunityAccountAchievements.getMedalLeClerc());
//						mapAchievement.put("MedalOrlik", myDataCommunityAccountAchievements.getMedalOrlik());
//						mapAchievement.put("MedalOskin", myDataCommunityAccountAchievements.getMedalOskin());
//						mapAchievement.put("MedalPoppel", myDataCommunityAccountAchievements.getMedalPoppel());
//						mapAchievement.put("MedalWittmann", myDataCommunityAccountAchievements.getMedalWittmann());
//						mapAchievement.put("Mousebane", myDataCommunityAccountAchievements.getMousebane());
//						mapAchievement.put("Raider", myDataCommunityAccountAchievements.getRaider());
//						mapAchievement.put("Scout", myDataCommunityAccountAchievements.getScout());
//						mapAchievement.put("Sniper", myDataCommunityAccountAchievements.getSniper());
//						mapAchievement.put("Supporter", myDataCommunityAccountAchievements.getSupporter());
//						mapAchievement.put("TankExpert", myDataCommunityAccountAchievements.getTankExpert());
//						mapAchievement.put("TitleSniper", myDataCommunityAccountAchievements.getTitleSniper());
//						mapAchievement.put("Warrior", myDataCommunityAccountAchievements.getWarrior());
//	
//						Set<Entry<String, Integer>> setEntryAchievement = mapAchievement.entrySet();
//						long nbAch = 0;
//						for (Entry<String, Integer> entry : setEntryAchievement) {
//							// System.out.println(myDataCommunityAccount.getName()+ ";" +entry.getKey() + ";" + entry.getValue()+";"+nbAch);
//							nbAch = nbAch + entry.getValue();
//	
//						}
//						long battles = 0;
//	
//						if (myDataCommunityAccount.getStats() != null) {
//							battles = myDataCommunityAccount.getStats().getBattles();
//						}
//						float moy = battles / nbAch;
//	
//						// le resultat envoy� au client
//						//resultAchievement = resultAchievement + "\\n\\r" + myDataCommunityAccount.getName() + ";" + battles + "\\n\r";
//						resultAchievement = resultAchievement + myDataCommunityAccount.getName() + ";" + battles + " ";
//	
//						System.out.println(myDataCommunityAccount.getName() + ";" + moy);
//	
//						if (nbAch > maxAchievement) {
//							maxAchievement = nbAch;
//							maxAchievementuserName = myDataCommunityAccount.getName();
//						}
//						// System.out.println("--");
//	
//					} else {
//	
//						System.out.println("Erreur de parse");
//					}
	
				}//for (DataCommunityClanMembers
	
				//System.out.println("maxAchievementuserName   " + maxAchievementuserName + " maxAchievement " + maxAchievement);
	
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
				urlClan = new URL("https://pedro-proxy.appspot.com/api.worldoftanks.eu/community/clans/api/1.1/?source_token=WG-WoT_Assistant-1.3.2&search=" +  input + "&offset="+ offset+ "&limit=" + limit);					
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
				Language detectedLanguage = null;
				try {
					detectedLanguage = Detect.execute(motto);
					
					if (nbTrad < 6 && detectedLanguage != null ) {
						translatedText = Translate.execute(motto, detectedLanguage, Language.FRENCH);
						nbTrad++;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
		        
		        // Prints out the language code
				if (detectedLanguage != null)
					myItemsDataClan.setMotto(motto + " (" + detectedLanguage.name() +") " + "--> traduction : " + translatedText);
			}
	
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
