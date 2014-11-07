package com.wot.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
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

import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.CommunityAccount;
import com.wot.shared.DataTankEncyclopedia;
import com.wot.shared.TankEncyclopedia;

@SuppressWarnings("serial")
public class ReadPersistPlayersRecruistation extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(WotServiceImpl.class.getName());
	
	private static	List<String> listMembersAdded = null;
	private static	List<String> listMembersDeleted = null;
	private static	HashMap<String, String> hmMembersWn8Added = null;
	
	private static String applicationIdEU = "d0a293dc77667c9328783d489c8cef73";
	private static String urlServerEU =  "http://api.worldoftanks.eu";

	
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
        	
        	log.warning("========lancement ReadPersistPlayersRecruistation ============== "  + " Param from : " + paramFrom + " nbSave to read : " + Long.valueOf(nbSave));
        	
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
        		    	log.warning("Date of the day :" + mydate.get(Calendar.DAY_OF_MONTH)+"."+mydate.get(Calendar.MONTH)+"."+mydate.get(Calendar.YEAR));
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
	        		    		log.warning(mydate.get( + Calendar.DAY_OF_MONTH)+ "/"+mydate.get(Calendar.MONTH)+"/"+mydate.get(Calendar.YEAR) + " " + mydate.get(Calendar.HOUR) + ":" + mydate.get(Calendar.MINUTE)) ;
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
        		    	log.warning("========lancement ReadPersistPlayersRecruistation ============== jLastUsers"  + jLastUsers);
        		    	log.warning("========lancement ReadPersistPlayersRecruistation ============== jFirstUsers"  + jFirstUsers);
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
    		    		dateLastUpdate = mydate.get( + Calendar.DAY_OF_MONTH)+ "/"+mydate.get(Calendar.MONTH)+"/"+mydate.get(Calendar.YEAR) + " " + mydate.get(Calendar.HOUR_OF_DAY) + ":" + mydate.get(Calendar.MINUTE) ;
    		    		
    		    		mydate.setTime(jFirstDaoRecruistation.getDate());
    		    		dateFirstUpdate = mydate.get( + Calendar.DAY_OF_MONTH)+ "/"+mydate.get(Calendar.MONTH)+"/"+mydate.get(Calendar.YEAR) + " " + mydate.get(Calendar.HOUR_OF_DAY) + ":" + mydate.get(Calendar.MINUTE) ;
    		    		
   
        		    	
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
    		
			for ( String member :listMembersAdded) {
				
				//get wn8
				String statUser = getWn8(member, "WN8");
				
				
				String aUrl = "<a href=\"http://wotlabs.net/eu/player/" + member + "\"" + " title=\"" + statUser +"\" target=\"_blank\">" + member +"</a>";
				userNameAdded = userNameAdded + "&nbsp" + aUrl + "&nbsp" + statUser +"<BR>";
				//userNameAdded = userNameAdded + "&nbsp" + member + "&nbsp" + statUser + "&nbsp" + aUrl + "<BR>";
				hmMembersWn8Added.put(aUrl, statUser);
				
			}
			for ( String members :listMembersDeleted) {
				
				userNameDeleted = userNameDeleted + " " + members +"<BR>";
			}
    		
			//Voir aussi si on peut ajouter le WN8 des joueurs
			//http://wotlabs.net/eu/player/strong44
			/*
			 * <div class="boxStats boxWn green" style="width:18%;float:left;margin-right:2.5%;margin-bottom:25px;">
				‌¶‌→
				<span>WN8</span>
				‌¶‌→1149‌→
				</div>
			 */
			
			if ("".equalsIgnoreCase(userNameAdded) )
				userNameAdded = "pas de joueur <BR>";

			if ("".equalsIgnoreCase(userNameDeleted) )
				userNameDeleted = "pas de joueur <BR>";

			
    		StringBuffer strBuf = new StringBuffer();
    		//<html
    		strBuf.append("<HTML>");
    		strBuf.append("<BODY bgcolor='#FFFFFF'>"); //fond blanc de l'iframe
 
        		String userAddedCodeColor= "#6d9521"; //vert;
    		String userDeletedCodeColor= "#cd3333"; //rouge;
    		

    		strBuf.append("<TABLE border bgcolor='" + userAddedCodeColor + "' style='color:white;' >").
    					//ent�tes des colonnes
		        		append("<TR >").
							append("<TH width='200'>").
								append("Entrées de Joueurs dans BR entre le  ").
							append("</TH>").
						
							append("<TH width='200'>").
								append(dateLastUpdate + " et le " + dateFirstUpdate).
							append("</TH>").
						
						append("</TR>").
						//2ème ligne d'entête 
						append("<TR>").
							append("<TH>").
								append("Joueurs").
							append("</TH>").
							append("<TH>").
								append("Stats" ).
							append("</TH>")
						.append("</TR>");
						
    		Set<Entry<String,String>> setEntry = hmMembersWn8Added.entrySet();
    		
    		for (Entry<String,String> entry :setEntry ) {
    			strBuf.append("</TR>").
				append("<TR>").
					append("<TD>").
						append(entry.getKey()).
					append("</TD>").
					append("<TD>").
						append(entry.getValue()).
					append("</TD>").
				append("</TR>");
		
    		}
			strBuf.append("</TABLE>");
    		
    		
    		//== WR
    		strBuf.append("<TABLE width='400' border bgcolor='" + userDeletedCodeColor + "' style='color:white;' >").
			//ent�tes des colonnes
    		append("<TR>").
				append("<TH>").
					append("Sorties de joueurs du BR").
				append("</TH>").
			append("</TR>").
			append("<TR>").
				append("<TD>").
					append(userNameDeleted).
				append("</TD>").
			append("</TR>").
			append("</TABLE>");
    		
    		
    		
    		
    		strBuf.append("</BODY>");
    		strBuf.append("</HTML>");
    		String buf= strBuf.toString();
    		resp.getWriter().println(buf);
    	}
    	
		
    }
    
    
    
	public static String getWn8(String member, String keyStat) throws IOException {
		//=======================
		/*
		 * 	WN8 805
 			Win Rate 48.2%
 			Recent WN8 785
 			Recent WR 44.64%
 			WN Rank 260966

		 */
		String res = "";
		
		//http://api.worldoftanks.eu/2.0/encyclopedia/tanks/?application_id=d0a293dc77667c9328783d489c8cef73
		String urlServer = "http://wotlabs.net/eu/player/" + member ;
		URL url = null;
		
		if(WotServiceImpl.lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
			url = new URL(WotServiceImpl.proxy + urlServer );
		}
		else {
			url = new URL(urlServer );
		}
		
		////////////////////
		//On se connecte au site et on charge le document html
		Connection connection = Jsoup.connect(urlServer);
		connection.timeout(30*1000); //in miliseondes
		
		Document doc = connection.url(urlServer).get();
		
		//On récupère dans ce document la premiere balise ayant comme nom h1 et pour attribut class="title"
		Elements elements= doc.getElementsByClass("boxStats");
		
		//Voir aussi si on peut ajouter le WN8 des joueurs
		//http://wotlabs.net/eu/player/strong44
		/*
		 * <div class="boxStats boxWn green" style="width:18%;float:left;margin-right:2.5%;margin-bottom:25px;">
			‌¶‌→
			<span>WN8</span>
			‌¶‌→1149‌→
			</div>
		 */
		for (Element ele : elements ) {
			log.warning(ele.text());
			if (ele.text() != null && ele.text().toLowerCase().contains(keyStat.toLowerCase())) {
				if ("".equalsIgnoreCase(res) )
					res =  ele.text();
				else
					res =  res + " : " + ele.text();
			}
		}
		
		
		return res ;
		
		
	}

}
