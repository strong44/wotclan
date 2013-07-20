package com.wot.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wot.server.Clan;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface WotServiceAsync {
	void getClan(String input, AsyncCallback<Clan> callback)
			throws IllegalArgumentException;

	void getMembersClan(String textToServer,
			AsyncCallback<String> asyncCallback);
	
	
}
