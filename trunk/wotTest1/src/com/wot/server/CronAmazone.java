package com.wot.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.swing.text.html.parser.Parser;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.google.gson.Gson;
import com.wot.server.api.TransformDtoObject;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.AllStatistics;
import com.wot.shared.CommunityAccount;
import com.wot.shared.CommunityClan;
import com.wot.shared.DataCommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.DataCommunityMembers;
import com.wot.shared.DataPlayerInfos;
import com.wot.shared.DataPlayerTankRatings;
import com.wot.shared.DataTankEncyclopedia;
import com.wot.shared.DataWnEfficientyTank;
import com.wot.shared.PlayerTankRatings;
import com.wot.shared.PlayersInfos;
import com.wot.shared.TankEncyclopedia;
import com.wot.shared.WnEfficientyTank;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@SuppressWarnings("serial")
public class CronAmazone extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(WotServiceImpl.class.getName());
	private static String urlServerEU =  "https://www.amazon.fr/s/ref=nb_sb_ss_i_7_20?__mk_fr_FR=%C3%85M%C3%85%C5%BD%C3%95%C3%91&url=search-alias%3Daps&field-keywords=samsung+galaxy+tab4+7+pouces&sprefix=samsung+galaxy+tab4+%2Caps%2C207";
	private static String lieu = "maison"; 
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	log.warning("========lancement doGet  CronAmazone ============== " );
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, CronAmazone ");
       	cronAmazone( );
    }

	public static void  cronAmazone( ) {
			
			log.warning("========lancement cronAmazone : " );
			
			try {
				
				//=== recup des stats des joueurs ==========
				URL url = null ;
				String urlServer = urlServerEU ;
				
				if(lieu.equalsIgnoreCase("boulot")){ //on passe par 1 proxy
					url = new URL(WotServiceImpl.proxy + urlServer );
				}
				else {
					url = new URL(urlServer);
				}
				
				/*
				<a class="a-link-normal s-access-detail-page  a-text-normal" 
				  title="Samsung Galaxy Tab 4 Tablette Tactile 7&quot; (17,78 cm) 1,2 GHz 8 Go Android Wi-Fi Blanc" 
				  href="http://www.amazon.fr/Samsung-Galaxy-Tab-Tablette-Tactile/dp/B00KINIP9C/ref=sr_1_1?ie=UTF8&amp;qid=1434972809&amp;sr=8-1&amp;keywords=samsung+galaxy+tab4+7+pouces">
				  <h2 class="a-size-medium a-color-null s-inline s-access-title a-text-normal">
				  Samsung Galaxy Tab 4 Tablette Tactile 7" (17,78 cm) 1,2 GHz 8 Go Android Wi-Fi Blanc
				  </h2>
				 </a>
				*/				
				/*
				 * <span id="priceblock_ourprice" class="a-size-medium a-color-price">EUR 141,10</span>
				 */
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setReadTimeout(60000);
				conn.setConnectTimeout(60000);
				conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				
				String line = "";
				String AllLines = "";
			
				while ((line = reader.readLine()) != null) {
					AllLines = AllLines + line;
				}
				System.out.println(AllLines);
				reader.close();
				
				//title="Samsung Galaxy Tab 4 Tablette Tactile 7&quot;
				//<span class="a-size-base a-color-price s-price a-text-bold">EUR 141,10</span></a>  
				int indexTitle = AllLines.indexOf("title=\"Samsung Galaxy Tab 4 Tablette Tactile 7&quot;");
				if (indexTitle> 0){ 
					int indexDebutEur = AllLines.indexOf("EUR", indexTitle);
					if (indexDebutEur > 0) {
						int indexDebutSpan = AllLines.indexOf("span", indexDebutEur);
						if (indexDebutSpan > 0)
						{
							//EUR 136,90
							String strPrix= AllLines.substring(indexDebutEur + 4 , indexDebutSpan - 2);
							System.out.println("Prix str: " + strPrix);
							try {
								Float floatPrix = Float.parseFloat(strPrix.replace(",", "."));
								System.out.println("Prix float: " + floatPrix);
								int prixMax = 137 ;
								if (floatPrix < prixMax) {
									System.out.println("Prix < " +prixMax);
									
									
									 Properties props = new Properties();
								        Session session = Session.getDefaultInstance(props, null);

								        String msgBody = "Le prix de la tablette samsung galaxy tab4 7 pouces est à :" + strPrix;

								        try {
								            Message msg = new MimeMessage(session);
								            msg.setFrom(new InternetAddress("thierry.leconniat@gmail.com", "Thierry LE CONNIAT"));
								            msg.addRecipient(Message.RecipientType.TO,
								                             new InternetAddress("esthetic.auto29@yahoo.fr", "SARL Esthetic"));
								            msg.setSubject("La recherche de la tablette Samsung galaxy tab4 7 pouces");
								            msg.setText(msgBody);
								            Transport.send(msg);

								        } catch (AddressException e) {
								            // ...
								        } catch (MessagingException e) {
								            // ...
								        }
								}
								else
									System.out.println("Prix > " +prixMax);
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								System.out.println("Erreur de conversion de prix en float: " + strPrix);
							}
							
						}
						else
							System.out.println("Erreur index of indexDebutSpan");
					}
					else
						System.out.println("Erreur index of indexDebutEur");
				}
				else
					System.out.println("Erreur index of indexTitle");
				
				
				
//				 org.jsoup.nodes.Document doc = Jsoup.parse(AllLines, "http://wotnvs.appspot.com");
//				 org.jsoup.select.Elements links =  doc.select("span[id]");
//				 
//				 for (Element link : links) {
//			            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
//			     }
				
				
			} catch (MalformedURLException e) {
				// ...
				log.throwing("cronAmazone", "", e);
				log.severe("MalformedURLException " + e.getLocalizedMessage());
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				// ...
				log.throwing("cronAmazone", "", e);
				log.severe("IOException " + e.getLocalizedMessage());
				e.printStackTrace();
			} catch (Exception e) {
					// ...
				e.printStackTrace();
				log.throwing("cronAmazone", "", e);
				log.severe("Exception " + e.getLocalizedMessage());
				 StackTraceElement[] stack = e.getStackTrace();
				 for (StackTraceElement st : stack) {
					 log.severe(st.getMethodName()+":"+st.getLineNumber());
					 
					 
				 }
				 
				//e.printStackTrace();
			}
			finally {
				
			}
		
			
		
		}


	 private static String trim(String s, int width) {
	        if (s.length() > width)
	            return s.substring(0, width-1) + ".";
	        else
	            return s;
	    }
	
	 private static void print(String msg, Object... args) {
	        System.out.println(String.format(msg, args));
	 }
	

	public static void main(String args[]) {
		cronAmazone();
	}
		

}
