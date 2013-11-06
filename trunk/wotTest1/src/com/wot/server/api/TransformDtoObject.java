package com.wot.server.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashMap;
import com.wot.server.DaoClan;
import com.wot.server.DaoCommunityAccount;
import com.wot.server.DaoCommunityClan;
import com.wot.server.DaoDataClan;
import com.wot.server.DaoDataCommunityAccount;
import com.wot.server.DaoDataCommunityAccountAchievements;
import com.wot.server.DaoDataCommunityAccountStats;
import com.wot.server.DaoDataCommunityAccountStatsVehicules;
import com.wot.server.DaoDataCommunityClan;
import com.wot.server.DaoDataCommunityClanMembers;
import com.wot.server.DaoDataCommunityMembers;
import com.wot.server.DaoItemsDataClan;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;
import com.wot.shared.CommunityClan;
import com.wot.shared.DataClan;
import com.wot.shared.DataCommunityAccount;
import com.wot.shared.DataCommunityAccountAchievements;
import com.wot.shared.DataCommunityAccountRatings;
import com.wot.shared.DataCommunityAccountVehicules;
import com.wot.shared.DataCommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.DataCommunityMembers;
import com.wot.shared.ItemsDataClan;

public class TransformDtoObject {

	public static DaoClan TransformClanToDaoClan(Clan clan ) {
		
		DaoClan daoClan = new DaoClan();
		daoClan.setStatus( clan.getStatus());
		daoClan.setStatus_code(clan.getStatus_code());
		
		daoClan.setItems(TransformItemsDataClanToDaoItemsDataClan(clan.getItems()));
		return daoClan;
	}

	public static DaoDataClan TransformDataClanToDaoDataClan(DataClan dataclan ) {
		
		DaoDataClan daoDataClan = new DaoDataClan();
		daoDataClan.setItems(TransformItemsDataClanToDaoItemsDataClan(dataclan.getItems()));
		return daoDataClan;
	}
	
	//transforme la liste des itemsDataclan
	public static List<DaoItemsDataClan> TransformItemsDataClanToDaoItemsDataClan(List<ItemsDataClan> itemsDataclan ) {
		
		List<DaoItemsDataClan> listDaoItemsDataClan = new ArrayList<DaoItemsDataClan>();
		
		//transform each ItemsDataClan to DAOItemsDataClan
		for (ItemsDataClan myItemsDataClan : itemsDataclan) {
			listDaoItemsDataClan.add(TransformItemsDataClanToDaoItemsDataClan(myItemsDataClan));
		}
		return listDaoItemsDataClan;
		
	}
	
	//transforme un itemsDataclan
	public static DaoItemsDataClan TransformItemsDataClanToDaoItemsDataClan(ItemsDataClan itemsDataclan ) {
		
		DaoItemsDataClan daoItemsDataClan = new DaoItemsDataClan();

		daoItemsDataClan.setAbbreviation(itemsDataclan.getAbbreviation());
		daoItemsDataClan.setClan_color(itemsDataclan.getClan_color());
		
		daoItemsDataClan.setClan_emblem_url(itemsDataclan.getClan_emblem_url());
		
		daoItemsDataClan.setCreated_at(itemsDataclan.getCreated_at());
		
		daoItemsDataClan.setId(itemsDataclan.getId());
		
		daoItemsDataClan.setMember_count(itemsDataclan.getMember_count());
		
		daoItemsDataClan.setMotto(itemsDataclan.getMotto());
		
		daoItemsDataClan.setName(itemsDataclan.getName());
		
		daoItemsDataClan.setOwner(itemsDataclan.getOwner());
		
		
		return daoItemsDataClan;
		
	}
	
//	//== Tranform datas CLAN and members 
//	public static DaoCommunityClan TransformCommunityClanToDaoCommunityClan(CommunityClan communityClan ) {
//		
//		DaoCommunityClan daoCommunityClan = new DaoCommunityClan();
//		daoCommunityClan.setStatus( communityClan.getStatus());
//		daoCommunityClan.setStatus_code(communityClan.getStatus_code());
//		
//		daoCommunityClan.setIdClan(communityClan.getIdClan());
//		daoCommunityClan.setDateCommunityClan(communityClan.getDateCommunityClan());
//		
//		daoCommunityClan.setData(TransformDataCommunityClanToDaoDataCommunityClan(communityClan.getData()));
//		return daoCommunityClan;
//	}

	//== new  
	public static CommunityClan TransformCommunityDaoCommunityClanToCommunityClan(DaoCommunityClan daoCommunityClan ) {
		
		CommunityClan communityClan = new CommunityClan();
		communityClan.setStatus( daoCommunityClan.getStatus());
		communityClan.setStatus_code(daoCommunityClan.getStatus_code());
		
		communityClan.setIdClan(daoCommunityClan.getIdClan());
		communityClan.setDateCommunityClan(daoCommunityClan.getDateCommunityClan());
		
		communityClan.setData(TransformDaoDataCommunityClanToDataCommunityClan(daoCommunityClan.getData()));
		return communityClan;
	}

	
	private static DataCommunityClan TransformDaoDataCommunityClanToDataCommunityClan( Map<String, DaoDataCommunityClanMembers> data) {
		DataCommunityClan myDataCommunityClan = new DataCommunityClan();
		//data
		myDataCommunityClan.setMembers(TransformListDaoDataCommunityClanMembersToListDataCommunityClanMembers(data));
		
		return myDataCommunityClan;
	}

	private static List<DataCommunityClanMembers> TransformListDaoDataCommunityClanMembersToListDataCommunityClanMembers(Map<String, DaoDataCommunityClanMembers> map) {
		List<DataCommunityClanMembers> listDataCommunityClanMembers = new ArrayList<DataCommunityClanMembers>();
		
		//transform each ItemsDataClan to DAOItemsDataClan
		Set<Entry<String, DaoDataCommunityClanMembers>> set =  map.entrySet();
		for (Entry<String, DaoDataCommunityClanMembers> entry :set) {
			listDataCommunityClanMembers.add(TransformDaoDataCommunityClanMembersToDataCommunityClanMembers(entry.getValue()));
		}
		return listDataCommunityClanMembers;
	}

	private static DataCommunityClanMembers TransformDaoDataCommunityClanMembersToDataCommunityClanMembers(DaoDataCommunityClanMembers clanMembers) {
		DataCommunityClanMembers myDataCommunityClanMembers = new DataCommunityClanMembers();
		List<DataCommunityMembers> listDataCommunityMembers = new ArrayList<DataCommunityMembers>();
		
		myDataCommunityClanMembers.setAccount_id(clanMembers.getAccount_id());
		myDataCommunityClanMembers.setAccount_name(clanMembers.getAccount_name());
		
		//Transform 
		Set<Entry<String, DaoDataCommunityMembers>> set =  clanMembers.getMembers().entrySet();
		for (Entry<String, DaoDataCommunityMembers> entry :set) {
			listDataCommunityMembers.add(TransformDaoDataCommunityMembersToDataCommunityMembers(entry.getValue()));
		}
		
		//add list !! 
		myDataCommunityClanMembers.setMembers(listDataCommunityMembers);
		return myDataCommunityClanMembers;
	}

	private static DataCommunityMembers TransformDaoDataCommunityMembersToDataCommunityMembers(DaoDataCommunityMembers myDaoDataCommunityMembers) {
		// TODO Auto-generated method stub
		DataCommunityMembers myDataCommunityMembers = new DataCommunityMembers();
		//
		myDataCommunityMembers.setAccount_id(myDaoDataCommunityMembers.getAccount_id());
		myDataCommunityMembers.setAccount_name(myDaoDataCommunityMembers.getAccount_name());
		myDataCommunityMembers.setCreated_at(myDaoDataCommunityMembers.getCreated_at());
		myDataCommunityMembers.setRole(myDaoDataCommunityMembers.getRole());
		myDataCommunityMembers.setUpdated_at(myDaoDataCommunityMembers.getUpdated_at());
		
		return myDataCommunityMembers;
	}

	public static DaoCommunityAccount TransformCommunityAccountToDaoCommunityAccount(CommunityAccount account) {
		// TODO Auto-generated method stub
		DaoCommunityAccount myDaoCommunityAccount = new DaoCommunityAccount();
		myDaoCommunityAccount.setStatus( account.getStatus());
		myDaoCommunityAccount.setStatus_code(account.getStatus_code());
		myDaoCommunityAccount.setIdUser(account.getIdUser());
		//myDaoCommunityAccount.setDateCommunityAccount(account.getDateCommunityAccount());
		myDaoCommunityAccount.setName(account.getName());	
		myDaoCommunityAccount.setData(TransformDataCommunityAccountToDaoDataCommunityAccount(account.getData()));
		
		return myDaoCommunityAccount;
	}

	
	public static CommunityAccount TransformDaoCommunityAccountToCommunityAccount(DaoCommunityAccount daoAccount) {
		// TODO Auto-generated method stub
		CommunityAccount myCommunityAccount = new CommunityAccount();
		myCommunityAccount.setStatus( daoAccount.getStatus());
		myCommunityAccount.setStatus_code(daoAccount.getStatus_code());
		myCommunityAccount.setIdUser(daoAccount.getIdUser());
		//myCommunityAccount.setDateCommunityAccount(daoAccount.getDateCommunityAccount());
		myCommunityAccount.setName(daoAccount.getName());	
		myCommunityAccount.setData(TransformDaoDataCommunityAccountToDataCommunityAccount(daoAccount.getData()));
		
		return myCommunityAccount;
	}

	
	private static DaoDataCommunityAccount TransformDataCommunityAccountToDaoDataCommunityAccount(DataCommunityAccount data) {
		DaoDataCommunityAccount myDaoDataCommunityAccount = new DaoDataCommunityAccount();
		myDaoDataCommunityAccount.setName(data.getName());
		
		myDaoDataCommunityAccount.setAchievements(TransformDataCommunityAccountAchievementsToDaoDataCommunityAccountAchievements(data.getAchievements()));
		myDaoDataCommunityAccount.setStats(TransformDataCommunityAccountStatsToDaoDataCommunityAccountStats(data.getStats()));
		//
		myDaoDataCommunityAccount.setStatsVehicules(TransformDataCommunityAccountStatsVehiculesToDaoDataCommunityAccountStatsVehicules(data.getVehicles()));
		return myDaoDataCommunityAccount;
	}

	private static List<DaoDataCommunityAccountStatsVehicules> TransformDataCommunityAccountStatsVehiculesToDaoDataCommunityAccountStatsVehicules(
			List<DataCommunityAccountVehicules> vehicules) {
		// TODO Auto-generated method stub
		List<DaoDataCommunityAccountStatsVehicules> listDaoDataCommunityAccountStatsVehicules = new ArrayList<DaoDataCommunityAccountStatsVehicules>();
		
		for (DataCommunityAccountVehicules myDataCommunityAccountVehicules : vehicules) {
			//on ne prend que les chars qui ont + de 100 batailles sinon trop de chars
			if (myDataCommunityAccountVehicules.getBattle_count()>=100) {
				listDaoDataCommunityAccountStatsVehicules.add(TransformDataCommunityAccountStatsVehiculeToDaoDataCommunityAccountStatsVehicule(myDataCommunityAccountVehicules));
			}
		}
		return listDaoDataCommunityAccountStatsVehicules;
	}

	private static DaoDataCommunityAccountStatsVehicules TransformDataCommunityAccountStatsVehiculeToDaoDataCommunityAccountStatsVehicule(
			DataCommunityAccountVehicules myDataCommunityAccountVehicules) {
		// TODO Auto-generated method stub
		/**
		 * 	/**
	 * {
        "spotted": 0, 
        "localized_name": "KV-1S", 
        "name": "KV-1s", 
        "level": 6, 
        "damageDealt": 0, 
        "survivedBattles": 0, 
        "battle_count": 1400, 
        "nation": "ussr", 
        "image_url": "/static/2.7.0/encyclopedia/tankopedia/vehicle/small/ussr-kv-1s.png", 
        "frags": 0, 
        "win_count": 773, 
        "class": "heavyTank"
      },  */
		DaoDataCommunityAccountStatsVehicules myDaoDataCommunityAccountStatsVehicules = new DaoDataCommunityAccountStatsVehicules();
		myDaoDataCommunityAccountStatsVehicules.setBattle_count(myDataCommunityAccountVehicules.getBattle_count());
		myDaoDataCommunityAccountStatsVehicules.setImage_url(myDataCommunityAccountVehicules.getImage_url());
		myDaoDataCommunityAccountStatsVehicules.setLevel(myDataCommunityAccountVehicules.getLevel());
		myDaoDataCommunityAccountStatsVehicules.setLocalized_name(myDataCommunityAccountVehicules.getLocalized_name());
		myDaoDataCommunityAccountStatsVehicules.setName(myDataCommunityAccountVehicules.getName());
		myDaoDataCommunityAccountStatsVehicules.setNation(myDataCommunityAccountVehicules.getNation());
		myDaoDataCommunityAccountStatsVehicules.setWin_count(myDataCommunityAccountVehicules.getWin_count());
		return myDaoDataCommunityAccountStatsVehicules;
	}

	private static DataCommunityAccount TransformDaoDataCommunityAccountToDataCommunityAccount(DaoDataCommunityAccount data) {
		DataCommunityAccount myDataCommunityAccount = new DataCommunityAccount();
		myDataCommunityAccount.setName(data.getName());
		
		myDataCommunityAccount.setAchievements(TransformDaoDataCommunityAccountAchievementsToDataCommunityAccountAchievements(data.getAchievements()));
		myDataCommunityAccount.setStats(TransformDaoDataCommunityAccountStatsToDataCommunityAccountStats(data.getStats()));
		return myDataCommunityAccount;
	}
	
	
	private static DataCommunityAccountRatings TransformDaoDataCommunityAccountStatsToDataCommunityAccountStats(DaoDataCommunityAccountStats stats) {
		DataCommunityAccountRatings myDataCommunityAccountStats =  new DataCommunityAccountRatings();
		
		myDataCommunityAccountStats.setBattle_avg_performance(new Double(stats.getBattle_avg_performance()));
		myDataCommunityAccountStats.setBattle_avg_xp(new Double(stats.getBattle_avg_xp()));
		myDataCommunityAccountStats.setBattle_wins(new Double(stats.getBattle_wins()));
		myDataCommunityAccountStats.setBattles(new Double(stats.getBattles()));
		myDataCommunityAccountStats.setCtf_points(new Double(stats.getCtf_points()));
		myDataCommunityAccountStats.setDamage_dealt(new Double(stats.getDamage_dealt()));
		myDataCommunityAccountStats.setDropped_ctf_points(new Double(stats.getDropped_ctf_points()));
		myDataCommunityAccountStats.setFrags(new Double(stats.getFrags()));
		myDataCommunityAccountStats.setIntegrated_rating(new Double(stats.getIntegrated_rating()));
		myDataCommunityAccountStats.setSpotted(new Double(stats.getSpotted()));
		myDataCommunityAccountStats.setXp(new Double(stats.getXp()));
		
		
		
		return myDataCommunityAccountStats;
	}

	private static DataCommunityAccountAchievements TransformDaoDataCommunityAccountAchievementsToDataCommunityAccountAchievements(DaoDataCommunityAccountAchievements achievements) {
		// TODO Auto-generated method stub
		DataCommunityAccountAchievements myDataCommunityAccountAchievements = new DataCommunityAccountAchievements();
		myDataCommunityAccountAchievements.setBeasthunter(achievements.getBeasthunter());
		myDataCommunityAccountAchievements.setDefender(achievements.getDefender());
		myDataCommunityAccountAchievements.setDiehard(achievements.getDiehard());
		myDataCommunityAccountAchievements.setInvader(achievements.getInvader());
		myDataCommunityAccountAchievements.setLumberjack(achievements.getLumberjack());
		myDataCommunityAccountAchievements.setMedalAbrams(achievements.getMedalAbrams());
		myDataCommunityAccountAchievements.setMedalBillotte(achievements.getMedalBillotte());
		myDataCommunityAccountAchievements.setMedalBurda(achievements.getMedalBurda());
		myDataCommunityAccountAchievements.setMedalCarius(achievements.getMedalCarius());
		myDataCommunityAccountAchievements.setMedalEkins(achievements.getMedalEkins());
		myDataCommunityAccountAchievements.setMedalFadin(achievements.getMedalFadin());
		myDataCommunityAccountAchievements.setMedalHalonen(achievements.getMedalHalonen());
		myDataCommunityAccountAchievements.setMedalKay(achievements.getMedalKay());
		myDataCommunityAccountAchievements.setMedalKnispel(achievements.getMedalKnispel());
		myDataCommunityAccountAchievements.setMedalKolobanov(achievements.getMedalKolobanov());
		myDataCommunityAccountAchievements.setMedalLavrinenko(achievements.getMedalLavrinenko());
		myDataCommunityAccountAchievements.setMedalLeClerc(achievements.getMedalLeClerc());
		myDataCommunityAccountAchievements.setMedalOrlik(achievements.getMedalOrlik());
		myDataCommunityAccountAchievements.setMedalOskin(achievements.getMedalOskin());
		myDataCommunityAccountAchievements.setMedalPoppel(achievements.getMedalPoppel() );
		myDataCommunityAccountAchievements.setMedalWittmann(achievements.getMedalWittmann());
		myDataCommunityAccountAchievements.setMousebane(achievements.getMousebane());
		myDataCommunityAccountAchievements.setRaider(achievements.getRaider());
		myDataCommunityAccountAchievements.setScout(achievements.getScout());
		myDataCommunityAccountAchievements.setSniper(achievements.getSniper());
		myDataCommunityAccountAchievements.setSupporter(achievements.getSupporter());
		myDataCommunityAccountAchievements.setTankExpert(achievements.getTankExpert());
		myDataCommunityAccountAchievements.setTitleSniper(achievements.getTitleSniper());
		myDataCommunityAccountAchievements.setWarrior(achievements.getWarrior());
		
		return myDataCommunityAccountAchievements;
	}

	
	
	
	private static DaoDataCommunityAccountStats TransformDataCommunityAccountStatsToDaoDataCommunityAccountStats(DataCommunityAccountRatings stats) {
		DaoDataCommunityAccountStats myDaoDataCommunityAccountStats =  new DaoDataCommunityAccountStats();
		
		myDaoDataCommunityAccountStats.setBattle_avg_performance(stats.getBattle_avg_performance());
		myDaoDataCommunityAccountStats.setBattle_avg_xp(stats.getBattle_avg_xp());
		myDaoDataCommunityAccountStats.setBattle_wins(stats.getBattle_wins());
		myDaoDataCommunityAccountStats.setBattles(stats.getBattles());
		myDaoDataCommunityAccountStats.setCtf_points(stats.getCtf_points());
		myDaoDataCommunityAccountStats.setDamage_dealt(stats.getDamage_dealt());
		myDaoDataCommunityAccountStats.setDropped_ctf_points(stats.getDropped_ctf_points());
		myDaoDataCommunityAccountStats.setFrags(stats.getFrags());
		myDaoDataCommunityAccountStats.setIntegrated_rating(stats.getIntegrated_rating());
		myDaoDataCommunityAccountStats.setSpotted(stats.getSpotted());
		myDaoDataCommunityAccountStats.setXp(stats.getXp());
		
		
		
		return myDaoDataCommunityAccountStats;
	}

	private static DaoDataCommunityAccountAchievements TransformDataCommunityAccountAchievementsToDaoDataCommunityAccountAchievements(DataCommunityAccountAchievements achievements) {
		// TODO Auto-generated method stub
		DaoDataCommunityAccountAchievements myDaoDataCommunityAccountAchievements = new DaoDataCommunityAccountAchievements();
		myDaoDataCommunityAccountAchievements.setBeasthunter(achievements.getBeasthunter());
		myDaoDataCommunityAccountAchievements.setDefender(achievements.getDefender());
		myDaoDataCommunityAccountAchievements.setDiehard(achievements.getDiehard());
		myDaoDataCommunityAccountAchievements.setInvader(achievements.getInvader());
		myDaoDataCommunityAccountAchievements.setLumberjack(achievements.getLumberjack());
		myDaoDataCommunityAccountAchievements.setMedalAbrams(achievements.getMedalAbrams());
		myDaoDataCommunityAccountAchievements.setMedalBillotte(achievements.getMedalBillotte());
		myDaoDataCommunityAccountAchievements.setMedalBurda(achievements.getMedalBurda());
		myDaoDataCommunityAccountAchievements.setMedalCarius(achievements.getMedalCarius());
		myDaoDataCommunityAccountAchievements.setMedalEkins(achievements.getMedalEkins());
		myDaoDataCommunityAccountAchievements.setMedalFadin(achievements.getMedalFadin());
		myDaoDataCommunityAccountAchievements.setMedalHalonen(achievements.getMedalHalonen());
		myDaoDataCommunityAccountAchievements.setMedalKay(achievements.getMedalKay());
		myDaoDataCommunityAccountAchievements.setMedalKnispel(achievements.getMedalKnispel());
		myDaoDataCommunityAccountAchievements.setMedalKolobanov(achievements.getMedalKolobanov());
		myDaoDataCommunityAccountAchievements.setMedalLavrinenko(achievements.getMedalLavrinenko());
		myDaoDataCommunityAccountAchievements.setMedalLeClerc(achievements.getMedalLeClerc());
		myDaoDataCommunityAccountAchievements.setMedalOrlik(achievements.getMedalOrlik());
		myDaoDataCommunityAccountAchievements.setMedalOskin(achievements.getMedalOskin());
		myDaoDataCommunityAccountAchievements.setMedalPoppel(achievements.getMedalPoppel() );
		myDaoDataCommunityAccountAchievements.setMedalWittmann(achievements.getMedalWittmann());
		myDaoDataCommunityAccountAchievements.setMousebane(achievements.getMousebane());
		myDaoDataCommunityAccountAchievements.setRaider(achievements.getRaider());
		myDaoDataCommunityAccountAchievements.setScout(achievements.getScout());
		myDaoDataCommunityAccountAchievements.setSniper(achievements.getSniper());
		myDaoDataCommunityAccountAchievements.setSupporter(achievements.getSupporter());
		myDaoDataCommunityAccountAchievements.setTankExpert(achievements.getTankExpert());
		myDaoDataCommunityAccountAchievements.setTitleSniper(achievements.getTitleSniper());
		myDaoDataCommunityAccountAchievements.setWarrior(achievements.getWarrior());
		
		return myDaoDataCommunityAccountAchievements;
	}

	
	
	
}
