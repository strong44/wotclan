package com.wot.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.wot.server.Clan;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	Clan greetServer(String clan) throws IllegalArgumentException;
	String findMembersClan(String idClan) throws IllegalArgumentException;
}
