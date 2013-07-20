package com.wot.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wot.server.Clan;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<Clan> callback)
			throws IllegalArgumentException;

	void findMembersClan(String textToServer,
			AsyncCallback<String> asyncCallback);
	
	
}
