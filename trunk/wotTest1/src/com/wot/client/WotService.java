package com.wot.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface WotService extends RemoteService {
	Clan getClan(String clan) throws IllegalArgumentException;
	Clan getClans(String clan, int offset) throws IllegalArgumentException;
	
	AllCommunityAccount getMembersClan(String idClan) throws IllegalArgumentException;
}
