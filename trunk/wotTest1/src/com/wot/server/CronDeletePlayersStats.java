package com.wot.server;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CronDeletePlayersStats extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(WotServiceImpl.class.getName());
	
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	log.warning("========lancement doGet  CronDeletePlayersStats ============== " );
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, CronDeletePlayersStats ");
        String nbDelete = req.getParameter("nbDelete");
        int nb = Integer.valueOf(nbDelete);
        if(nbDelete != null && !"".equalsIgnoreCase(nbDelete) && nb > 0) {
        	cronDeleteSomeStats( new Date(), nb);
		}else {
			log.severe("ERROR: =======lancement CronDeletePlayersStats avec " + nbDelete);
		}
    }

    /**
     * efffacement de la table DaoCommunityAccount2 de nb enregistrement
     * @param date
     * @param nb
     */
	public void cronDeleteSomeStats(Date date, int nb) {
			
			log.warning("========lancement cronDeleteSomeStats : " +  date + ":" + nb + " :============== " );
			
			PersistenceManager pm =null;
			pm = PMF.get().getPersistenceManager();
			try {
				//some cleaning in stats 
				Query query = pm.newQuery(DaoCommunityAccount2.class);
			    query.setOrdering("dateCommunityAccount asc");
			    query.setRange(0, nb); //only nb results 
			    List<DaoCommunityAccount2> resultsTmp = (List<DaoCommunityAccount2>) query.execute();
			    
			    try {
			    	 if (!resultsTmp.isEmpty()) {
			    		    for (DaoCommunityAccount2 myDaoCommunityAccount2 : resultsTmp) {
			    		    	pm.currentTransaction().begin();
					        	//
			    		    	//log.warning(myDaoCommunityAccount2.getDateCommunityAccount().toString());
			    		    	//
			    		    	if (myDaoCommunityAccount2.getData() != null && myDaoCommunityAccount2.getData().getStats() != null) {
			    		    		pm.deletePersistent(myDaoCommunityAccount2.getData().getStats());
			    		    		pm.deletePersistent(myDaoCommunityAccount2.getData());
			    		    	}
			    		    	pm.deletePersistent(myDaoCommunityAccount2);
					        	pm.currentTransaction().commit();
			    		    }
			    	 } 
		        }
			    catch(Exception e){
			    	e.printStackTrace();
			    	log.log(Level.SEVERE, "Exception while deleting daoCommunityAccount", e);
		        	pm.currentTransaction().rollback();
		        }
			    
			    //DaoDataCommunityAccountStatsVehicules - On peut tout supprimer
			    query = pm.newQuery(DaoDataCommunityAccountStatsVehicules.class);
			    query.setRange(0, nb); //only nb results 
			    List<DaoDataCommunityAccountStatsVehicules> resultsVeh = (List<DaoDataCommunityAccountStatsVehicules>) query.execute();
			    
			    try {
			    	 if (!resultsVeh.isEmpty()) {
			    		    for (DaoDataCommunityAccountStatsVehicules myDao : resultsVeh) {
			    		    	pm.currentTransaction().begin();
					        	//
			    		    	//log.warning(myDao.getName());
			    		    	//
					        	pm.deletePersistent(myDao);
					        	pm.currentTransaction().commit();
			    		    }
			    	 } 
		        }
			    catch(Exception e){
			    	e.printStackTrace();
			    	log.log(Level.SEVERE, "Exception while deleting DaoDataCommunityAccountStatsVehicules", e);
		        	pm.currentTransaction().rollback();
		        }
			    
			    
			}  catch (Exception e) {
					// ...
				e.printStackTrace();
				log.throwing("Delete stats", "", e);
				log.severe("Exception " + e.getLocalizedMessage());
				 StackTraceElement[] stack = e.getStackTrace();
				 for (StackTraceElement st : stack) {
					 log.severe(st.getMethodName()+":"+st.getLineNumber());
				 }
			}
			finally {
				if (pm != null)
					pm.close();
			}
		}
}
