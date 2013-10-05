package com.wot.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;
import com.wot.shared.CommunityClan;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface WotServiceAsync {
	void getClan(String input, AsyncCallback<Clan> callback)
			throws IllegalArgumentException;

	void getClans(String input, int offsetClan, AsyncCallback<Clan> callback)
			throws IllegalArgumentException;
	
	void getAllMembersClanAndStats(String textToServer,
			List<String> listIdUser, AsyncCallback<AllCommunityAccount> callBack);
	
	void getAllMembersClan(String textToServer,
			AsyncCallback<CommunityClan> asyncCallback);
}
