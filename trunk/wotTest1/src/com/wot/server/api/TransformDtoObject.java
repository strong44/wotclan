package com.wot.server.api;

import java.util.ArrayList;
import java.util.List;

import com.wot.server.DaoClan;
import com.wot.server.DaoCommunityAccount;
import com.wot.server.DaoCommunityClan;
import com.wot.server.DaoDataClan;
import com.wot.server.DaoDataCommunityAccount;
import com.wot.server.DaoDataCommunityAccountAchievements;
import com.wot.server.DaoDataCommunityAccountStats;
import com.wot.server.DaoDataCommunityClan;
import com.wot.server.DaoDataCommunityClanMembers;
import com.wot.server.DaoItemsDataClan;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;
import com.wot.shared.CommunityClan;
import com.wot.shared.DataClan;
import com.wot.shared.DataCommunityAccount;
import com.wot.shared.DataCommunityAccountAchievements;
import com.wot.shared.DataCommunityAccountRatings;
import com.wot.shared.DataCommunityClan;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.ItemsDataClan;

public class TransformDtoObject {

	public static DaoClan TransformClanToDaoClan(Clan clan ) {
		
		DaoClan daoClan = new DaoClan();
		daoClan.setStatus( clan.getStatus());
		daoClan.setStatus_code(clan.getStatus_code());
		
		daoClan.setData(TransformDataClanToDaoDataClan(clan.getData()));
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
	
	//== Tranform datas CLAN and members 
	public static DaoCommunityClan TransformCommunityClanToDaoCommunityClan(CommunityClan communityClan ) {
		
		DaoCommunityClan daoCommunityClan = new DaoCommunityClan();
		daoCommunityClan.setStatus( communityClan.getStatus());
		daoCommunityClan.setStatus_code(communityClan.getStatus_code());
		
		daoCommunityClan.setIdClan(communityClan.getIdClan());
		daoCommunityClan.setDateCommunityClan(communityClan.getDateCommunityClan());
		
		daoCommunityClan.setData(TransformDataCommunityClanToDaoDataCommunityClan(communityClan.getData()));
		return daoCommunityClan;
	}

	private static DaoDataCommunityClan TransformDataCommunityClanToDaoDataCommunityClan(DataCommunityClan data) {
		DaoDataCommunityClan myDaoDataCommunityClan = new DaoDataCommunityClan();
		myDaoDataCommunityClan.setMembers(TransformListDataCommunityClanMembersToListDaoDataCommunityClanMembers(data.getMembers()));
		
		return myDaoDataCommunityClan;
	}

	private static List<DaoDataCommunityClanMembers> TransformListDataCommunityClanMembersToListDaoDataCommunityClanMembers(List<DataCommunityClanMembers> members) {
		List<DaoDataCommunityClanMembers> listDaoDataCommunityClanMembers = new ArrayList<DaoDataCommunityClanMembers>();
		
		//transform each ItemsDataClan to DAOItemsDataClan
		for (DataCommunityClanMembers member : members) {
			listDaoDataCommunityClanMembers.add(TransformDataCommunityClanMembersToDaoDataCommunityClanMembers(member));
		}
		return listDaoDataCommunityClanMembers;
	}

	private static DaoDataCommunityClanMembers TransformDataCommunityClanMembersToDaoDataCommunityClanMembers(DataCommunityClanMembers member) {
		DaoDataCommunityClanMembers myDaoDataCommunityClanMembers = new DaoDataCommunityClanMembers();
		
		myDaoDataCommunityClanMembers.setAccount_id(member.getAccount_id());
		myDaoDataCommunityClanMembers.setAccount_name(member.getAccount_name());
		
		return myDaoDataCommunityClanMembers;
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

	private static DaoDataCommunityAccount TransformDataCommunityAccountToDaoDataCommunityAccount(DataCommunityAccount data) {
		DaoDataCommunityAccount myDaoDataCommunityAccount = new DaoDataCommunityAccount();
		myDaoDataCommunityAccount.setName(data.getName());
		
		myDaoDataCommunityAccount.setAchievements(TransformDataCommunityAccountAchievementsToDaoDataCommunityAccountAchievements(data.getAchievements()));
		myDaoDataCommunityAccount.setStats(TransformDataCommunityAccountStatsToDaoDataCommunityAccountStats(data.getStats()));
		return myDaoDataCommunityAccount;
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
