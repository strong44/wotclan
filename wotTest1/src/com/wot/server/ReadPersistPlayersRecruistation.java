package com.wot.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
       
       if (nbJours == null || "".equalsIgnoreCase(nbJours) ) 
    		   nbJours = "2"; 
        
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
        		    	DaoRecruistation jLastDaoRecruistation =  resultsTmp.get(0); //le dernier jour sauvegardé  
        		    	DaoRecruistation jFirstDaoRecruistation =  resultsTmp.get(size-1); //le premier jour sauvegardé
        		    	
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
        		    		if (!listJFirstUsers.contains(jLastuser))
        		    		{
        		    			listMembersAdded.add(jLastuser);
        		    		}
        		    	}
        		    	
        		    	//départ recrues 
        		    	for (String jFirstUser : listJFirstUsers) {
        		    		
        		    		//si la liste du jour plus ancien ne contient le nom de joueur de la liste recent c'est une nouvelle recrue 
        		    		if (!listJlastUsers.contains(jFirstUser))
        		    		{
        		    			listMembersDeleted.add(jFirstUser);
        		    		}
        		    	}

        		    	
        		    	
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
									append("Entrées de Joueurs dans BR").
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
