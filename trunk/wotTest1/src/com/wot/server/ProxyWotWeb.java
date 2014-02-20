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
import javax.servlet.http.*;

import com.google.gson.Gson;
import com.wot.server.api.TransformDtoObject;
import com.wot.shared.AllCommunityAccount;
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
import com.wot.shared.TankEncyclopedia;
import com.wot.shared.WnEfficientyTank;

@SuppressWarnings("serial")
public class ProxyWotWeb extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(WotServiceImpl.class.getName());
	
	//static String lieu = "boulot"; //boulot ou maison si boulot -> pedro proxy 
	
	private static String applicationIdEU = "d0a293dc77667c9328783d489c8cef73";
	private static String urlServerEU =  "http://api.worldoftanks.eu";

	
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	log.warning("========lancement doGet ProxyWotWeb  ============== " );
        resp.setContentType("text/plain");
        //resp.getWriter().println("Hello, ProxyWotWeb :" + req.getRequestURI() + "-" + req.getQueryString());
        
        if (req.getQueryString() != null && !"".equalsIgnoreCase(req.getQueryString())) {
			URL url = null ;
			
			url = new URL(req.getQueryString());		
		
			//lecture de la rÃ©ponse recherche du clan
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setReadTimeout(60000);
			conn.setConnectTimeout(60000);
			//conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			//BufferedReader reader = new BufferedReader(new InputStreamReader(urlClan.openStream(), "UTF-8"));
			String line = "";
			String AllLines = "";
	
			while ((line = reader.readLine()) != null) {
				AllLines = AllLines + line;
			}
			log.info(AllLines);
			reader.close();
			resp.getWriter().println(AllLines);
        }
        else {
        	resp.getWriter().println("Hello, ProxyWotWeb :" + req.getRequestURI() + "-" + req.getQueryString());
        }
		
		
		//pour proxy http://wotachievement.appspot.com/WotWeb/
        //api.worldoftanks.ru/2.0/account/info/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
        
        
        //pour proxy en DEV : http://127.0.0.1:8888/cronPersistPlayersStats?clanId=500006824
        //
        //http://api.worldoftanks.ru/2.0/account/info/?application_id=171745d21f7f98fd8878771da1000a31&account_id=461
    }

}
