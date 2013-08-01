package com.wot.client;

import java.util.Date;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.Binder;

import com.wot.client.ContactDatabase.Category;
import com.wot.client.ContactDatabase.ContactInfo;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;
import com.wot.shared.FieldVerifier;
import com.wot.shared.ItemsDataClan;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.core.java.util.Arrays;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;





/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WotTest1 implements EntryPoint {
	String idClan ="" ;
	int offsetClan = 0;
	int limitClan = 100;
	
	  
	  RootPanel rootPanel ;
	  DockPanel dockPanel;
	  SimplePager pager;
	  CellTable<CommunityAccount> tableCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);
	  
	// Create a data provider.
	  ListDataProvider<CommunityAccount> dataProvider = new ListDataProvider<CommunityAccount>(CommunityAccount.KEY_PROVIDER);
	    
 

	//
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

	
	
	/*
	 * call this when we have data to put in table
	 */
	public  void buildACellTableForCommunityAccountWithData(List<CommunityAccount> listCommAcc) {
	    
	    //update dataprovider with some known list 
	    dataProvider.setList(listCommAcc);
		
		// Create a CellTable.
	    //CellTable<CommunityAccount> table = new CellTable<CommunityAccount>();
		tableCommAcc.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	    
	    
	    ListHandler<CommunityAccount> columnSortHandler =
		        new ListHandler<CommunityAccount>(dataProvider.getList());
	    tableCommAcc.addColumnSortHandler(columnSortHandler);
	    
//	  //Create a Pager to control the table.
//	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
//	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
//	    pager.setDisplay(tableCommAcc);
	    
	    // Add a text column to show the name.
	    TextColumn<CommunityAccount> nameColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return object.getName();
	      }
	    };
	    tableCommAcc.addColumn(nameColumn, "Name");

	    nameColumn.setSortable(true);
	    
	 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    // java.util.List.
	    columnSortHandler.setComparator(nameColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the name columns.
	            if (o1 != null) {
	              return (o2 != null) ? o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase()) : 1;
	            }
	            return -1;
	          }
	        });
	    //tableCommAcc.addColumnSortHandler(columnSortHandler);
	    
	 // We know that the data is sorted alphabetically by default.
	    tableCommAcc.getColumnSortList().push(nameColumn);
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> idColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return object.getIdUser();
	      }
	    };
	    tableCommAcc.addColumn(idColumn, "User Id");

	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> wrColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_avg_performance());
	      }
	    };
	    tableCommAcc.addColumn(wrColumn, "Win rate");
	    
	    wrColumn.setSortable(true);
	    
	 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    // java.util.List.
	    columnSortHandler.setComparator(wrColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	int val1 = o1.getData().getStats().getBattle_avg_performance();
	            	int val2 = o2.getData().getStats().getBattle_avg_performance();
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> avgXpColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_avg_xp() );
	      }
	    };
	    tableCommAcc.addColumn(avgXpColumn, "Avg Xp");
	    
	    avgXpColumn.setSortable(true);
	    
	 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    // java.util.List.
	    columnSortHandler.setComparator(avgXpColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	int val1 = o1.getData().getStats().getBattle_avg_xp();
	            	int val2 = o2.getData().getStats().getBattle_avg_xp();
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    
	    // Add a text column 
	    TextColumn<CommunityAccount> battleWinsColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_wins() );
	      }
	    };
	    tableCommAcc.addColumn(battleWinsColumn, "Victories");
	    
	    battleWinsColumn.setSortable(true);
	    
	 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    // java.util.List.
	    columnSortHandler.setComparator(battleWinsColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	int val1 = o1.getData().getStats().getBattle_wins();
	            	int val2 = o2.getData().getStats().getBattle_wins();
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> battlesColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattles() );
	      }
	    };
	    tableCommAcc.addColumn(battlesColumn, "Battles");

	    //////
	    battlesColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    // java.util.List.
		    columnSortHandler.setComparator(battlesColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getStats().getBattles();
		            	int val2 = o2.getData().getStats().getBattles();
		              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> ctfColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getCtf_points() );
	      }
	    };
	    tableCommAcc.addColumn(ctfColumn, "Capture Points");

	    ctfColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    // java.util.List.
		    columnSortHandler.setComparator(ctfColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getStats().getCtf_points();
		            	int val2 = o2.getData().getStats().getCtf_points();
		              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> dmgColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getDamage_dealt() );
	      }
	    };
	    tableCommAcc.addColumn(dmgColumn, "Damage");
	    dmgColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    // java.util.List.
		    columnSortHandler.setComparator(dmgColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getStats().getDamage_dealt();
		            	int val2 = o2.getData().getStats().getDamage_dealt();
		              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> dropCtfColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getDropped_ctf_points() );
	      }
	    };
	    tableCommAcc.addColumn(dropCtfColumn, "Defense Points");
	    dropCtfColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    // java.util.List.
		    columnSortHandler.setComparator(dropCtfColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getStats().getDropped_ctf_points();
		            	int val2 = o2.getData().getStats().getDropped_ctf_points();
		              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> fragsColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getFrags() );
	      }
	    };
	    tableCommAcc.addColumn(fragsColumn, "Destroyed");
	    fragsColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    // java.util.List.
		    columnSortHandler.setComparator(fragsColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getStats().getFrags();
		            	int val2 = o2.getData().getStats().getFrags();
		              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> irColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getIntegrated_rating() );
	      }
	    };
	    tableCommAcc.addColumn(irColumn, "Integrated Rating");
	    irColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    // java.util.List.
		    columnSortHandler.setComparator(irColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getStats().getIntegrated_rating();
		            	int val2 = o2.getData().getStats().getIntegrated_rating();
		              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
	
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> spottedColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getSpotted() );
	      }
	    };
	    tableCommAcc.addColumn(spottedColumn, "Detected");

	    spottedColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    // java.util.List.
		    columnSortHandler.setComparator(spottedColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getStats().getSpotted();
		            	int val2 = o2.getData().getStats().getSpotted();
		              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> xpColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getXp() );
	      }
	    };
	    tableCommAcc.addColumn(xpColumn, "Experience");

	    xpColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    // java.util.List.
		    columnSortHandler.setComparator(battleWinsColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getStats().getXp();
		            	int val2 = o2.getData().getStats().getXp();
		              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<CommunityAccount> selectionModel = new SingleSelectionModel<CommunityAccount>();
	    tableCommAcc.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	  CommunityAccount selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	          Window.alert("You selected: " + selected.getName());
	        }
	      }
	    });

	    // Set the total row count. This isn't strictly necessary, but it affects
	    // paging calculations, so its good habit to keep the row count up to date.
	    
	    tableCommAcc.setRowCount(listCommAcc.size(), true); //no need to do here because we have add list to data provider

	    // Push the data into the widget.
	    tableCommAcc.setRowData(0, listCommAcc);            //idem no nedd dataprovider
	    
	 // Connect the table to the data provider.
	    dataProvider.addDataDisplay(tableCommAcc);
	    dataProvider.refresh();
    
   }
	

	/**
		 * This is the entry point method.
		 */
	public void onModuleLoad() {
			
			
			final Label errorLabel = new Label();
	
			// Add the nameField and sendButton to the RootPanel
			// Use RootPanel.get() to get the entire body element
			rootPanel = RootPanel.get();
			//RootPanel.get("errorLabelContainer").add(errorLabel);
			
			dockPanel = new DockPanel();
			rootPanel.add(dockPanel, 29, 259);
			dockPanel.setSize("1193px", "550px");
			
			/////////
			final Grid grid = new Grid(10, 2);
			dockPanel.add(grid, DockPanel.SOUTH);
			grid.setSize("1193px", "464px");
			
			//nouveau tableau des membres
			
//			tableCommAcc.setVisible(false);
//			dockPanel.add(tableCommAcc, DockPanel.SOUTH);
			//rootPanel.add(tableCommAcc);
			
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
					tableCommAcc.setVisible(false);
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
			
			
			// Create a handler for the Button search all clans
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
					tableCommAcc.setVisible(false);
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
			// Create a handler for search clan's members
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
									
									dockPanel.remove(tableCommAcc);
									if (dataProvider.getDataDisplays()!= null && !dataProvider.getDataDisplays().isEmpty()) 
										dataProvider.removeDataDisplay(tableCommAcc);
									
									//construct column in celltable tableCommAcc , set data set sort handler etc ..
									buildACellTableForCommunityAccountWithData(listAccount.getListCommunityAccount());
									  
									//Create a Pager to control the table.
								    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
								    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
								    pager.setDisplay(tableCommAcc);
									
							    
								    //add to dock panel ======
								    dockPanel.add(pager, DockPanel.SOUTH);
									pager.setPage(10);
									pager.setVisible(true);
									
									dockPanel.add(tableCommAcc, DockPanel.SOUTH);
									tableCommAcc.setVisible(true);
								    
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
