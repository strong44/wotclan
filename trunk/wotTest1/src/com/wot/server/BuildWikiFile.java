package com.wot.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.wot.shared.ObjectFactory;
import com.wot.shared.XmlAchievements;
import com.wot.shared.XmlDescription;
import com.wot.shared.XmlListAchievement;
import com.wot.shared.XmlListCategoryAchievement;
import com.wot.shared.XmlListSrcImg;
import com.wot.shared.XmlSrc;
import com.wot.shared.XmlWiki;

public class BuildWikiFile {
	static String lieu ="maison"; 
	
	static public void buildWikiXML() throws IllegalArgumentException {
		URL urlAchievement = null;
		String AllLinesWot = "";
		try {
			if(lieu.equalsIgnoreCase("boulot")) //on passe par 1 proxy
				urlAchievement = new URL ("https://tractro.appspot.com/wiki.worldoftanks.com/achievements");
			else
				urlAchievement = new URL ("http://wiki.worldoftanks.com/achievements");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlAchievement.openStream()));
			String line = "";
			

			while ((line = reader.readLine()) != null) {
				AllLinesWot = AllLinesWot + line;
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//entre  "Battle Hero Achievements" et "Commemorative Achievements" se positionner à la fin le la chaine "src="
		// et extraire depuis cette position jusqu'au début de  la chaine width 
		String cat1BattleHero =  "Battle Hero Achievements";
		String cat2Comm = "Commemorative Achievements";
		String cat3Epc = "Epic Achievements (medals)"; //avec <i> à la fin 
		String cat4Special = "Special Achievements (titles)";
		String cat5Step = "Step Achievements (medals)";
		String cat6Rise = "Rise of the Americas Achievements (medals)" ;
		String cat7Clan = "Clan Wars Campaigns Achievements (medals)";
		
		ObjectFactory objFactory = null;
		try {
			JAXBContext context = JAXBContext.newInstance(XmlWiki.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			objFactory = new ObjectFactory();
			
			//création WIKI
			XmlWiki wiki = objFactory.createXmlWiki();
			
			//création ACHIEVEMENTS
			XmlAchievements myXmlAchievements = objFactory.createXmlAchievements();
			wiki.setACHIEVEMENTS(myXmlAchievements);
			
			//parsing HTML WIKI
			parseHtmlAchievement(AllLinesWot, cat1BattleHero, cat2Comm, objFactory, wiki);
			parseHtmlAchievement(AllLinesWot, cat2Comm, cat3Epc, objFactory, wiki);
			parseHtmlAchievement(AllLinesWot, cat3Epc, cat4Special, objFactory, wiki);
			parseHtmlAchievement(AllLinesWot, cat4Special, cat5Step, objFactory, wiki);
			parseHtmlAchievement(AllLinesWot, cat5Step, cat6Rise, objFactory, wiki);
			parseHtmlAchievement(AllLinesWot, cat6Rise, cat7Clan, objFactory, wiki);
			parseHtmlAchievement(AllLinesWot, cat7Clan, "printfooter", objFactory, wiki);
			
			m.marshal(wiki, System.out);
			m.marshal(wiki, new File("wotWiki.xml")); // D:\workspace\wotclan\\D:\workspace\wotclan
			//JAXBContext.newInstance("com.wot.shared").createMarshaller().marshal(wiki, System.out);
			
			
			
			//A partir du XML instancié les classes !!
//			Unmarshaller unmarshaller = context.createUnmarshaller();
//			wiki = (XmlWiki) unmarshaller.unmarshal(new File("wotWiki.xml"));
//			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>unmarshaller ");
//			System.out.println(wiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT().get(0).getNAME());
			
			
			
			///////////
			HashMap<String, XmlListAchievement> hashMapAch = BuidHashMapAchievement (wiki) ;
			System.out.println(hashMapAch);
			
		} catch (JAXBException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
	
	}
	/**
	 * parse le HTML du wiki achieveemnt pour en extraire les noms de m�dailles , les src d'ic�nes et les descriptions
	 * @param AllLinesWot
	 * @param cat1Medal
	 * @param cat2Medal
	 * @param objFactory 
	 * @param wiki 
	 */
	static void parseHtmlAchievement (String AllLinesWot, String cat1Medal, String cat2Medal, ObjectFactory objFactory, XmlWiki wiki) {

		//création category achievement
		XmlListCategoryAchievement myXmlListCategoryAchievement = objFactory.createXmlListCategoryAchievement();
		myXmlListCategoryAchievement.setNAME(cat1Medal);
		
		//création xlmDescrition category achievement
		XmlDescription myXmlDescription= objFactory.createXmlDescription();
		myXmlDescription.setVALUE("Desccription de la ctégorie de médailles");
		myXmlListCategoryAchievement.setDESCRIPTION(myXmlDescription);
		
		//Ajouter la catégorie achievement au wiki
		wiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT().add(myXmlListCategoryAchievement);
		
		//parse WIKI HTML
		int pos1 = AllLinesWot.indexOf(cat1Medal+"</span></div>" );
		int pos2 = -1; 
		if ("printfooter".equalsIgnoreCase(cat2Medal))
			pos2 = AllLinesWot.indexOf(cat2Medal );
		else
			pos2 = AllLinesWot.indexOf(cat2Medal+"</span></div>" );
		
		
		//selon les cat�gories de m�dailles, on doit aller rechercher la 2�me pour la 1�re ocurence du nom de la cat�gorie de m�dailles
		//donc on prend toujours la derni�re 
		
		if (pos1 == -1)
			pos1 = AllLinesWot.indexOf(cat1Medal+ " <i>" );
		
		
		if (pos2 == -1)
			pos2 = AllLinesWot.indexOf(cat2Medal+ " <i>" );
		
		
		System.out.println("=======>>>>>>>>>>>>>" + cat1Medal);
		if (pos1 != -1 && pos2 !=-1) {
			int posSrc = 0 ;
			while(posSrc != -1 && posSrc<pos2) {
				posSrc = AllLinesWot.indexOf("src=", pos1);
				int posSlashDiv = AllLinesWot.indexOf("</div>", posSrc); //on doit rechercher tous les src avant </div>
				
				if (posSrc != -1 && posSrc<pos2) {
					
					//on est dans les medailles en question
					String srcImgMedal = "";
					List<String> listSrcImgMedal = new ArrayList<String>();
					do {
						posSrc = AllLinesWot.indexOf("src=", pos1);
						posSrc=  posSrc+"src=".length();
						int posWidth = AllLinesWot.indexOf("width", posSrc);
						srcImgMedal = AllLinesWot.substring(posSrc+1, posWidth-2);
						listSrcImgMedal.add(srcImgMedal);
						
						pos1= posWidth;
						posSrc = AllLinesWot.indexOf("src=", pos1);
					}while(posSrc < posSlashDiv);
					
					int posDebutB = AllLinesWot.indexOf("<b>", pos1);
					int posFinB = AllLinesWot.indexOf("</b>", pos1);
					
					String titleMedal= AllLinesWot.substring(posDebutB+"<b>".length(), posFinB);
					for (String src : listSrcImgMedal) { 
						System.out.println(src + "\t" + titleMedal + "\t"); //titre de la m�daille
					}
					pos1= posFinB;
					
					//la description de la m�daille se trouve entre </b> et le prochain "<"
					int posInf = AllLinesWot.indexOf("<", posFinB + "</b>".length());
					String descMedalWithB= AllLinesWot.substring(posDebutB-1 + "<".length(), posInf);
					System.out.println("\t" + descMedalWithB);
					
					
					//création d'un achievement
					XmlListAchievement myXmlListAchievement = objFactory.createXmlListAchievement();
					
					//set du nom de la médaille
					myXmlListAchievement.setNAME(titleMedal);
					
					//set description de la médaille
					//création xlmDescrition achievement
					myXmlDescription= objFactory.createXmlDescription();
					myXmlDescription.setVALUE(descMedalWithB);
					myXmlListAchievement.setDESCRIPTION(myXmlDescription);
					
					//set des src des icônes des médailles
					XmlListSrcImg myXmlListSrcImg = objFactory.createXmlListSrcImg();
					
					for (String src : listSrcImgMedal) {
						//création des src
						XmlSrc myXmlSrc = objFactory.createXmlSrc();
						myXmlSrc.setVALUE(src);
						
						//ajout à la liste des src de la médaille
						myXmlListSrcImg.getSRC().add(myXmlSrc);
					}
					
					myXmlListAchievement.setSRCIMG(myXmlListSrcImg);
					
					//ajouter listAchievement à Catégory achievement
					myXmlListCategoryAchievement.getACHIEVEMENT().add(myXmlListAchievement);
				}
				
				
			}
			
		}
	}
	
	
	public static HashMap<String, XmlListAchievement> BuidHashMapAchievement (XmlWiki xmlWiki) {
		HashMap<String, XmlListAchievement> hashMapAchievement = new HashMap<String, XmlListAchievement>();
		
		
		//parcours de toutes les cat�gories de m�dailles
		for(XmlListCategoryAchievement listCatAch	:	xmlWiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT() ) {
			for (XmlListAchievement ach : listCatAch.getACHIEVEMENT()) {
				for (XmlSrc src : ach.getSRCIMG().getSRC()) {
					String srcValue = src.getVALUE();
					int posLastSlash  = srcValue.lastIndexOf("/");
					String nameFile = srcValue.substring(posLastSlash+1);
					hashMapAchievement.put(nameFile, ach);
				}
				
			}
		}
		
		return hashMapAchievement;
		
	}
	
	public static void main(String[] args) {
		
		buildWikiXML();
		System.exit(0);
		
	}

}
