package com.wot.client;

import com.wot.server.Clan;
import com.wot.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WotTest1 implements EntryPoint {
	String idClan ="" ;
	
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
		final TextBox nameField = new TextBox();
		rootPanel.add(nameField, 83, 10);
		nameField.setText("NOVA SNAIL");
		nameField.setSize("125px", "18px");

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		////////////
								
		///////////////////////
		final Button sendButton = new Button("Recherche un clan");
		rootPanel.add(sendButton, 224, 12);
		
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
		
		findMembersClanButton.setText("Chercher membres");
		rootPanel.add(findMembersClanButton, 29, 195);
		findMembersClanButton.setSize("68px", "28px");
		
		Frame frameSearchResultClan = new Frame("http://www.google.com");
		rootPanel.add(frameSearchResultClan, 625, 81);
		frameSearchResultClan.setSize("300px", "350px");
		
		Button searchClanButton = new Button("recherche Des clans");
		rootPanel.add(searchClanButton, 374, 12);
		searchClanButton.setSize("146px", "28px");
		

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
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the Button search clan 
		class HandlerGetClan implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				getClan();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					getClan();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void getClan() {
				grid.resizeRows(0);
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
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
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
			
			
			
		}

		///////////
		// Create a handler for the sendButton and nameField
		class HandlerGetMembersClan implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				getMembersClan();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					getMembersClan();
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
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				wotService.getMembersClan(textToServer,
						new AsyncCallback<String>() {
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

//							
							public void onSuccess(String  result) {
								//write to grid name; batailles name;batailles
								String tabTes [] = result.split(" ");
								int row = 0;
								int col = 0;
								grid.resizeRows(tabTes.length);
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
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
			
			
			
		}
		
		////
		// Add a handler to send the name to the server
		HandlerGetMembersClan handlerFindMembers = new HandlerGetMembersClan();
		findMembersClanButton.addClickHandler(handlerFindMembers);
				
				

		// Add a handler to send the name to the server
		HandlerGetClan handler = new HandlerGetClan();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
		
		//second button
		
	}
}
