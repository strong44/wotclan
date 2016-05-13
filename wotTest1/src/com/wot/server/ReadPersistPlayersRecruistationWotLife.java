package com.wot.server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wot.client.WotService;
import com.wot.client.WotServiceAsync;
import com.wot.shared.CommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.DataCommunityMembers;

@SuppressWarnings("serial")
public class ReadPersistPlayersRecruistationWotLife extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(WotServiceImpl.class.getName());
	
	private static	List<String> listMembersAdded = null;
	private static	List<String> listMembersDeleted = null;
	private static	HashMap<String, String> hmMembersWn8Added = null;
	
	private static String urlServer = "http://www.noobmeter.com/player/eu/ " ;
	private static String urlServerWotLife = "https://fr.wot-life.com/eu/player/" ;
	private static String urlServerWotForum = "http://nova.snail.soforums.com/profile.php?mode=viewprofile&u=" ;
	
	private static 	HashMap<String , String> hmStatWn8Member = new HashMap<String, String>();
 
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	log.warning("========lancement doGet  ReadPersistPlayersRecruistation ============== " );
        resp.setContentType("text/html");
        
        //paramétre from=lastSAve : http://wotachievement.appspot.com/readPersistPlayersRecruistation?from=lastSave
        //Recupère les recrues entre les 2 derniers point de sauvegarde
        //
        //from=lastDay
        //Recupère les recrues de tous les points de sauvegarde de la dernière journée
        
       String paramFrom = req.getParameter("from");
       
       String dateLastUpdate = "";
       String dateFirstUpdate = "";
       String nbSave = "2";
       
       //valeur par défaut du paramètre from = lastSave
       if (paramFrom == null || "".equalsIgnoreCase(paramFrom) )  
    	   paramFrom = "lastSave"; 
       
       //en fonction de la valeur du paramètre from lastSave ou lastDay on récupère plus ou moins de sauvegarde <nbSave> (avec comme max 10 pour une journée !) 
       
       if (paramFrom.equalsIgnoreCase("lastSave")) {
    	   nbSave = "2";
       }else if (paramFrom.equalsIgnoreCase("lastDay")) {
    	   nbSave = "10";
       }
       
       
        if(true ) {
        	
        	//log.warning("========lancement ReadPersistPlayersRecruistation ============== "  + " Param from : " + paramFrom + " nbSave to read : " + Long.valueOf(nbSave));
        	
        	//lecture de la composition du clan en base wotachievement et seulement une fois 
        	if (true ) {
        		listMembersAdded = new ArrayList<String>();
        		listMembersDeleted = new ArrayList<String>();
        		hmMembersWn8Added = new HashMap<String, String>();
        		
         		PersistenceManager pm = null;
        		pm = PMF.get().getPersistenceManager();
        		
                try {
        			Query query = pm.newQuery(DaoRecruistation.class);
        		    //query.setFilter("idClan == nameParam");
        		    //query.setOrdering("name desc");
        		    query.setOrdering("date desc");
        		    query.setRange(0, Long.valueOf(nbSave)); //only nbJours results 
        		    //query.setOrdering("hireDate desc");
        		    //query.declareParameters("String nameParam");
        		    List<DaoRecruistation> resultsTmp = (List<DaoRecruistation>) query.execute();
        		    
        		  //recup des membres des nbJours derniers jours
        		    if(resultsTmp.size() >= 1  )
        		    {       
        		    	//on prend le premier et le dernier jour
        		    	int size = resultsTmp.size();
        		    	
        		    	//Rechercher la sauvegarde du jour d'avant j-1
         		    	Calendar mydate = Calendar.getInstance();
        		    	log.warning("Date of the day :" + mydate.get(Calendar.DAY_OF_MONTH)+"."+ (mydate.get(Calendar.MONTH)+1)+"."+mydate.get(Calendar.YEAR));
        		    	Date dateToday = mydate.getTime();
          		    	
        		    	DaoRecruistation jLastDaoRecruistation =  resultsTmp.get(0); //le dernier jour sauvegardé  
        		    	DaoRecruistation jFirstDaoRecruistation =  resultsTmp.get(size-1); //le premier jour sauvegardé
        		    	
        		    	//si on veut toutes les dernieres sauvegarde de la journée
        		    	//on recherche la sauvegarde vieille de 1 jour
        		    	if(paramFrom.equalsIgnoreCase("lastDay")) {
	        		    	//milliseconds for one day
	        		    	// 1000 * 1s * 60s * 60mn * 24 
	        		    	long milliSecondsDay = 1000 *60 * 60 * 24 ;
	        		    	
	        		    	for (DaoRecruistation dao : resultsTmp) {
	        		    		
	        		    		long timeDateDao = dao.getDate().getTime() ;
	        		    		mydate.setTime(dao.getDate());
	        		    		log.warning(mydate.get( + Calendar.DAY_OF_MONTH)+ "/"+(mydate.get(Calendar.MONTH)+1)+"/"+mydate.get(Calendar.YEAR) + " " + mydate.get(Calendar.HOUR) + ":" + mydate.get(Calendar.MINUTE)) ;
	        		    		//trouver l'enregistrement du jour d'avant 
	        		    		if (timeDateDao <= (dateToday.getTime() - milliSecondsDay) ) {
	        		    			jFirstDaoRecruistation = dao;
	        		    			break ;
	        		    		}
	        		    	}
        		    	}
        		    	
        		    	
        		    	//recup des users du bureau de Recrutement   
        		    	String jLastUsers =  jLastDaoRecruistation.getUsers().getValue();
        		    	String jFirstUsers =  jFirstDaoRecruistation.getUsers().getValue();
        		    	//log.warning("========lancement ReadPersistPlayersRecruistation ============== jLastUsers"  + jLastUsers);
        		    	//log.warning("========lancement ReadPersistPlayersRecruistation ============== jFirstUsers"  + jFirstUsers);
        		    	//on split la chaine users en tableau 
        		    	String tabJLastUsers[] = jLastUsers.split(",");
        		    	String tabJFirstUsers[] = jFirstUsers.split(",");
        		    	
        		    	//convertir des tableau en liste
        		    	List<String> listJlastUsers = Arrays.asList(tabJLastUsers); //dernier jour (+ recent )
        		    	List<String> listJFirstUsers = Arrays.asList(tabJFirstUsers); //premier jour (+ ancien) 
        		    	
        		    	//nouvelles recrues
        		    	for (String jLastuser : listJlastUsers) {
        		    		
        		    		//si la liste du jour plus ancien ne contient le nom de joueur de la liste recent c'est une nouvelle recrue 
        		    		if (jLastuser != null && !"".equalsIgnoreCase(jLastuser) &&  !"Joueur".equalsIgnoreCase(jLastuser) && !listJFirstUsers.contains(jLastuser))
        		    		{
        		    			listMembersAdded.add(jLastuser);
        		    		}
        		    	}
        		    	
        		    	//départ recrues 
        		    	for (String jFirstUser : listJFirstUsers) {
        		    		
        		    		//si la liste du jour plus ancien ne contient le nom de joueur de la liste recent c'est une nouvelle recrue 
        		    		if (jFirstUser != null && !"".equalsIgnoreCase(jFirstUser) &&  !"Joueur".equalsIgnoreCase(jFirstUser) && !listJlastUsers.contains(jFirstUser))
        		    		{
        		    			listMembersDeleted.add(jFirstUser);
        		    		}
        		    	}
        		    	//format date 
        		    	//long timeDate = jLastDaoRecruistation.getDate().getTime() ;
    		    		mydate.setTime(jLastDaoRecruistation.getDate());
    		    		dateLastUpdate = mydate.get( + Calendar.DAY_OF_MONTH)+ "/"+(mydate.get(Calendar.MONTH)+1)+"/"+mydate.get(Calendar.YEAR) + " " + mydate.get(Calendar.HOUR_OF_DAY) + ":" + mydate.get(Calendar.MINUTE) ;
    		    		
    		    		mydate.setTime(jFirstDaoRecruistation.getDate());
    		    		dateFirstUpdate = mydate.get( + Calendar.DAY_OF_MONTH)+ "/"+(mydate.get(Calendar.MONTH)+1)+"/"+mydate.get(Calendar.YEAR) + " " + mydate.get(Calendar.HOUR_OF_DAY) + ":" + mydate.get(Calendar.MINUTE) ;
    		    		
   
        		    	
        		    }else {
        		    	log.warning("========lancement ReadPersistPlayersRecruistation ============== no result " );
        		    }
                }
        	    catch(Exception e){
        	    	e.printStackTrace();
        	    	log.log(Level.SEVERE, "Exception while quering  DaoRecruistation", e);
                	pm.currentTransaction().rollback();
                }
        	}
        	
        	
        	//build a HTML result
    		String userNameAdded = "";
    		String userNameDeleted = "";
    		
    		String statUser = "";
    		List<String> listStatUser = new ArrayList<String>();
    		listStatUser.add("WN8");
    		listStatUser.add("Battles");
    		//listStatUser.add("Tanks");
    		
    		List<String> listStatUserDetailed = new ArrayList<String>();
    		listStatUserDetailed.add("Total");
    		listStatUserDetailed.add("24 heures");
    		listStatUserDetailed.add("7 jours");
    		listStatUserDetailed.add("30 jours");
    		
    		
    		
			for ( String member :listMembersAdded) {
				
				//get wn8
				statUser = "";
				if (!hmStatWn8Member.containsKey(member)) {
					statUser = getStatsWotLife(member, listStatUser);
					//on mémorise dans un cache les stats  
					hmStatWn8Member.put(member, statUser);
				}else {
					//on recup du cache
					statUser = hmStatWn8Member.get(member);
				}

				String aUrl = "<a href=\"https://fr.wot-life.com/eu/player/" + member + "\"" + " title=\"" + member +"\" target=\"_blank\">" + member +"</a>";
				userNameAdded = userNameAdded + "&nbsp" + aUrl + "&nbsp" + statUser +"<BR>";
				//userNameAdded = userNameAdded + "&nbsp" + member + "&nbsp" + statUser + "&nbsp" + aUrl + "<BR>";
				hmMembersWn8Added.put(aUrl, statUser);
				
			}
			for ( String members :listMembersDeleted) {
				
				userNameDeleted = userNameDeleted + " " + members +"<BR>";
			}
    		
			if ("".equalsIgnoreCase(userNameAdded) )
				userNameAdded = "pas de joueur <BR>";

			if ("".equalsIgnoreCase(userNameDeleted) )
				userNameDeleted = "pas de joueur <BR>";

			
    		StringBuffer strBuf = new StringBuffer();
    		//<html
    		strBuf.append("<HTML>");
    		strBuf.append("<BODY bgcolor='#FFFFFF'>"); //fond blanc de l'iframe
 
        	String userAddedCodeColor= "#6d9521"; //vert;
    		//String userDeletedCodeColor= "#cd3333"; //rouge;
        	
        	//style couleur gris clair
    		String styleColorWN8 = " style = 'color: white;background-color: rgb(105, 105, 105);' ";
    		
        	//style couleur gris plus foncé
    		String styleColorBattles = " style = 'color: white;background-color: rgb(130, 130, 130);' ";

    		String styleColorContact = " style='color: black;background-color: rgb(255, 255, 255);' ";
    		
    		String styleColorHeader = " style='color: black;background-color: rgb(60, 60, 60);' ";
    		
    		String styleColorPlayer = " style='color: black;background-color: rgb(50, 105, 105);' ";
    		
        	//tableau noir
    		strBuf.append("<TABLE border  " + " style='color: white;background-color: rgb(5, 5, 5);' >").
    					//entêtes des colonnes
		        		append("<TR >").
							append("<TH colspan=\"11\" >").
								append("Nouveaux joueurs candidats entre le  ").
								append(dateLastUpdate ).
								append(" et le " + dateFirstUpdate).
							append("</TH>").

				    		//colonnes contacté ...
				    		append("<TH" + styleColorContact+  ">").
			    			append("Contacté (O/N)").
							append("</TH>");
				    		
    						strBuf.append("<TH" + styleColorContact+  ">").
			    			append("A répondu (O/N)").
							append("</TH>");

    						strBuf.append("<TH" + styleColorContact+  ">").
			    			append("Sa réponse").
							append("</TH>");

    						strBuf.append("<TH" + styleColorContact+  ">").
			    			append("Invité (O/N)").
							append("</TH>").
						append("</TR>").
						
						// ===== 2ème ligne d'entête Joueurs WN8 Battles tanks ===== 
						append("<TR>").
							append("<TH"+ styleColorHeader + ">").
								append("Joueurs").
							append("</TH>");
				    		
    					//listStatUser WN8 + battles 
			    		strBuf.append("<TH colspan=\"4\"" + styleColorHeader + ">").
			    		append("WN8").
						append("</TH>");
			    		
			    		strBuf.append("<TH colspan=\"4\"" + styleColorHeader + ">").
			    		append("Battles").
						append("</TH>");

			    		//colonne tanks
			    		strBuf.append("<TH colspan=\"2\"" + styleColorHeader + ">").
		    			append("Tanks").
						append("</TH>");

			    		//4 cellules vides d'entêtes
			    		strBuf.append("<TH" + styleColorContact+ ">").
						append("<BR>"). 
						append("</Th>");
			    		
			    		strBuf.append("<TH" + styleColorContact+ ">").
						append("<BR>"). 
						append("</Th>");

			    		strBuf.append("<TH" + styleColorContact+ ">").
						append("<BR>"). 
						append("</Th>");

			    		strBuf.append("<TH" + styleColorContact+ ">").
						append("<BR>"). 
						append("</TH>");
		    		
						strBuf.append("</TR>");
						
						//===== 3ème ligne d'entête Total - 24 heures ...======
						strBuf.append("<TR>").
						append("<TH"+ styleColorHeader + ">").
							append("").//colonne joueur 
						append("</TH>");
						//listStatUser WN8 Total .. Depuis 30 jours
			    		for (String stat  : listStatUserDetailed) {
			    			strBuf.append("<TH"+ styleColorHeader + ">").
		    				append(stat).
		    				append("</TH>");
			    		}
			    		
						//listStatUser batailles Total .. Depuis 30 jours
			    		for (String stat  : listStatUserDetailed) {
		    				strBuf.append("<TH " + styleColorHeader + ">").
		    				append(stat).
		    				append("</TH>");
			    		}
			    		//tank T6
			    		strBuf.append("<TH " + styleColorHeader + ">").
		    			append("Tanks T6").
						append("</TH>");
			    		
			    		//tank T8
			    		strBuf.append("<TH " + styleColorHeader + ">").
		    			append("Tanks T8").
						append("</TH>");
						
						
						//4 cellules vides
			    		strBuf.append("<TH " + styleColorContact + ">").
		    			append("<BR>").
						append("</TH>");

			    		strBuf.append("<TH " + styleColorContact + ">").
		    			append("<BR>").
						append("</TH>");

			    		strBuf.append("<TH " + styleColorContact + ">").
		    			append("<BR>").
						append("</TH>");

			    		strBuf.append("<TH " + styleColorContact + ">").
		    			append("<BR>").
						append("</TH>");

						strBuf.append("</TR>");
						
						
    		Set<Entry<String,String>> setEntryUsersAdded = hmMembersWn8Added.entrySet();
    		//==== lignes de stats des joueurs ======== 
    		for (Entry<String,String> entryUser :setEntryUsersAdded ) {
    			//strBuf.append("</TR>").
    			strBuf.append("<TR>").
					append("<TD"+ styleColorPlayer +">").
						append(entryUser.getKey()).//nom du joueur 
					append("</TD>").
					append(entryUser.getValue().replaceAll("style='WN8'", styleColorWN8).replaceAll("style='Battles'", styleColorBattles)).//les stats WN8 +Battles
					//4 cellules vides 
					append("<TD" + styleColorContact+ ">").
					append("<BR>"). 
					append("</TD>").

					append("<TD" + styleColorContact+ ">").
					append("<BR>"). 
					append("</TD>").

					append("<TD" + styleColorContact+ ">").
					append("<BR>"). 
					append("</TD>").

					append("<TD" + styleColorContact+ ">").
					append("<BR>"). 
					append("</TD>").

				append("</TR>");
		
    		}
			strBuf.append("</TABLE>");

			//traitement de recup d'infos sur forum
			ArrayList<String> listUserForum = new ArrayList<String>();
			listUserForum.add("samuel52");
			listUserForum.add("strong44");
			
			
			
			//getAllMembersClan(String idClan)
			//renseigner les users des hashMap si NULL ou vide
//			if (WotServiceImpl.hMapUserNameId != null && WotServiceImpl.hMapUserNameId.size()> 0){
//				//String clan_id= "500006074";
//				////////////
//				strBuf.append("<TABLE>");
//				Set<Entry<String, String>> setUserId = WotServiceImpl.hMapUserNameId .entrySet();
//				
//				for (Entry<String, String> entryUserId : setUserId) {
//					String res = getStatsWotForum(entryUserId.getKey(), "Localisation");
//					
//					strBuf.append("<TR>");
//						strBuf.append("<TD>");
//						strBuf.append(res);
//						strBuf.append("</TD>");
//					strBuf.append("</TR>");
//				}
//				strBuf.append("</TABLE>");
//			}
			
			////////////
			//
    		strBuf.append("</BODY>");
    		strBuf.append("</HTML>");
    		String buf= strBuf.toString();
    		resp.getWriter().println(buf);
    	}
    	
		
    }
    
    
    
	public static String getStatsWotLife(String member, List<String> listKeyStat) throws IOException {
		String res = "";
		String urlServerLocal = urlServerWotLife + member ;
		
		
		////////////////////
		//On se connecte au site et on charge le document html
		Connection connection = Jsoup.connect(urlServerLocal);
		connection.timeout(30*1000); //in miliseondes
		
		Document doc = connection.url(urlServerLocal).get();
		
		//On récupère dans ce document la premiere balise de type HEADER et portant le nom <Nombre de batailles>
		/*
		 * <table class="stats-table table-md">
	    <tr><td style="width: 180px;"></td>
	      <th colspan="2">Total</th>
	      <th colspan="2">Depuis 24 heures</th>
	      <th colspan="2">Depuis 7 jours</th>
	      <th colspan="2">Depuis 30 jours</th>
	    </tr>
	    <tr>
	      <th>Nombre de batailles</th>
	      <td colspan="2" class="text-right">15686</td>
	      <td colspan="2" class="text-right">0</td>
	      <td colspan="2" class="text-right">72</td>
	      <td colspan="2" class="text-right">357</td>
		 */
		//---WN8--- 
		Elements elementsTablesorter= doc.getElementsByTag("th");
		for (Element elementTableSorter : elementsTablesorter ) {
			
			
			if (elementTableSorter.text().contains("WN8")) {
				System.out.println("WN8");
				//on peut rechercher le WN8
				//Elements elementsTD= elementTableSorter.getElementsByTag("td");
				Element elementTD= elementTableSorter.nextElementSibling();
				if ("".equalsIgnoreCase(res) )
					//res =  "<td>" + keyStatMem + ":" +eleSorter.text() +"</td>" ;
					res =  "<td style='WN8'>" +elementTD.text() +"</td>" ;
				else
					res =  res + "<td style='WN8'>"  +elementTD.text() + "</td>";
				//
				elementTD= elementTD.nextElementSibling();
				if ("".equalsIgnoreCase(res) )
					//res =  "<td>" + keyStatMem + ":" +eleSorter.text() +"</td>" ;
					res =  "<td style='WN8'>" +elementTD.text() +"</td>" ;
				else
					res =  res + "<td style='WN8'>"  +elementTD.text() + "</td>";
				//
				//
				elementTD= elementTD.nextElementSibling();
				if ("".equalsIgnoreCase(res) )
					//res =  "<td>" + keyStatMem + ":" +eleSorter.text() +"</td>" ;
					res =  "<td style='WN8'>" +elementTD.text() +"</td>" ;
				else
					res =  res + "<td style='WN8'>"  +elementTD.text() + "</td>";
				//
				//
				elementTD= elementTD.nextElementSibling();
				if ("".equalsIgnoreCase(res) )
					//res =  "<td>" + keyStatMem + ":" +eleSorter.text() +"</td>" ;
					res =  "<td style='WN8'>" +elementTD.text() +"</td>" ;
				else
					res =  res + "<td style='WN8'>"  +elementTD.text() + "</td>";
				//
				break;
			}
		}
		if (res.isEmpty()) {
			res =  "<td style='WN8'>"  +"USER NOT FOUND " + "</td>";
			return res ;
		}
		
		//---nombre de batailles--- 
		elementsTablesorter= doc.getElementsByTag("th");
		for (Element elementTableSorter : elementsTablesorter ) {
			//System.out.println("th:" + elementTableSorter.text());
			
			if (elementTableSorter.text().contains("Nombre de batailles")) {
				
				//on peut rechercher le nb de batailles
				Element elementTD= elementTableSorter.nextElementSibling();
				if ("".equalsIgnoreCase(res) )
					res =  "<td style='Battles'>" +elementTD.text() +"</td>" ;
				else
					res =  res + "<td style='Battles'>"  +elementTD.text() + "</td>";
				//
				elementTD= elementTD.nextElementSibling();
				if ("".equalsIgnoreCase(res) )
					//res =  "<td>" + keyStatMem + ":" +eleSorter.text() +"</td>" ;
					res =  "<td style='Battles'>" +elementTD.text() +"</td>" ;
				else
					res =  res + "<td style='Battles'>"  +elementTD.text() + "</td>";
				//
				//
				elementTD= elementTD.nextElementSibling();
				if ("".equalsIgnoreCase(res) )
					//res =  "<td>" + keyStatMem + ":" +eleSorter.text() +"</td>" ;
					res =  "<td style='Battles'>" +elementTD.text() +"</td>" ;
				else
					res =  res + "<td style='Battles'>"  +elementTD.text() + "</td>";
				//
				//
				elementTD= elementTD.nextElementSibling();
				if ("".equalsIgnoreCase(res) )
					//res =  "<td>" + keyStatMem + ":" +eleSorter.text() +"</td>" ;
					res =  "<td style='Battles'>" +elementTD.text() +"</td>" ;
				else
					res =  res + "<td style='Battles'>"  +elementTD.text() + "</td>";
				//
				break;
			}
		}
		
		//---les chars -----
		/*
		 * <div id="tanks_wrapper" class="dataTables_wrapper no-footer">
			<table id="tanks" class="row-table dataTable no-footer dtr-inline collapsed" style="width: 100%" role="grid">
			<thead>
			<tbody>
			<tr class="odd" role="row">
			<tr class="odd" role="row">
				<td class="control" tabindex="0"></td>
				<td class=" all">
				<td class=" all">KV-85</td>
				<td data-sort="4">
				<td class="text-center" data-sort="5">
				<td class="text-center" data-sort="2">
				<td class="text-center">6</td>
				<td class="text-right">768,42</td>
				<td class="text-center">526</td>
				<td class="text-center sorting_1" style="display: none;">1767</td>
				<td class="text-center" style="display: none;">55,46%</td>
				<td class="text-center wn s5 all">1317,80</td>
			</tr>
		 */
		
		//-- liste des tanks
		Element elementTabTank= doc.getElementById("tanks");
		//index ---
		Integer indexNameTankInTable = 2;//nom du tank
		Integer indexTierTankInTable = 6;//tier du tank
		Integer indexWN8TankInTable = 11;//wn8 du tank
		
		String listTankT6 ="";
		String listTankT8 ="";
		
		if(elementTabTank.getElementsByTag("tr") != null ){
			for (Element elementTRTable : elementTabTank.getElementsByTag("tr") ) {
				Elements elementsTDTable = elementTRTable.getElementsByTag("td");
				if(elementsTDTable.size()> 10) {
					Element myElementTierTank = elementsTDTable.get(indexTierTankInTable);
					Element myElementNameTank = elementsTDTable.get(indexNameTankInTable);
					Element myElementWN8Tank = elementsTDTable.get(indexWN8TankInTable);
					
					Integer tierTank = Integer.valueOf (myElementTierTank.ownText());
					String nameTank = myElementNameTank.ownText();
					String wn8Tank = myElementWN8Tank.ownText();
					wn8Tank = "(" +wn8Tank + ")";
					
					if (tierTank == 6) {
						//Cz08 T 25
						if (nameTank.equalsIgnoreCase("ARL 44") || 
								nameTank.equalsIgnoreCase("KV-85") ||
								nameTank.equalsIgnoreCase("T37") ||
								nameTank.equalsIgnoreCase("Cromwell B") ||
								nameTank.equalsIgnoreCase("Cromwell") ||
								nameTank.equalsIgnoreCase("O-I") ||
								nameTank.equalsIgnoreCase("T-150") || 
								nameTank.equalsIgnoreCase("M6") ||
								nameTank.equalsIgnoreCase("59-16") ||
								nameTank.equalsIgnoreCase("Cz08 T 25") ||
								nameTank.equalsIgnoreCase("KV-2") ) {
							listTankT6 = listTankT6 + "<br>" + nameTank + wn8Tank;
						}
					}
	
					if (tierTank == 8) {
						if (nameTank.equalsIgnoreCase("IS-3") || 
								nameTank.equalsIgnoreCase("T32") ||
								nameTank.equalsIgnoreCase("AMX 50 100") ||
								nameTank.equalsIgnoreCase("T49") ||
								nameTank.equalsIgnoreCase("T34") ||
								nameTank.equalsIgnoreCase("KV-4") ||
								nameTank.equalsIgnoreCase("FCM 50 t") ||
								nameTank.equalsIgnoreCase("STA-1") ||
								nameTank.equalsIgnoreCase("WZ-111") ||
								nameTank.equalsIgnoreCase("110") ||
								nameTank.equalsIgnoreCase("ISU-152") ||
								nameTank.equalsIgnoreCase("WZ-132") ||
								nameTank.equalsIgnoreCase("Spähpanzer Ru 251") ||
								nameTank.equalsIgnoreCase("AMX 13 90") 
								 ) {
							listTankT8 = listTankT8 + "<br>" + nameTank+ wn8Tank;
						}
					}
	
					//System.out.println("Tier/name tank " + listTankT6);
				}
	
			}
		}
		res = res + "<td>"  +listTankT6 + "</td>" +"<td>"  +listTankT8 + "</td>";
		return res ;
		
		
	}



	public static String getStatsWotForum(String member, String key) throws IOException {
		String res = "";
		String urlServerLocal = urlServerWotForum + member ;
		
		
		////////////////////
		String url = null;
		
		if(WotServiceImpl.lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
			url = WotServiceImpl.proxy + urlServerLocal ;
		}
		else {
			url = urlServerLocal ;
		}
		
		
		//On se connecte au site et on charge le document html
		Connection connection = Jsoup.connect(url);
		connection.timeout(30*1000); //in miliseondes
		
		Document doc = connection.url(urlServerLocal).get();
		
		//---recherche de la balise TAG "voir le profil"   --- 
		Elements elementsTablesorter= doc.getElementsByTag("th");
		for (Element elementTableSorter : elementsTablesorter ) {
			
			//System.out.println(elementTableSorter.text());
			
			if (elementTableSorter.text().contains("Voir le profil")) {
				System.out.println("user :" + member + " found ");
				
				res =  "<td style='user'>" + member+ "</td>";
				//
				//user-id-304 profile-field-Date-20de-20naissance
				//Elements elementsClassSorter= doc.getElementsByClass("profile-field-Date-20de-20naissance");
				Elements elementsClassSorter= doc.getElementsByClass("profile-field-Localisation");
				if (elementsClassSorter!= null && elementsClassSorter.size()> 0) {
					res =  res + "<td style='user'>"  +key +"</TD><TD> " + elementsClassSorter.text()+ "</td>";
					break;
				}
				break;
			}
		}
		if (res.isEmpty()) {
			res =  "<td style='user'>"  +"USER NOT FOUND</TD><TD>" + member + "</td>";
			return res ;
		}
		return res ;
		
		
	}
	
	public static void main(String[] args) 
    {
        String s = 	"<div id='tanks_wrapper' class='dataTables_wrapper no-footer'>"+
			"<table id='tanks' class='row-table dataTable no-footer dtr-inline collapsed' style='width: 100%' role='grid'>"+
			"<thead>"+
			"<tbody>"+
			"<tr class='odd' role='row'>"+
			"<tr class='odd' role='row'>"+
				"<td class='control' tabindex='0'></td>"+
				"<td class=' all'>"+
				"<td class=' all'>KV-85</td>"+
				"<td data-sort='4'>"+
				"<td class='text-center' data-sort='5'>"+
				"<td class='text-center' data-sort='2'>"+
				"<td class='text-center'>6</td>"+
				"<td class='text-right'>768,42</td>"+
				"<td class='text-center'>526</td>"+
				"<td class='text-center sorting_1' style='display: none;'>1767</td>"+
				"<td class='text-center' style='display: none;'>55,46%</td>"+
				"<td class='text-center wn s5 all'>1317,80</td>"+
			"</tr></tbody></table></div>";

        	    
        Document doc = Jsoup.parse(s);
        //================================
		Element elementTabTank= doc.getElementById("tanks");
		String res ="";
		//index ---
		Integer indexNameTankInTable = 2;//nom du tank
		Integer indexTierTankInTable = 6;//tier du tank
		String listTankT6 =" ";
		String listTankT8 =" ";
		
		for (Element elementTRTable : elementTabTank.getElementsByTag("tr") ) {
			Elements elementsTDTable = elementTRTable.getElementsByTag("td");
			if(elementsTDTable.size()> 10) {
				Element myElementTierTank = elementsTDTable.get(indexTierTankInTable);
				Element myElementNameTank = elementsTDTable.get(indexNameTankInTable);
				Integer tierTank = Integer.valueOf (myElementTierTank.ownText());
				String nameTank = myElementNameTank.ownText();
				
				if (tierTank == 6) {
					listTankT6 = listTankT6 + nameTank;
				}

				if (tierTank == 8) {
					listTankT8 = listTankT8 + nameTank;
				}

				System.out.println("Tier/name tank " + listTankT6);
			}

		}
 
    }
	
}
