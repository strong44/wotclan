package com.wot.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.wot.server.api.TransformDtoObject;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.CommunityAccount;
import com.wot.shared.CommunityClan;
import com.wot.shared.DataCommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.DataCommunityMembers;

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
        		double wr = (double)commAcc.getData().getStatistics().getAllStatistics().getWins()/(double)commAcc.getData().getStatistics().getAllStatistics().getBattles();
        		StringBuffer strBuf = new StringBuffer();
        		//<html
        		strBuf.append("<HTML>");
        		strBuf.append("<BODY bgcolor='#FFFFFF'>"); //fond blanc de l'iframe
        		//codes couleurs
        		//below 300 BAD
        		String wn8CodeColor= "";
        		String wrCodeColor= "";
        		
        		if (wn8 <= 300 ) 
        			wn8CodeColor = "#000000";// couleur black du fond de la cellule
        		else
        			if (wn8 <= 599 )
        				wn8CodeColor = "#cd3333"; //rouge
        			else
        				if (wn8 <= 899 )
            				wn8CodeColor = "#d77900"; //orange
        				else
            				if (wn8 <= 1249 )
                				wn8CodeColor = "#d7b600"; //jaune
            				else
                				if (wn8 <= 1599 )
                    				wn8CodeColor = "#6d9521"; //vert
                				else
                    				if (wn8 <= 1899 )
                        				wn8CodeColor = "#4c762e"; //vert foncé
                    				else
                        				if (wn8 <= 2349 )
                            				wn8CodeColor = "#4a92b7"; //bleu
                        				else
                            				if (wn8 <= 2899 )
                                				wn8CodeColor = "#83579d"; //violet
                            				else
                                				if (wn8 >= 2900 )
                                    				wn8CodeColor = "#5a3175"; //violet foncé
        			
        		
        		if (wr <= 0.45 ) 
        			wrCodeColor = "#cd3333";// couleur rouge du fond de la cellule
        		else
        			if (wr <= 0.45 )
        				wrCodeColor = "#cd3333"; //rouge
        			else
        				if (wr <= 0.47 )
        					wrCodeColor = "#d77900"; //orange
        				else
            				if (wr <= 0.49 )
            					wrCodeColor = "#d7b600"; //jaune
            				else
                				if (wr <= 0.52 )
                					wrCodeColor = "#6d9521"; //vert
                				else
                    				if (wr <= 0.54 )
                    					wrCodeColor = "#4c762e"; //vert foncé
                    				else
                        				if (wr <= 0.56 )
                        					wrCodeColor = "#4a92b7"; //bleu
                        				else
                            				if (wr <= 0.60 )
                            					wrCodeColor = "#83579d"; //violet
                            				else
                                				if (wr > 0.60)
                                					wrCodeColor = "#5a3175"; //violet foncé
        		//== WN8
        		strBuf.append("<TABLE border bgcolor='" + wn8CodeColor + "' style='color:white;' >").
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
        		//== WR
        		strBuf.append("<TABLE border bgcolor='" + wrCodeColor + "' style='color:white;' >").
				//entêtes des colonnes
        		append("<TR>").
					append("<TH>").
						append("WR").
					append("</TH>").
				append("</TR>").
				append("<TR>").
					append("<TD>").
						append(wr*100).
					append("</TD>").
				append("</TR>").
			append("</TABLE>");
        		
        		strBuf.append("</BODY>");
        		strBuf.append("</HTML>");
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
