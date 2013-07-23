package com.wot.server.api;

import java.util.ArrayList;
import java.util.List;

import com.wot.server.DaoClan;
import com.wot.server.DaoDataClan;
import com.wot.server.DaoItemsDataClan;
import com.wot.shared.Clan;
import com.wot.shared.DataClan;
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
	
	
	
	
	
}
