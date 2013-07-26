package com.wot.client;

import java.util.List;

import com.wot.shared.AllCommunityAccount;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;
import com.wot.shared.FieldVerifier;
import com.wot.shared.ItemsDataClan;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WotTest1 implements EntryPoint {
	String idClan ="" ;
	int offsetClan = 0;
	int limitClan = 100;
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final WotServiceAsync wotService = GWT
			.create(WotService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		
		
		
		final Label errorLabel = new Label();

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel rootPanel = RootPanel.get();
		//RootPanel.get("errorLabelContainer").add(errorLabel);
		
		DockPanel dockPanel = new DockPanel();
		rootPanel.add(dockPanel, 29, 259);
		dockPanel.setSize("1193px", "550px");
		
		final Grid grid = new Grid(10, 2);
		dockPanel.add(grid, DockPanel.SOUTH);
		grid.setSize("1193px", "464px");
		
		final TextBox nameClan = new TextBox();
		
		
		rootPanel.add(nameClan, 83, 10);
		nameClan.setText("NOVA SNAIL");
		nameClan.setSize("125px", "18px");

		nameClan.addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				// TODO Auto-generated method stub
				offsetClan = 0;
				limitClan = 100;
			}
		});
		
		
		
		// Focus the cursor on the name field when the app loads
		nameClan.setFocus(true);
		////////////
								
		///////////////////////
		final Button searchClanButton = new Button("Recherche un clan");
		rootPanel.add(searchClanButton, 224, 12);
		
		Label lblNewLabel = new Label("Clan");
		lblNewLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(lblNewLabel, 26, 16);
		lblNewLabel.setSize("52px", "24px");
		
		final TextArea mottoClan = new TextArea();
		rootPanel.add(mottoClan, 116, 81);
		mottoClan.setSize("667px", "29px");
		
		final Image imageClan = new Image();
		rootPanel.add(imageClan, 29, 80);
		imageClan.setSize("68px", "40px");
		
		final TextArea ownerClan = new TextArea();
		rootPanel.add(ownerClan, 117, 137);
		ownerClan.setSize("78px", "18px");
		
		Label owner = new Label("Propri\u00E9taire");
		owner.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(owner, 29, 137);
		owner.setSize("68px", "24px");
		
		Label lblNombresDeMenbres = new Label("Nombres de membres");
		lblNombresDeMenbres.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(lblNombresDeMenbres, 229, 137);
		lblNombresDeMenbres.setSize("82px", "39px");
		
		final TextArea nbMembersClan = new TextArea();
		rootPanel.add(nbMembersClan, 317, 137);
		nbMembersClan.setSize("78px", "18px");
		
		Label labelAbbrevClan = new Label("Abr\u00E9viation CLAN");
		labelAbbrevClan.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(labelAbbrevClan, 425, 137);
		labelAbbrevClan.setSize("68px", "39px");
		
		final TextArea abbrevClan = new TextArea();
		rootPanel.add(abbrevClan, 513, 137);
		abbrevClan.setSize("78px", "18px");
		
		Label lblIcne = new Label("Embl\u00EAme");
		lblIcne.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(lblIcne, 29, 50);
		lblIcne.setSize("68px", "24px");
		
		Label lblDec = new Label("Motto");
		lblDec.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rootPanel.add(lblDec, 116, 51);
		lblDec.setSize("127px", "24px");
		
		Button findMembersClanButton = new Button("Send");
		
		//
		
		findMembersClanButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		});
		
		findMembersClanButton.setText("Chercher membres du CLAN");
		rootPanel.add(findMembersClanButton, 29, 195);
		findMembersClanButton.setSize("214px", "28px");
		
		Button searchClansButton = new Button("Rechercher Des clans");
		rootPanel.add(searchClansButton, 374, 12);
		searchClansButton.setSize("146px", "28px");
		

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		
		
		
		
		
		
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				searchClanButton.setEnabled(true);
				searchClanButton.setFocus(true);
			}
		});

		// Create a handler for the Button search clan 
		class HandlerGetClan implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				getClan();
				offsetClan = 0;
				limitClan = 100;
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					getClan();
					offsetClan = 0;
					limitClan = 100;
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void getClan() {
				grid.resizeRows(0);
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameClan.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				searchClanButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				wotService.getClan(textToServer,
						new AsyncCallback<Clan>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(Clan result) {
								//dialogBox.setText("Remote Procedure Call");
								//serverResponseLabel
								//		.removeStyleName("serverResponseLabelError");
								//serverResponseLabel.setHTML(result);
								//write to grid name; batailles name;batailles
								mottoClan.setText(result.getData().getItems().get(0).getMotto());
								imageClan.setUrl(result.getData().getItems().get(0).getClan_emblem_url());
								ownerClan.setText(result.getData().getItems().get(0).getOwner());
								nbMembersClan.setText(result.getData().getItems().get(0).getMember_count());
								abbrevClan.setText(result.getData().getItems().get(0).getAbbreviation());
								idClan = result.getData().getItems().get(0).getId();
								//dialogBox.center();
								//closeButton.setFocus(true);
							}
							
					});
				searchClanButton.setEnabled(true);
				searchClanButton.setFocus(true);
			}
			
			
			
		}

		////
		
		
		// Create a handler for the Button search clansss
		class HandlerGetClans implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				getClans();
				
						offsetClan = offsetClan + 100;
						limitClan = 100;
				
				
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					getClans();
				}
			}

			
			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void getClans() {
				grid.resizeRows(0);
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameClan.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				searchClanButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				wotService.getClans(textToServer ,offsetClan,
						new AsyncCallback<Clan>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(Clan listClan) {
								//dialogBox.setText("Remote Procedure Call");
								//serverResponseLabel
								//		.removeStyleName("serverResponseLabelError");
								//serverResponseLabel.setHTML(result);
								//write to grid name; batailles name;batailles
								
								
								int row = 0;
								int col = 0;
								grid.resizeRows(listClan.getData().getItems().size());
								grid.resizeColumns(5);
								grid.setBorderWidth(2);
								grid.setCellPadding(5);
								grid.setCellSpacing(5);
								
								for (ItemsDataClan clan : listClan.getData().getItems()) {
									
									grid.setText(row, col,clan.getMotto());col++;
									
									
									grid.setText(row, col,clan.getName());col++;
									
									grid.setText(row, col,clan.getOwner());col++;
									
									grid.setText(row, col,clan.getMember_count());col++;
									grid.setText(row, col, clan.getAbbreviation());col++;
									//idClan = clan.getData().getItems().get(0).getId();
									row ++ ; col =0;
								}
							
								//closeButton.setFocus(true);
								
								
							}
							
					});
				searchClanButton.setEnabled(true);
				searchClanButton.setFocus(true);
			}
			
			
			
		}

		////
		
		///////////
		// Create a handler for the sendButton and nameField
		class HandlerGetMembersClan implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				getMembersClan();
				offsetClan = 0;
				limitClan = 100;
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					getMembersClan();
					offsetClan = 0;
					limitClan = 100;
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void getMembersClan() {
				grid.resizeRows(0);
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = idClan;
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				searchClanButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				wotService.getMembersClan(textToServer,
						new AsyncCallback<AllCommunityAccount>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							
							public void onSuccess(AllCommunityAccount listAccount) {
								//write to grid name; batailles name;batailles
								int row = 0;
								int col = 0;
								grid.resizeRows(listAccount.getListCommunityAccount().size() + 1);
								grid.resizeColumns(12);  //les  colonnes
								grid.setBorderWidth(1);
								grid.setCellPadding(1);
								grid.setCellSpacing(1);
								
								
								//les entetes
								grid.setText(row, col, "Soldier"); col++;
								grid.setText(row, col, String.valueOf("Battles")); col++;
								grid.setText(row, col, String.valueOf("Perf Moy Battle"));col++; //battleAvgPerf Perf Moy Battle
								grid.setText(row, col, String.valueOf("Xp Moy Battle"));col++; //battleAvgXp Xp moy bataille
								grid.setText(row, col, String.valueOf("Victories"));col++; //battleWins Victories
								grid.setText(row, col, String.valueOf("Capture Points"));col++; //ctfPoints Cpature points
								grid.setText(row, col, String.valueOf("Damage"));col++; //damageDealt Damage
								grid.setText(row, col, String.valueOf("Defense Points"));col++;  //droppedCtfPoints defense points
								grid.setText(row, col, String.valueOf("Destroyed"));col++; //frags = destroyed
								grid.setText(row, col, String.valueOf("integratedRating"));col++;
								grid.setText(row, col, String.valueOf("Detected"));col++;
								grid.setText(row, col, String.valueOf("xp"));col++;
								
								
								row ++ ; col =0;
								
								//les données 
								for(CommunityAccount myCommunityAccount : listAccount.getListCommunityAccount()) {
									int battle = myCommunityAccount.getData().getStats().getBattles();
									String name = myCommunityAccount.getName();
									int  battleAvgPerf = myCommunityAccount.getData().getStats().getBattle_avg_performance();
									int  battleAvgXp = myCommunityAccount.getData().getStats().getBattle_avg_xp();
									int  battleWins = myCommunityAccount.getData().getStats().getBattle_wins();
									int  ctfPoints = myCommunityAccount.getData().getStats().getCtf_points();
									int  damageDealt = myCommunityAccount.getData().getStats().getDamage_dealt();
									int  droppedCtfPoints = myCommunityAccount.getData().getStats().getDropped_ctf_points();
									int  frags = myCommunityAccount.getData().getStats().getFrags();
									int  integratedRating = myCommunityAccount.getData().getStats().getIntegrated_rating();
									int  spotted = myCommunityAccount.getData().getStats().getSpotted();
									int  xp = myCommunityAccount.getData().getStats().getXp();
									
									
									//set dans les colonnes
									grid.setText(row, col, name); col++;
									grid.setText(row, col, String.valueOf(battle)); col++;
									grid.setText(row, col, String.valueOf(battleAvgPerf));col++;
									grid.setText(row, col, String.valueOf(battleAvgXp));col++;
									grid.setText(row, col, String.valueOf(battleWins));col++;
									grid.setText(row, col, String.valueOf(ctfPoints));col++;
									grid.setText(row, col, String.valueOf(damageDealt));col++;
									grid.setText(row, col, String.valueOf(droppedCtfPoints));col++;
									grid.setText(row, col, String.valueOf(frags));col++;
									grid.setText(row, col, String.valueOf(integratedRating));col++;
									grid.setText(row, col, String.valueOf(spotted));col++;
									grid.setText(row, col, String.valueOf(xp));col++;
									
									
									row ++ ; col =0;
								}
								
								//dialogBox.center();
								//closeButton.setFocus(true);
							}
//							
							public void onSuccess2(String result ) {
								//write to grid name; batailles name;batailles
								String tabTes [] = result.split(" ");
								int row = 0;
								int col = 0;
								grid.resizeRows(tabTes.length);
								grid.resizeColumns(5);  //cinq colonnes
								grid.setBorderWidth(2);
								grid.setCellPadding(5);
								grid.setCellSpacing(5);
								for (int i = 0 ; i < tabTes.length; i++ ) {
									String ele = tabTes[i];  //name;battle
									String name = ele.substring(0,  ele.indexOf(";"));
									String battle = ele.substring(ele.indexOf(";")+1);
									grid.setText(row, col, name); col++;
									grid.setText(row, col, battle); 
									row ++ ; col =0;
									
								}
								
								
								
								//dialogBox.center();
								//closeButton.setFocus(true);
							}
						});
				searchClanButton.setEnabled(true);
				searchClanButton.setFocus(true);
			}
			
			
			
		}
		
		////
		// Add a handler to send the name to the server
		HandlerGetMembersClan handlerFindMembers = new HandlerGetMembersClan();
		findMembersClanButton.addClickHandler(handlerFindMembers);
				
				

		// Add a handler to send the name to the server
		HandlerGetClan handler = new HandlerGetClan();
		searchClanButton.addClickHandler(handler);
		nameClan.addKeyUpHandler(handler);
		
		// Add a handler to find clans
		HandlerGetClans handlerGetClans = new HandlerGetClans();
		searchClansButton.addClickHandler(handlerGetClans);
		nameClan.addKeyUpHandler(handlerGetClans);

		
		//second button
		
	}
}
