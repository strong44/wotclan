package com.wot.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Text;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.CommunityAccount;

@SuppressWarnings("serial")
public class PersistPlayersRecruistation extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(WotServiceImpl.class.getName());
	
	//static String lieu = "boulot"; //boulot ou maison si boulot -> pedro proxy 
	

	
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	log.warning("========lancement doGet  PersistPlayersRecruistation ============== " );
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, PersistPlayersRecruistation ");
        String recrues = req.getParameter("Recrues");
        if(recrues != null && !"".equalsIgnoreCase(recrues)) {
        	persistAllUsersRecruistation( new Date(), recrues);
		}else {
			log.severe("ERROR: =======lancement PersistPlayersRecruistation  with Recrues null ===");
		}
    }

	public static String persistAllUsersRecruistation(Date date, String users) {
			
			log.warning("========lancement persistAllUsersRecruistation : " +  date + ":" + users + " :============== " );
			
			List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
			AllCommunityAccount myAllCommunityAccount = new AllCommunityAccount ();
			myAllCommunityAccount.setListCommunityAccount(listCommunityAccount);
			PersistenceManager pm =null;
			pm = PMF.get().getPersistenceManager();
			List<String> listIdUser = new ArrayList<String>();
			
			try {
				
				DaoRecruistation myDaoRecruistation = new DaoRecruistation();
				myDaoRecruistation.setDate(date);
				Text text =  new Text(users);
				myDaoRecruistation.setUsers(text);
				
				/////////////////////
				//pm = PMF.get().getPersistenceManager();
		        try {
		        	
		        	
		        	//must transform before persist the objet
		        	pm.currentTransaction().begin();
		        	
		        	pm.makePersistent(myDaoRecruistation);
		        	pm.currentTransaction().commit();
		        	//log.warning("vehicules daoCommunityAccount " + daoCommunityAccount.getData().statsVehicules.get(0).getName() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getBattle_count() + ":"+  daoCommunityAccount.getData().statsVehicules.get(0).getWin_count());
		        	
		        }
			    catch(Exception e){
			    	e.printStackTrace();
			    	log.log(Level.SEVERE, "Exception while saving daoCommunityAccount", e);
		        	pm.currentTransaction().rollback();
		        }
			        
			
						
				
			}   catch (Exception e) {
					// ...
				e.printStackTrace();
				log.throwing("Persist users recruistation", "", e);
				log.severe("Exception " + e.getLocalizedMessage());
				 StackTraceElement[] stack = e.getStackTrace();
				 for (StackTraceElement st : stack) {
					 log.severe(st.getMethodName()+":"+st.getLineNumber());
					 
					 
				 }
				 
				//e.printStackTrace();
			}
			finally {
				if (pm != null)
					pm.close();
			}
		
			log.warning("======== End persistAllUsersRecruistation : " +  date + ":" + users + " :============== " );

			return users;
		
		}
	


}
