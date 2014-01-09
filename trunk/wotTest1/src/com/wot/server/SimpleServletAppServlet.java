package com.wot.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.wot.server.api.TransformDtoObject;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.CommunityAccount;
import com.wot.shared.CommunityClan;
import com.wot.shared.DataCommunityAccountRatings;
import com.wot.shared.DataCommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.DataCommunityMembers;
import com.wot.shared.PlayerRatings;

@SuppressWarnings("serial")
public class SimpleServletAppServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(WotServiceImpl.class.getName());
	static List<String> listUsersPersisted = new ArrayList<String>();
	String lieu = "maison"; //boulot ou maison si boulot -> pedro proxy 
	
	private String applicationIdEU = "d0a293dc77667c9328783d489c8cef73";
	private String urlServerEU =  "http://api.worldoftanks.eu";

	
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	log.warning("========lancement doGet SimpleServletAppServlet  ============== " );
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, persistStatsForCron for NVS");
        
        persistStatsForCron("500006074");//clan NVS
        persistStatsForCron("500006824");//clan FAFE -Forces Armées FrancophonEs
    }
    
    
    
	public String persistStatsForCron(String idClan) throws IllegalArgumentException {
		//
		log.warning("========lancement persistStatsForCron  ============== " + idClan);
		
		//recup des stats des user du clan NVS -- pour l'instant le seul clan
		// a terme il faut r�cup�rer les clan pr�sents en base, et requeter les stats de leur joueurs 

		
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
		
		
		
		
		persistAllStatsCron( new Date(), idClan);
		//return resultAchievement;
		return idClan;
	}

	public List<String> persistAllStatsCron(Date date, String idClan) {
		
		
		
		List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
		AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
		myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
		PersistenceManager pm =null;
		pm = PMF.get().getPersistenceManager();
		List<String> listIdUser = new ArrayList<String>();
		
		try {
			///
			
			
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
			
			String line = "";
			String AllLines = "";
	
			while ((line = reader.readLine()) != null) {
				AllLines = AllLines + line;
			}
			reader.close();
			Gson gson = new Gson();
			DaoCommunityClan2 daoCommunityClan = gson.fromJson(AllLines, DaoCommunityClan2.class);
			daoCommunityClan.setIdClan(idClan);
			daoCommunityClan.setDateCommunityClan(date);
			//persist clan ?
			
			CommunityClan communityClan = TransformDtoObject.TransformCommunityDaoCommunityClanToCommunityClan(daoCommunityClan);
			if (communityClan != null) {
	
				DataCommunityClan myDataCommunityClan = communityClan.getData();
				List<DataCommunityClanMembers> listClanMembers = myDataCommunityClan.getMembers();
	
				for (DataCommunityClanMembers dataClanMember : listClanMembers) {
					for (DataCommunityMembers member : dataClanMember.getMembers()) {
						log.warning("membermember " + member.getAccount_name() + " " + member.getAccount_id() );
						String idUser = member.getAccount_id();
						//log.warning("treatUser " + treatUser);
						listIdUser.add(idUser);
					}

				}//for (DataCommunityClanMembers
			} else {
	
				System.out.println("Erreur de parse");
			}
			////
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
			
			readerUser.close();
			Gson gsonUser = new Gson();
			PlayerRatings playerRatings = gsonUser.fromJson(AllLinesUser, PlayerRatings.class);
			//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la s�rialisation (pas de MAP !!))
			List<CommunityAccount> listCommunityAccount1 =  TransformDtoObject.TransformPlayerRatingsToListCommunityAccount(playerRatings);
			
			
			/////////////////////
			for(CommunityAccount communityAccount : listCommunityAccount1) {
				
					//Transform playerRatings en communityAccount (pour utiliser des types compatibles avec la s�rialisation (pas de MAP !!))
					//communityAccount =  TransformDtoObject.TransformPlayerRatingsToCommunityAccount(playerRatings);
					
					//make some calculation of stats 
					DataCommunityAccountRatings myDataCommunityAccountStats = communityAccount.getData();
//							
					int battles = myDataCommunityAccountStats.getBattles();
					log.warning("userId :" + communityAccount.getIdUser() + " battles : " + battles);

					if (true){
						//pm = PMF.get().getPersistenceManager();
				        try {
				        	//must transform before persist the objet clan
				        	pm.currentTransaction().begin();
				        	DaoCommunityAccount2 daoCommunityAccount = TransformDtoObject.TransformCommunityAccountToDaoCommunityAccount(communityAccount);
				        	
				        	//pour eviter trop de donn�es en base 60 write OP 
				        	//daoCommunityAccount.getData().setAchievements(null);
				        	daoCommunityAccount.setDateCommunityAccount(date);
				        	//
				        	pm.makePersistent(daoCommunityAccount);
				        	pm.currentTransaction().commit();
				        	//log.warning("vehicules daoCommunityAccount " + daoCommunityAccount.getData().statsVehicules.get(0).getName() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getBattle_count() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getWin_count());
				        	listUsersPersisted.add(communityAccount.getIdUser());
				        	
				        }
					    catch(Exception e){
					    	log.log(Level.SEVERE, "Exception while saving daoCommunityAccount", e);
				        	pm.currentTransaction().rollback();
				        }
				        finally {
				            //pm.close();
				        }
					}
			}
//						}//for (DataCommunityClanMembers

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

}
