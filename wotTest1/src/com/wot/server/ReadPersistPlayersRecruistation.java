package com.wot.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Text;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.CommunityAccount;

@SuppressWarnings("serial")
public class ReadPersistPlayersRecruistation extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(WotServiceImpl.class.getName());
	
	private static	List<String> listMembersAdded = null;
	private static	List<String> listMembersDeleted = null;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	log.warning("========lancement doGet  ReadPersistPlayersRecruistation ============== " );
        resp.setContentType("text/html");
       String nbJours = req.getParameter("NbJours");
       String dateUpdate = "";
       
       if (nbJours == null || "".equalsIgnoreCase(nbJours) ) 
    		   nbJours = "5"; 
        
        if(true ) {
        	
        	log.warning("========lancement ReadPersistPlayersRecruistation ============== "  + Long.valueOf(nbJours));
        	
        	//lecture de la composition du clan en base wotachievement et seulement une fois 
        	if (true ) {
        		listMembersAdded = new ArrayList<String>();
        		listMembersDeleted = new ArrayList<String>();

         		PersistenceManager pm = null;
        		pm = PMF.get().getPersistenceManager();
        		
                try {
        			Query query = pm.newQuery(DaoRecruistation.class);
        		    //query.setFilter("idClan == nameParam");
        		    //query.setOrdering("name desc");
        		    query.setOrdering("date desc");
        		    query.setRange(0, Long.valueOf(nbJours)); //only nbJours results 
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
        		    	
        		    	
//        		    	Date jLastDaoDate = jLastDaoRecruistation.getDate();
//        		    	mydate.setTimeInMillis(jLastDaoDate.getTime());
//        		    	log.warning("Date de mise à jour :" + mydate.get( + Calendar.DAY_OF_MONTH)+"."+mydate.get(Calendar.MONTH)+"."+mydate.get(Calendar.YEAR));
//        		    	dateUpdate =  mydate.get( + Calendar.DAY_OF_MONTH)+ "/"+mydate.get(Calendar.MONTH)+"/"+mydate.get(Calendar.YEAR) + " " + mydate.get(Calendar.HOUR) + ":" + mydate.get(Calendar.MINUTE) ;

        		    	
        		    	
        		    	
      		    	//recup des users du bureau de R pour les 2 jours 
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
        	if (true ) { 
        		String userNameAdded = "";
        		String userNameDeleted = "";
        		
				for ( String members :listMembersAdded) {
					
					userNameAdded = userNameAdded + " " + members +"<BR>";
				}
				for ( String members :listMembersDeleted) {
					
					userNameDeleted = userNameDeleted + " " + members +"<BR>";
				}
        		
        		StringBuffer strBuf = new StringBuffer();
        		//<html
        		strBuf.append("<HTML>");
        		strBuf.append("<BODY bgcolor='#FFFFFF'>"); //fond blanc de l'iframe
 
        		String userAddedCodeColor= "#6d9521"; //vert;
        		String userDeletedCodeColor= "#cd3333"; //rouge;
        		

        		strBuf.append("<TABLE width='150' border bgcolor='" + userAddedCodeColor + "' style='color:white;' >").
        					//ent�tes des colonnes
			        		append("<TR>").
								append("<TH>").
									append("Entrées de Joueurs dans BR : " + dateUpdate).
								append("</TH>").
							append("</TR>").
        					append("<TR>").
        						append("<TD>").
        							append(userNameAdded).
        						append("</TD>").
        					append("</TR>").
        				append("</TABLE>");
        		//== WR
        		strBuf.append("<TABLE width='150' border bgcolor='" + userDeletedCodeColor + "' style='color:white;' >").
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
        	
		}else {
			log.warning("WARNING: =======lancement ReadPersistPlayersRecruistation  with nbJours =" + nbJours );
		}
    }

}
