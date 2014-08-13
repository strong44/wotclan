package com.wot.server;

import java.io.BufferedReader;
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
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import org.apache.tools.ant.taskdefs.Get;

import com.google.gson.Gson;
import com.wot.server.api.TransformDtoObject;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.AllStatistics;
import com.wot.shared.CommunityAccount;
import com.wot.shared.CommunityClan;
import com.wot.shared.DataPlayerInfos;
import com.wot.shared.DataCommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.DataCommunityMembers;
import com.wot.shared.DataPlayerTankRatings;
import com.wot.shared.DataTankEncyclopedia;
import com.wot.shared.DataWnEfficientyTank;
import com.wot.shared.PlayersInfos;
import com.wot.shared.PlayerTankRatings;
import com.wot.shared.Statistics;
import com.wot.shared.TankEncyclopedia;
import com.wot.shared.WnEfficientyTank;

@SuppressWarnings("serial")
public class ReadPersistPlayersStats extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(WotServiceImpl.class.getName());
	
	private static String applicationIdEU = "d0a293dc77667c9328783d489c8cef73";
	private static String urlServerEU =  "http://api.worldoftanks.eu";

	
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	log.warning("========lancement doGet  ReadPersistPlayersStats ============== " );
        resp.setContentType("text/html");
        //resp.getWriter().println("Hello, ReadPersistPlayersStats ");
        String clanId = req.getParameter("clanId");
        String userName = req.getParameter("userName");
        if(clanId != null && !"".equalsIgnoreCase(clanId) && userName != null && !"".equalsIgnoreCase(userName)) {
        	List<CommunityAccount> listCommAcc = readPersistAllStats( new Date(), clanId, userName);
        	//build a HTML result
        	if (listCommAcc.size() > 0) { 
        		CommunityAccount commAcc = listCommAcc.get(0);
        		double wn8 = commAcc.getData().getStatistics().getAllStatistics().getWn8();
        		StringBuffer strBuf = new StringBuffer();
        		//<html
        		//strBuf.append("<HTML>");
        		//strBuf.append("<BODY>");
        		
        		strBuf.append("<TABLE border>").
        					//entêtes des colonnes
			        		append("<TR>").
								append("<TH>").
									append("WN8").
								append("</TH>").
							append("</TR>").
        					append("<TR>").
        						append("<TD>").
        							append(wn8).
        						append("</TD>").
        					append("</TR>").
        				append("</TABLE>");
        		
        		//strBuf.append("</BODY>");
        		//strBuf.append("</HTML>");
        		String buf= strBuf.toString();
        		resp.getWriter().println(buf);
        	}
        	
		}else {
			log.severe("ERROR: =======lancement ReadPersistPlayersStats  with idClan or userName null ===" + clanId +":" +userName);
		}
    }



	public static List<CommunityAccount> readPersistAllStats(Date date, String idClan, String userName) {
			
			log.warning("========lancement readPersistAllStats : " +  date + ":" + idClan + ":"+ userName + " :============== " );
			
			List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
			AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
			myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
			PersistenceManager pm =null;
			pm = PMF.get().getPersistenceManager();
			List<String> listIdUser = new ArrayList<String>();
			//List<DataPlayerInfos> listPlayerInfos = null;
			
			List<CommunityAccount> resultsFinal = new ArrayList<CommunityAccount>();
			
			try {
				
				//construction de la liste des id des joueurs du clan (séparateur la ,)  
				String idUser = generateAllIdUsers(idClan, userName, date);System.out.println(date);
				if (idUser != null ){
					
						pm = PMF.get().getPersistenceManager();
				    	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd hh:mm");
							
				        try {
				        	
				        	/// query
								Query query = pm.newQuery(DaoCommunityAccount2.class);
							    query.setFilter("idUser == nameParam");
							    query.setOrdering("name desc");
							    query.setOrdering("dateCommunityAccount desc");
							    query.setRange(0, 1); //only 6 results 
							    //query.setOrdering("hireDate desc");
							    query.declareParameters("String nameParam");
							    List<DaoCommunityAccount2> resultsTmp = (List<DaoCommunityAccount2>) query.execute(idUser);
							    
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
					    catch(Exception e){
					    	log.severe(e.getLocalizedMessage());
				        	//pm.currentTransaction().rollback();
				        }
				}

	
			}  catch (Exception e) {
					// ...
				e.printStackTrace();
				log.throwing("Persist stats", "", e);
				log.severe("Exception " + e.getLocalizedMessage());
				 StackTraceElement[] stack = e.getStackTrace();
//				 for (StackTraceElement st : stack) {
//					 log.severe(st.getMethodName()+":"+st.getLineNumber());
//					 
//					 
//				 }
				 
				//e.printStackTrace();
			}
			finally {
				if (pm != null)
					pm.close();
			}
		
			return resultsFinal;
		
		}


	
	public static String generateAllIdUsers(String idClan, String userName, Date date) throws IOException {
		List<String> listIdUser = new ArrayList<String>();
		////-- membres du clan 
		URL urlClan = null ;
		// recup des membres du clan NVS
		urlClan = null ;
			
		if(WotServiceImpl.lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
			urlClan = new URL(WotServiceImpl.proxy + "http://api.worldoftanks.eu/2.0/clan/info/?application_id=d0a293dc77667c9328783d489c8cef73&clan_id="+idClan);				
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
			
			String accountName = null;
			for (DataCommunityClanMembers dataClanMember : listClanMembers) {
				for (DataCommunityMembers member : dataClanMember.getMembers()) {
					log.warning("membermember " + member.getAccount_name() + " " + member.getAccount_id() );
					accountName = member.getAccount_name();
					String idUser = member.getAccount_id();
					//log.warning("treatUser " + treatUser);
					if (userName != null && accountName  != null && userName.equalsIgnoreCase(accountName))
						listIdUser.add(idUser);

					
					//si on aprécisé un username, on ne veut alors que l'ID de ce USER
					if (userName != null && accountName  != null && userName.equalsIgnoreCase(accountName))
						break ;

					
					
				}
				//si on aprécisé un username, on ne veut alors que l'ID de ce USER
				if (userName != null && accountName  != null && userName.equalsIgnoreCase(accountName))
					break ;


			}//for (DataCommunityClanMembers
		} else {

			log.severe("Erreur de parse");
		}
		////
		String AllIdUser ="";
		for(String idUser :listIdUser) {
			if ("".equalsIgnoreCase(AllIdUser)) 
				AllIdUser = idUser;
			else
				AllIdUser = AllIdUser + "," + idUser;
				
		}
		
		return AllIdUser;
	}
	


}
