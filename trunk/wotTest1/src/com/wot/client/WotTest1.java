package com.wot.client;






import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import java.util.HashMap;

import java.util.List;
import java.util.Set;





















import com.google.gwt.cell.client.Cell;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;

import com.google.gwt.user.client.rpc.AsyncCallback;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;


import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;



import com.wot.shared.AllCommunityAccount;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;
import com.wot.shared.CommunityClan;
import com.wot.shared.DataCommunityAccount;
import com.wot.shared.DataCommunityAccountVehicules;
import com.wot.shared.DataCommunityClanMembers;
import com.wot.shared.DataCommunityMembers;
import com.wot.shared.FieldVerifier;
import com.wot.shared.ItemsDataClan;
import com.wot.shared.XmlListAchievement;
import com.wot.shared.XmlListCategoryAchievement;
import com.wot.shared.XmlSrc;
import com.wot.shared.XmlWiki;





/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WotTest1 implements EntryPoint {
	static boolean adminLogin = false ;
	static String noData = "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQ8KRYghA2Xyp8gWTkK4ZNtBQL2nixsiYdAFDeFBCaj_ylXcfhK";
	XmlWiki xmlWiki = null;
	String idClan ="" ;
	int offsetClan = 0;
	int limitClan = 100;
	
	int passSaving = 0;
	int nbUsersToTreat = 30;
	
	  RootPanel rootPanel ;
	  DockPanel dockPanel;
	  
	  //m�canisme de pagination
	  SimplePager pagerStatsCommunityAccount;
	  SimplePager pagerHistorizedStatsCommunityAccount;
	  SimplePager pagerHistorizedStatsTanksCommunityAccount;
	  
	  SimplePager pagerClan;
	  SimplePager pagerAchievementsCommunityAccount;
	  
	  //tableau des stats joueurs
	  CellTable<CommunityAccount> tableStatsCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);
	  
	  //tableau des stats historisés des joueurs
	  CellTable<CommunityAccount> tableHistorizedStatsCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);

	  //tableau des stats tanks historisés des joueurs
	  CellTable<CommunityAccount> tableHistorizedStatsTanksCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);

	  //tableau des stats joueurs
	  CellTable<CommunityAccount> tableAchivementCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);

	  //tableau des clans
	  CellTable<ItemsDataClan> tableClan = new  CellTable<ItemsDataClan> (ItemsDataClan.KEY_PROVIDER);
	  
	  // Create a data provider for tab players.
	  ListDataProvider<CommunityAccount> dataStatsProvider = new ListDataProvider<CommunityAccount>(CommunityAccount.KEY_PROVIDER);

	  // Create a data provider for tab players historized .
	  ListDataProvider<CommunityAccount> dataHistorizedStatsProvider = new ListDataProvider<CommunityAccount>(CommunityAccount.KEY_PROVIDER);

	  // Create a data provider for tab players historized .
	  ListDataProvider<CommunityAccount> dataHistorizedStatsTanksProvider = new ListDataProvider<CommunityAccount>(CommunityAccount.KEY_PROVIDER);
		  
	  // Create a data provider for achievement players.
	  ListDataProvider<CommunityAccount> dataAchievementsProvider = new ListDataProvider<CommunityAccount>(CommunityAccount.KEY_PROVIDER);
	  
	  //create data provider for tab clans
	  ListDataProvider<ItemsDataClan> dataClanProvider = new ListDataProvider<ItemsDataClan>(ItemsDataClan.KEY_PROVIDER);
	     
	  HashMap<String, String>  hmAccNameAccId =new HashMap<String, String >();
	  HashMap<String, String>  hmAccIdAccName =new HashMap<String, String >();


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
	public  void buildACellTableForStatsCommunityAccount(List<CommunityAccount> listCommAcc) {
	    
		tableStatsCommAcc.setPageSize(30);
		
	    //update dataprovider with some known list 
	    dataStatsProvider.setList(listCommAcc);
		
		// Create a CellTable.
		tableStatsCommAcc.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	    
	    
	    ListHandler<CommunityAccount> columnSortHandler =
		        new ListHandler<CommunityAccount>(dataStatsProvider.getList());
	    tableStatsCommAcc.addColumnSortHandler(columnSortHandler);
	    
	    
	    // Add a text column to show the name.
	    TextColumn<CommunityAccount> nameColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	    	  //map accountId/ accountName
	    	  //hmAccIdAccName.get(object.getIdUser());
	        return hmAccIdAccName.get(object.getIdUser());
	      }
	    };
	    tableStatsCommAcc.addColumn(nameColumn, "Name");

	    nameColumn.setSortable(true);
	    
	 // Add a ColumnSortEvent.ListHandler to connect sorting to the
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
	    
	 // We know that the data is sorted alphabetically by default.
	    tableStatsCommAcc.getColumnSortList().push(nameColumn);
	    
	    // Add a text column to show the user id.
	    TextColumn<CommunityAccount> idColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return object.getIdUser();
	      }
	    };
	    tableStatsCommAcc.addColumn(idColumn, "User Id");

	    idColumn.setSortable(true);
	    
	 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(idColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the name columns.
	            if (o1 != null) {
	              return (o2 != null) ? o1.getIdUser().toUpperCase().compareTo(o2.getIdUser().toUpperCase()) : 1;
	            }
	            return -1;
	          }
	        });
	    
//	    // Add a text column to show the win rate.
//	    TextColumn<CommunityAccount> wrColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getBattle_avg_performance());
//	      }
//	    };
//	    tableCommAcc.addColumn(wrColumn, "Win rate");
//	    
//	    wrColumn.setSortable(true);
//	    
//	    // Add a ColumnSortEvent.ListHandler to connect sorting to the
//	    columnSortHandler.setComparator(wrColumn,
//	        new Comparator<CommunityAccount>() {
//	          public int compare(CommunityAccount o1, CommunityAccount o2) {
//	            if (o1 == o2) {
//	              return 0;
//	            }
//
//	            // Compare the columns.
//	            if (o1 != null) {
//	            	int val1 = o1.getData().getBattle_avg_performance();
//	            	int val2 = o2.getData().getBattle_avg_performance();
//	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//	            }
//	            return -1;
//	          }
//	        });

	    
	    
	    // Add a text column to show the win rate.
	    TextColumn<CommunityAccount> wrCalcColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getBattle_avg_performanceCalc());
	      }
	    };
	    tableStatsCommAcc.addColumn(wrCalcColumn, "Win rate");
	    
	    wrCalcColumn.setSortable(true);
	    
	    // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(wrCalcColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	Double val1 = o1.getData().getBattle_avg_performanceCalc();
	            	Double val2 = o2.getData().getBattle_avg_performanceCalc();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    

	    
	    
	    // Add a text column battleWinsColumn
	    TextColumn<CommunityAccount> battleWinsColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getBattle_wins() );
	      }
	    };
	    tableStatsCommAcc.addColumn(battleWinsColumn, "Victories");
	    
	    battleWinsColumn.setSortable(true);
	    
	    // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(battleWinsColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	int val1 = o1.getData().getBattle_wins();
	            	int val2 = o2.getData().getBattle_wins();
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });
	    
	    // Add a text column to show battlesColumn
	    TextColumn<CommunityAccount> battlesColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getBattles() );
	      }
	    };
	    tableStatsCommAcc.addColumn(battlesColumn, "Battles");

	    //////
	    battlesColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(battlesColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	int val1 = o1.getData().getBattles();
	            	int val2 = o2.getData().getBattles();
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });
	    
		    
//	    // Add a text column to show ctfColumn
//	    TextColumn<CommunityAccount> ctfColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getCtf_points() );
//	      }
//	    };
//	    tableCommAcc.addColumn(ctfColumn, "Capture Points");
//
//	    ctfColumn.setSortable(true);
//	    
//		// Add a ColumnSortEvent.ListHandler to connect sorting to the
//	    columnSortHandler.setComparator(ctfColumn,
//	        new Comparator<CommunityAccount>() {
//	          public int compare(CommunityAccount o1, CommunityAccount o2) {
//	            if (o1 == o2) {
//	              return 0;
//	            }
//
//	            // Compare the columns.
//	            if (o1 != null) {
//	            	int val1 = o1.getData().getCtf_points();
//	            	int val2 = o2.getData().getCtf_points();
//	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//	            }
//	            return -1;
//	          }
//	        });
//	    
	    //Add column to show ratio capture points
	    TextColumn<CommunityAccount> ratioCtfColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getRatioCtfPoints());
	      }
	    };
	    tableStatsCommAcc.addColumn(ratioCtfColumn, "Avg capture points");
	    
	    ratioCtfColumn.setSortable(true);
	    
	    // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(ratioCtfColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	Double val1 = o1.getData().getRatioCtfPoints();
	            	Double val2 = o2.getData().getRatioCtfPoints();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    
	    
//	    // Add a text column to show dmgColumn
//	    TextColumn<CommunityAccount> dmgColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getDamage_dealt() );
//	      }
//	    };
//	    tableCommAcc.addColumn(dmgColumn, "Damage");
//	    dmgColumn.setSortable(true);
//	    
//		// Add a ColumnSortEvent.ListHandler to connect sorting to the
//	    columnSortHandler.setComparator(dmgColumn,
//	        new Comparator<CommunityAccount>() {
//	          public int compare(CommunityAccount o1, CommunityAccount o2) {
//	            if (o1 == o2) {
//	              return 0;
//	            }
//
//	            // Compare the columns.
//	            if (o1 != null) {
//	            	int val1 = o1.getData().getDamage_dealt();
//	            	int val2 = o2.getData().getDamage_dealt();
//	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//	            }
//	            return -1;
//	          }
//	        });
//	    
	    //Add column to show ratio damage points ratio
	    TextColumn<CommunityAccount> ratioDamageColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getRatioDamagePoints());
	      }
	    };
	    tableStatsCommAcc.addColumn(ratioDamageColumn, "Avg damage points");
	    
	    ratioDamageColumn.setSortable(true);
	    
	    // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(ratioDamageColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	Double val1 = o1.getData().getRatioDamagePoints();
	            	Double val2 = o2.getData().getRatioDamagePoints();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    // Add a text column to show dropCtfColumn
//	    TextColumn<CommunityAccount> dropCtfColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getDropped_ctf_points() );
//	      }
//	    };
//	    tableCommAcc.addColumn(dropCtfColumn, "Defense Points");
//	    dropCtfColumn.setSortable(true);
//	    
//		// Add a ColumnSortEvent.ListHandler to connect sorting to the
//	    columnSortHandler.setComparator(dropCtfColumn,
//	        new Comparator<CommunityAccount>() {
//	          public int compare(CommunityAccount o1, CommunityAccount o2) {
//	            if (o1 == o2) {
//	              return 0;
//	            }
//
//	            // Compare the columns.
//	            if (o1 != null) {
//	            	int val1 = o1.getData().getDropped_ctf_points();
//	            	int val2 = o2.getData().getDropped_ctf_points();
//	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//	            }
//	            return -1;
//	          }
//	        });
//	
	    
	    //Add column to show ratio capture points
	    TextColumn<CommunityAccount> ratioDroppedCtfColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getRatioDroppedCtfPoints());
	      }
	    };
	    tableStatsCommAcc.addColumn(ratioDroppedCtfColumn, "Avg Defense points");
	    
	    ratioDroppedCtfColumn.setSortable(true);
	    
	    // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(ratioDroppedCtfColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	Double val1 = o1.getData().getRatioDroppedCtfPoints();
	            	Double val2 = o2.getData().getRatioDroppedCtfPoints();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
//	    // Add a text column to show fragsColumn
//	    TextColumn<CommunityAccount> fragsColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getFrags() );
//	      }
//	    };
//	    tableCommAcc.addColumn(fragsColumn, "Destroyed");
//	    fragsColumn.setSortable(true);
//	    
//		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//	    columnSortHandler.setComparator(fragsColumn,
//	        new Comparator<CommunityAccount>() {
//	          public int compare(CommunityAccount o1, CommunityAccount o2) {
//	            if (o1 == o2) {
//	              return 0;
//	            }
//
//	            // Compare the columns.
//	            if (o1 != null) {
//	            	int val1 = o1.getData().getFrags();
//	            	int val2 = o2.getData().getFrags();
//	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//	            }
//	            return -1;
//	          }
//	        });
//	    
	    //Add column to show avg destroyed
	    TextColumn<CommunityAccount> ratioFragsColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getRatioDestroyedPoints());
	      }
	    };
	    tableStatsCommAcc.addColumn(ratioFragsColumn, "Avg destroyed");
	    
	    ratioFragsColumn.setSortable(true);
	    
	    // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(ratioFragsColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	Double val1 = o1.getData().getRatioDestroyedPoints();
	            	Double val2 = o2.getData().getRatioDestroyedPoints();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });


	    
	    
//	    // Add a text column to show irColumn
//	    TextColumn<CommunityAccount> irColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getIntegrated_rating() );
//	      }
//	    };
//	    tableCommAcc.addColumn(irColumn, "Integrated Rating");
//	    irColumn.setSortable(true);
//	    
//		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    // java.util.List.
//	    columnSortHandler.setComparator(irColumn,
//	        new Comparator<CommunityAccount>() {
//	          public int compare(CommunityAccount o1, CommunityAccount o2) {
//	            if (o1 == o2) {
//	              return 0;
//	            }
//
//	            // Compare the columns.
//	            if (o1 != null) {
//	            	int val1 = o1.getData().getIntegrated_rating();
//	            	int val2 = o2.getData().getIntegrated_rating();
//	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//	            }
//	            return -1;
//	          }
//	        });
//		    
//	
	    
	    // Add a text column to show spottedColumn
//	    TextColumn<CommunityAccount> spottedColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getSpotted() );
//	      }
//	    };
//	    tableCommAcc.addColumn(spottedColumn, "Detected");
//
//	    spottedColumn.setSortable(true);
//	    
//		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    // java.util.List.
//	    columnSortHandler.setComparator(spottedColumn,
//	        new Comparator<CommunityAccount>() {
//	          public int compare(CommunityAccount o1, CommunityAccount o2) {
//	            if (o1 == o2) {
//	              return 0;
//	            }
//
//	            // Compare the columns.
//	            if (o1 != null) {
//	            	int val1 = o1.getData().getSpotted();
//	            	int val2 = o2.getData().getSpotted();
//	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//	            }
//	            return -1;
//	          }
//	        });
//	    
	    //Add column to show avg detected
	    TextColumn<CommunityAccount> ratioDetectedColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getRatioDetectedPoints());
	      }
	    };
	    tableStatsCommAcc.addColumn(ratioDetectedColumn, "Avg detected");
	    
	    ratioDetectedColumn.setSortable(true);
	    
	    // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(ratioDetectedColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	Double val1 = o1.getData().getRatioDetectedPoints();
	            	Double val2 = o2.getData().getRatioDetectedPoints();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    // Add a text column to show xpColumn
	    TextColumn<CommunityAccount> xpColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getXp() );
	      }
	    };
	    tableStatsCommAcc.addColumn(xpColumn, "Experience");

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
	            	int val1 = o1.getData().getXp();
	            	int val2 = o2.getData().getXp();
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });
		    
	    // Add a text column to show avgXpColumn
	    TextColumn<CommunityAccount> avgXpColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getBattle_avg_xp() );
	      }
	    };
	    tableStatsCommAcc.addColumn(avgXpColumn, "Avg Xp");
	    
	    avgXpColumn.setSortable(true);
	    
	    // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(avgXpColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	int val1 = o1.getData().getBattle_avg_xp();
	            	int val2 = o2.getData().getBattle_avg_xp();
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });

	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<CommunityAccount> selectionModel = new SingleSelectionModel<CommunityAccount>();
	    tableStatsCommAcc.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	  CommunityAccount selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	          //Window.alert("You selected: " + selected.getName());
	        }
	      }
	    });

	    // Set the total row count. This isn't strictly necessary, but it affects
	    // paging calculations, so its good habit to keep the row count up to date.
	    
	    tableStatsCommAcc.setRowCount(listCommAcc.size(), true); //no need to do here because we have add list to data provider

	    // Push the data into the widget.
	    tableStatsCommAcc.setRowData(0, listCommAcc);            //idem no nedd dataprovider
	    
	 // Connect the table to the data provider.
	    dataStatsProvider.addDataDisplay(tableStatsCommAcc);
	    dataStatsProvider.refresh();
   }
	

	/*
		 * call this when we have data to put in table
		 */
//		public  void buildACellTableForAchivementsCommunityAccount(List<CommunityAccount> listCommAcc, XmlWiki xmlWiki, String categoryAchievement) {
//		    
//			final HashMap<String, XmlListAchievement> hashMapAch = buidHashMapAchievement(xmlWiki, categoryAchievement);//Battle Hero Achievements - Commemorative Achievements - Epic Achievements (medals) - Special Achievements (titles) - Step Achievements (medals) 
//			
//			tableAchivementCommAcc.setPageSize(30);
//			
//		    //update dataprovider with some known list 
//			dataAchievementsProvider.setList(listCommAcc);
//			
//			// Create a CellTable.
//		    tableAchivementCommAcc.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
//		    
//		    
//		    ListHandler<CommunityAccount> columnSortHandler =
//			        new ListHandler<CommunityAccount>(dataAchievementsProvider.getList());
//		    tableAchivementCommAcc.addColumnSortHandler(columnSortHandler);
//		    
//		    
//		    // Add a text column to show the name.
//		    TextColumn<CommunityAccount> nameColumn = new TextColumn<CommunityAccount>() {
//		      @Override
//		      public String getValue(CommunityAccount object) {
//		        return object.getName();
//		      }
//		    };
//		    tableAchivementCommAcc.addColumn(nameColumn, "Name");
//	
//		    nameColumn.setSortable(true);
//		    
//		    // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(nameColumn,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//	
//		            // Compare the name columns.
//		            if (o1 != null) {
//		              return (o2 != null) ? o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase()) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    
//		    // We know that the data is sorted alphabetically by default.
//		    tableAchivementCommAcc.getColumnSortList().push(nameColumn);
//	    
//		    
//		    // Add a text column to show the battles.
//		    TextColumn<CommunityAccount> battlesColumn = new TextColumn<CommunityAccount>() {
//		      @Override
//		      public String getValue(CommunityAccount object) {
//		        return String.valueOf(object.getData().getBattles());
//		      }
//		    };
//		    tableAchivementCommAcc.addColumn(battlesColumn, "Battles");
//	
//		    battlesColumn.setSortable(true);
//		    
//		    // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(battlesColumn,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//	
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getBattles();
//		            	int val2 = o2.getData().getBattles();
//		              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    
//		    // We know that the data is sorted alphabetically by default.
//		    tableAchivementCommAcc.getColumnSortList().push(battlesColumn);
//	    
//		    
//		    
//		    //============= colonne Top gun  ==========================
//		    MyButtonCell buttonCell = new MyButtonCell() ;
//		    String nameAch = getNameAch(hashMapAch, "Warrior");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Warrior";
//		    	  int val =contact.getData().getAchievements().getWarrior();
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Warrior";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getWarrior();
//		            	int val2 = o2.getData().getAchievements().getWarrior();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    //============= colonne Defender ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Defender");
//
//		    //Column<CommunityAccount, String> column = new Column<CommunityAccount, String>();
//		    
//		    if(nameAch != null) {
//		    //buttonCell.
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Defender";
//		    	  int val =contact.getData().getAchievements().getDefender();
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Defender";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getDefender();
//		            	int val2 = o2.getData().getAchievements().getDefender();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne Hunter ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Beasthunter");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Beasthunter";
//		    	  int val =contact.getData().getAchievements().getBeasthunter();
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Beasthunter";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getBeasthunter();
//		            	int val2 = o2.getData().getAchievements().getBeasthunter();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    
//		    //============= colonne Diehard ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Diehard");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Diehard";
//		    	  int val =contact.getData().getAchievements().getDiehard();
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Diehard";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getDiehard();
//		            	int val2 = o2.getData().getAchievements().getDiehard();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    
//		    
//		    //============= colonne Invader ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Invader");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Invader";
//		    	  int val =contact.getData().getAchievements().getInvader();
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Invader";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getInvader();
//		            	int val2 = o2.getData().getAchievements().getInvader();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    
//		    
//		    
//		    
//		    
//		    
//		    
//		    
//		    //============= colonne MedalAbrams (class)==========================
//		    buttonCell = new MyButtonCell() ;
//		    //recherche du titre de la médaille
//		    nameAch = getNameAch(hashMapAch, "MedalAbrams1");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  
//		    	  int val = contact.getData().getAchievements().getMedalAbrams();
//					String urlImgSrc = noData;
//					String baseNameAch = "MedalAbrams";
//					
//					String nameAch = baseNameAch+ val;
//					String pathImg = "";
//					if (val == 0) {
//		    		  nameAch = noData;
//		    		  pathImg = nameAch;
//		    		}else {
//		    			nameAch = baseNameAch+ val;
//		    			pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//		    		}
//					String valStr = String.valueOf(val);
//					return pathImg + "#Cl:" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalAbrams1";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalAbrams();
//		            	int val2 = o2.getData().getAchievements().getMedalAbrams();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		
//		    
//		    //============= colonne MedalBillotte ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalBillotte");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalBillotte";
//		    	  int val =contact.getData().getAchievements().getMedalBillotte();
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalBillotte";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalBillotte();
//		            	int val2 = o2.getData().getAchievements().getMedalBillotte();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    //============= colonne Steelwall ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Steelwall");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Steelwall";
//		    	  int val =contact.getData().getAchievements().getSteelwall();
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Steelwall";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getSteelwall();
//		            	int val2 = o2.getData().getAchievements().getSteelwall();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//	  
//
//		    
//		    
//		    //============= colonne MedalBurda ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalBurda");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalBurda";
//		    	  int val =contact.getData().getAchievements().getMedalBurda();
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalBurda";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalBurda();
//		            	int val2 = o2.getData().getAchievements().getMedalBurda();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne MedalCarius1 (class)==========================
//		    buttonCell = new MyButtonCell() ;
//		    //recherche du titre de la médaille
//		    nameAch = getNameAch(hashMapAch, "MedalCarius1");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  
//		    	  int val = contact.getData().getAchievements().getMedalCarius();
//					String urlImgSrc = noData;
//					String baseNameAch = "MedalCarius";
//					
//					String nameAch = baseNameAch+ val;
//					String pathImg = "";
//					if (val == 0) {
//		    		  nameAch = noData;
//		    		  pathImg = nameAch;
//		    		}else {
//		    			nameAch = baseNameAch+ val;
//		    			pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//		    		}
//					
//					String valStr = String.valueOf(val);
//					return pathImg + "#Cl:" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalCarius1";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalCarius();
//		            	int val2 = o2.getData().getAchievements().getMedalCarius();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne MedalEkins1 (class)==========================
//		    buttonCell = new MyButtonCell() ;
//		    //recherche du titre de la médaille
//		    nameAch = getNameAch(hashMapAch, "MedalEkins1");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  
//		    	  int val = contact.getData().getAchievements().getMedalEkins();
//					String urlImgSrc = noData;
//					String baseNameAch = "MedalEkins";
//					
//					String nameAch = baseNameAch+ val;
//					String pathImg = "";
//					if (val == 0) {
//		    		  nameAch = noData;
//		    		  pathImg = nameAch;
//		    		}else {
//		    			nameAch = baseNameAch+ val;
//		    			pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//		    		}
//					
//					String valStr = String.valueOf(val);
//					return pathImg + "#Cl:" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalEkins1";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalEkins();
//		            	int val2 = o2.getData().getAchievements().getMedalEkins();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//	
//		    //============= colonne MedalFadin ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalFadin");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalFadin";
//		    	  int val =contact.getData().getAchievements().getMedalFadin();
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalFadin";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalFadin();
//		            	int val2 = o2.getData().getAchievements().getMedalFadin();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne MedalHalonen ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalHalonen");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalHalonen";
//		    	  int val =contact.getData().getAchievements().getMedalHalonen();
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalHalonen";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalHalonen();
//		            	int val2 = o2.getData().getAchievements().getMedalHalonen();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//
//		    //============= colonne MedalKay1 (class)==========================
//		    buttonCell = new MyButtonCell() ;
//		    //recherche du titre de la médaille
//		    nameAch = getNameAch(hashMapAch, "MedalKay1");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  
//		    	  int val = contact.getData().getAchievements().getMedalKay();
//					String urlImgSrc = noData;
//					String baseNameAch = "MedalKay";
//					
//					String nameAch = baseNameAch+ val;
//					String pathImg = "";
//					if (val == 0) {
//		    		  nameAch = noData;
//		    		  pathImg = nameAch;
//		    		}else {
//		    			nameAch = baseNameAch+ val;
//		    			pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//		    		}
//					
//					String valStr = String.valueOf(val);
//					return pathImg + "#Cl:" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalKay1";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalKay();
//		            	int val2 = o2.getData().getAchievements().getMedalKay();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//	
//		    
//		    //============= colonne MedalKnispel1 (class)==========================
//		    buttonCell = new MyButtonCell() ;
//		    //recherche du titre de la médaille
//		    nameAch = getNameAch(hashMapAch, "MedalKnispel1");
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  
//		    	  int val = contact.getData().getAchievements().getMedalKnispel();
//					String urlImgSrc = noData;
//					String baseNameAch = "MedalKnispel";
//					
//					String nameAch = baseNameAch+ val;
//					String pathImg = "";
//					if (val == 0) {
//		    		  nameAch = noData;
//		    		  pathImg = nameAch;
//		    		}else {
//		    			nameAch = baseNameAch+ val;
//		    			pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//		    		}
//					
//					String valStr = String.valueOf(val);
//					return pathImg + "#Cl:" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalKnispel1";
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalKnispel();
//		            	int val2 = o2.getData().getAchievements().getMedalKnispel();
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//	
//		    
//		    //============= colonne MedalKolobanov ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalKolobanov");		// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalKolobanov";				// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalKolobanov();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalKolobanov";				// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalKolobanov();// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalKolobanov();// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//
//	
//		    //============= colonne MedalLavrinenko1 (class)==========================
//		    buttonCell = new MyButtonCell() ;
//		    //recherche du titre de la médaille
//		    nameAch = getNameAch(hashMapAch, "MedalLavrinenko1");// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  
//		    	  int val = contact.getData().getAchievements().getMedalLavrinenko();// TO CHANGE !!
//					String urlImgSrc = noData;
//					String baseNameAch = "MedalLavrinenko";// TO CHANGE !!
//					
//					String nameAch = baseNameAch+ val;
//					String pathImg = "";
//					if (val == 0) {
//		    		  nameAch = noData;
//		    		  pathImg = nameAch;
//		    		}else {
//		    			nameAch = baseNameAch+ val;
//		    			pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//		    		}
//					
//					String valStr = String.valueOf(val);
//					return pathImg + "#Cl:" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalLavrinenko1";// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalLavrinenko();// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalLavrinenko();// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//	
//		    
//
//		    //============= colonne MedalLeClerc1 (class)==========================
//		    buttonCell = new MyButtonCell() ;
//		    //recherche du titre de la médaille
//		    nameAch = getNameAch(hashMapAch, "MedalLeClerc1");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  
//		    	  int val = contact.getData().getAchievements().getMedalLeClerc();	// TO CHANGE !!
//					String urlImgSrc = noData;
//					String baseNameAch = "MedalLeClerc";							// TO CHANGE !!
//					
//					String nameAch = baseNameAch+ val;
//					String pathImg = "";
//					if (val == 0) {
//		    		  nameAch = noData;
//		    		  pathImg = nameAch;
//		    		}else {
//		    			nameAch = baseNameAch+ val;
//		    			pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//		    		}
//					
//					String valStr = String.valueOf(val);
//					return pathImg + "#Cl:" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalLeClerc1";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalLeClerc();			// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalLeClerc();			// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//	
//		    //============= colonne MedalKolobanov ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalKolobanov");		// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalKolobanov";				// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalKolobanov();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalKolobanov";				// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalKolobanov();// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalKolobanov();// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    //============= colonne MedalOrlik ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalOrlik");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalOrlik";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalOrlik();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalOrlik";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalOrlik();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalOrlik();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//
//
//		    //============= colonne MedalOskin ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalOskin");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalOskin";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalOskin();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalOskin";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalOskin();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalOskin();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//
//		    //============= colonne MedalPoppel1 (class)==========================
//		    buttonCell = new MyButtonCell() ;
//		    //recherche du titre de la médaille
//		    nameAch = getNameAch(hashMapAch, "MedalPoppel1");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  
//		    	  int val = contact.getData().getAchievements().getMedalPoppel();	// TO CHANGE !!
//					String urlImgSrc = noData;
//					String baseNameAch = "MedalPoppel";							// TO CHANGE !!
//					
//					String nameAch = baseNameAch+ val;
//					String pathImg = "";
//					if (val == 0) {
//		    		  nameAch = noData;
//		    		  pathImg = nameAch;
//		    		}else {
//		    			nameAch = baseNameAch+ val;
//		    			pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//		    		}
//					
//					String valStr = String.valueOf(val);
//					return pathImg + "#Cl:" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalPoppel1";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//		    //tri sur colonne
//		    column.setSortable(true);
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalPoppel();			// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalPoppel();			// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//	
//		    //============= colonne Mousebane ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Mousebane");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Mousebane";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMousebane();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Mousebane";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMousebane();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMousebane();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    
//
//		    //============= colonne Raider ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Raider");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Raider";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getRaider();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Raider";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getRaider();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getRaider();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    
//		    
//
//
//
//		    //============= colonne Sniper ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Sniper");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Sniper";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getSniper();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Sniper";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getSniper();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getSniper();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//	
//		    //============= colonne Scout ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Scout");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Scout";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getScout();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Scout";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getScout();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getScout();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//
//		    //============= colonne MedalPascucci ==========================
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalPascucci");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalPascucci";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalPascucci();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalPascucci";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalPascucci();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalPascucci();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne MechanicEngineer or MechanicEngineergray
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MechanicEngineer");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MechanicEngineer";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getMechanicEngineer();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch =nameAch + "gray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MechanicEngineer";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getMechanicEngineer();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getMechanicEngineer();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//			    
//		    //============= colonne  MedalBrunoPietro 
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalBrunoPietro");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalBrunoPietro";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalBrunoPietro();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalBrunoPietro";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalBrunoPietro();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalBrunoPietro();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne  HeroesOfRassenay 
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "HeroesOfRassenay");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "HeroesOfRassenay";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getHeroesOfRassenay();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "HeroesOfRassenay";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getHeroesOfRassenay();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getHeroesOfRassenay();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne Evileye
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Evileye");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Evileye";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getEvileye();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Evileye";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getEvileye();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getEvileye();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//	
//		    //============= colonne Expert: U.S.A. TankExpert2
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "TankExpert2");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "TankExpert2";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getTankExperts().getUsa();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="TankExpertUSAgray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "TankExpert2";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getTankExperts().getUsa();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getTankExperts().getUsa();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    //============= colonne  Expert: Germany 
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "TankExpert1");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "TankExpert1";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getTankExperts().getGermany();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="TankExpertGermanygray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "TankExpert1";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getTankExperts().getGermany();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getTankExperts().getGermany();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne Expert: France 
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "TankExpert4");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "TankExpert4";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getTankExperts().getFrance();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="TankExpertFrancegray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "TankExpert4";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getTankExperts().getFrance();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getTankExperts().getFrance();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    
//		    //============= colonne Expert: U.S.S.R. 
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "TankExpert0");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "TankExpert0";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getTankExperts().getUssr();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="TankExpertUSSRgray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "TankExpert0";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getTankExperts().getUssr();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getTankExperts().getUssr();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//			    
//		    //============= colonne Expert: United Kingdom 
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "TankExpert5");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "TankExpert5";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getTankExperts().getUk();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="TankExpertBritgray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "TankExpert5";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getTankExperts().getUk();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getTankExperts().getUk();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//			    
//		    //============= colonne Expert: China
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "TankExpert3");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "TankExpert3";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getTankExperts().getChina();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="TankExpertChinagray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "TankExpert3";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getTankExperts().getChina();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getTankExperts().getChina();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne MedalTamadaYoshio
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalTamadaYoshio");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalTamadaYoshio";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalTamadaYoshio();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalTamadaYoshio";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalTamadaYoshio();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalTamadaYoshio();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//	
//			    
//		    //============= colonne Bombardier
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Bombardier");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Bombardier";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getBombardier();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Bombardier";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getBombardier();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getBombardier();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//			    
//		    //============= colonne MedalBrothersInArms
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalBrothersInArms");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalBrothersInArms";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalBrothersInArms();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalBrothersInArms";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalBrothersInArms();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalBrothersInArms();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//			    
//		    //============= colonne maxDiehardSeries --> pas trouvé
//		    
//		    //============= colonne maxKillingSeries --> pas trouvé
//		    
//		    //============= colonne HandOfDeath
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "HandOfDeath");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "HandOfDeath";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getHandOfDeath();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "HandOfDeath";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getHandOfDeath();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getHandOfDeath();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//			    
//		    //============= colonne MedalTarczay
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalTarczay");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalTarczay";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalTarczay();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalTarczay";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalTarczay();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalTarczay();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne Sinai
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Sinai");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Sinai";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getSinai();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Sinai";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getSinai();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getSinai();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne maxInvincibleSeries --> Invincible ?
//		    
//		    //============= colonne MedalCrucialContribution
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalCrucialContribution");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalCrucialContribution";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalCrucialContribution();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalCrucialContribution";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalCrucialContribution();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalCrucialContribution();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne TitleSniper
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "TitleSniper");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "TitleSniper";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getTitleSniper();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "TitleSniper";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getTitleSniper();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getTitleSniper();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne MedalDeLanglade
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalDeLanglade");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalDeLanglade";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalDeLanglade();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalDeLanglade";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalDeLanglade();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalDeLanglade();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============= colonne medalWittmann --> pas trouvé dans wiki
//		    
//		    //============= colonne maxPiercingSeries --> pas trouvé dans wiki
//		    
//		    //============= colonne Kamikaze
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "Kamikaze");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "Kamikaze";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getKamikaze();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "Kamikaze";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getKamikaze();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getKamikaze();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//			    
//		    //============= colonne MedalRadleyWalters
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalRadleyWalters");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalRadleyWalters";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalRadleyWalters();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalRadleyWalters";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalRadleyWalters();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalRadleyWalters();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    
//		    
//		    //============= colonne MedalNikolas
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalNikolas");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalNikolas";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalNikolas();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalNikolas";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalNikolas();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalNikolas();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    //============= colonne medalBoelter -> pas trouvé
//		    
//		    //============= colonne TankExpert (gray) 
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "TankExpert");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "TankExpert";								// TO CHANGE !!
//		    	  int val = contact.getData().getAchievements().getTankExpert();	// TO CHANGE !!
//		    	  if(val ==0 ) {
//		    		  nameAch ="TankExpertgray2";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "TankExpert";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int  val1 = o1.getData().getAchievements().getTankExpert();	// TO CHANGE !!
//		            	int  val2 = o2.getData().getAchievements().getTankExpert();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    //============= colonne MedalLafayettePool
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalLafayettePool");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalLafayettePool";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalLafayettePool();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalLafayettePool";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalLafayettePool();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalLafayettePool();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    //============= colonne Technical Engineer, U.S.A.  
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MechanicEngineer2");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MechanicEngineer2";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getMechanicEngineers().getUsa();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="MechanicEngineerUSAgray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MechanicEngineer2";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getMechanicEngineers().getUsa();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getMechanicEngineers().getUsa();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    
//		    
//		    //============== colonne Technical Engineer, Germany  
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MechanicEngineer1");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MechanicEngineer1";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getMechanicEngineers().getGermany();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="MechanicEngineerGermanygray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MechanicEngineer1";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getMechanicEngineers().getGermany();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getMechanicEngineers().getGermany();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    
//		    
//		    //============== colonne Technical Engineer, France  
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MechanicEngineer4");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MechanicEngineer4";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getMechanicEngineers().getFrance();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="MechanicEngineerFrancegray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MechanicEngineer4";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getMechanicEngineers().getFrance();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getMechanicEngineers().getFrance();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============== colonne Technical Engineer, U.S.S.R.  
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MechanicEngineer0");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MechanicEngineer0";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getMechanicEngineers().getUssr();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="MechanicEngineerUSSRgray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MechanicEngineer0";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getMechanicEngineers().getUssr();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getMechanicEngineers().getUssr();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============== colonne Technical Engineer, United Kingdom  
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MechanicEngineer5");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MechanicEngineer5";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getMechanicEngineers().getUk();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="MechanicEngineerBritgray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MechanicEngineer5";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getMechanicEngineers().getUk();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getMechanicEngineers().getUk();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    //============== colonne Technical Engineer, China  
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MechanicEngineer3");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MechanicEngineer3";								// TO CHANGE !!
//		    	  String val = contact.getData().getAchievements().getMechanicEngineers().getGermany();	// TO CHANGE !!
//		    	  if(val.equalsIgnoreCase("false")) {
//		    		  nameAch ="MechanicEngineerChinagray";
//		    	  }
//				String pathImg = buildImgAch(hashMapAch, nameAch, contact, 0);
//				String valStr = String.valueOf(val);
//				valStr= "    ";
//				return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MechanicEngineer3";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	String  val1 = o1.getData().getAchievements().getMechanicEngineers().getGermany();	// TO CHANGE !!
//		            	String  val2 = o2.getData().getAchievements().getMechanicEngineers().getGermany();	// TO CHANGE !!
//		            	return (o2 != null) ?  val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//		    
//		    
//		    
//		    
//		    
//		    //============= colonne MedalLehvaslaiho
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalLehvaslaiho");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalLehvaslaiho";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalLehvaslaiho();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalLehvaslaiho";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalLehvaslaiho();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalLehvaslaiho();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    //============= colonne MedalDumitru
//		    buttonCell = new MyButtonCell() ;
//		    nameAch = getNameAch(hashMapAch, "MedalDumitru");						// TO CHANGE !!
//		    if(nameAch != null) {
//		    Column<CommunityAccount, String> column = addColumn(buttonCell, nameAch, new GetValue<String>() {
//		      @Override
//		      public String getValue(CommunityAccount contact) {
//		    	  String nameAch = "MedalDumitru";								// TO CHANGE !!
//		    	  int val =contact.getData().getAchievements().getMedalDumitru();	// TO CHANGE !!
//					String pathImg = buildImgAch(hashMapAch, nameAch, contact, val);
//					String valStr = String.valueOf(val);
//					return pathImg + "#" + valStr;//on retourne l'url de l'icône de la médaille + le nb de fois qu'elle a été acquise ou sa classe 
//		      }
//
//		      
//		    }, new FieldUpdater<CommunityAccount, String>() {
//		        @Override
//		        public void update(int index, CommunityAccount object, String value) {
//			    	String nameAch = "MedalDumitru";								// TO CHANGE !!
//			    	buildPopup(nameAch, hashMapAch);
//		        }
//		      });
//
//		    //tri sur colonne
//		    column.setSortable(true);
//		    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(column,
//		        new Comparator<CommunityAccount>() {
//		          public int compare(CommunityAccount o1, CommunityAccount o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//
//		            // Compare the name columns.
//		            if (o1 != null) {
//		            	int val1 = o1.getData().getAchievements().getMedalDumitru();	// TO CHANGE !!
//		            	int val2 = o2.getData().getAchievements().getMedalDumitru();	// TO CHANGE !!
//		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//		            }
//		            return -1;
//		          }
//		        });
//		    }
//		    //========= fin création colonne =============
//
//		    //============= colonne maxSniperSeries -> pas trouvé dans wiki
//		    
//	
//		    
//
//	    /////////////////////////////////////////////////
//		    // Add a selection model to handle user selection.
//		    final SingleSelectionModel<CommunityAccount> selectionModel = new SingleSelectionModel<CommunityAccount>();
//		    tableAchivementCommAcc.setSelectionModel(selectionModel);
//		    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//		      public void onSelectionChange(SelectionChangeEvent event) {
//		    	  CommunityAccount selected = selectionModel.getSelectedObject();
//		        if (selected != null) {
//		          //Window.alert("You selected: " + selected.getName());
//		        }
//		      }
//		    });
//	
//		    // Set the total row count. This isn't strictly necessary, but it affects
//		    // paging calculations, so its good habit to keep the row count up to date.
//		    
//		    tableAchivementCommAcc.setRowCount(listCommAcc.size(), true); //no need to do here because we have add list to data provider
//	
//		    // Push the data into the widget.
//		    tableAchivementCommAcc.setRowData(0, listCommAcc);            //idem no nedd dataprovider
//		    
//		 // Connect the table to the data provider.
//		    dataAchievementsProvider.addDataDisplay(tableAchivementCommAcc);
//		    dataAchievementsProvider.refresh();
//	   }
//

	/*
	 * call this when we have data to put in table
	 */
	public  void buildACellTableForCommunityClan(Clan listClan) {
			    
	    //update dataprovider with some known list 
	    dataClanProvider.setList(listClan.getItems());
		
		// Create a CellTable.
	    //CellTable<CommunityAccount> table = new CellTable<CommunityAccount>();
		tableClan.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	    
	    
	    ListHandler<ItemsDataClan> columnSortHandler =
		        new ListHandler<ItemsDataClan>(dataClanProvider.getList());
	    tableClan.addColumnSortHandler(columnSortHandler);
	    
    
	    // Add a text column to show the name.
	    TextColumn<ItemsDataClan> nameColumn = new TextColumn<ItemsDataClan>() {
	      @Override
	      public String getValue(ItemsDataClan object) {
	        return object.getName();
	      }
	    };
	    tableClan.addColumn(nameColumn, "Name");

	    nameColumn.setSortable(true);
	    
	 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(nameColumn,
	        new Comparator<ItemsDataClan>() {
	          public int compare(ItemsDataClan o1, ItemsDataClan o2) {
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
	    tableClan.getColumnSortList().push(nameColumn);
	    
	    // Add a text column to show the id.
	    TextColumn<ItemsDataClan> idColumn = new TextColumn<ItemsDataClan>() {
	      @Override
	      public String getValue(ItemsDataClan object) {
	        return object.getId();
	      }
	    };
	    tableClan.addColumn(idColumn, "Clan Id");

	    
	    // Add a text column to show the abbrev.
	    TextColumn<ItemsDataClan> abbrevColumn = new TextColumn<ItemsDataClan>() {
	      @Override
	      public String getValue(ItemsDataClan object) {
	        return String.valueOf(object.getAbbreviation());
	      }
	    };
	    tableClan.addColumn(abbrevColumn, "Abreviation");
	    
	    abbrevColumn.setSortable(true);
	    
	 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    // java.util.List.
	    columnSortHandler.setComparator(abbrevColumn,
	        new Comparator<ItemsDataClan>() {
	          public int compare(ItemsDataClan o1, ItemsDataClan o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	String val1 = o1.getAbbreviation();
	            	String val2 = o2.getAbbreviation();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    
	    
	    // Add a text column to show the getClan_emblem_url().
		    
    		Column<ItemsDataClan, String> emblemColumn = new
    		Column<ItemsDataClan, String>(new ImageCell())
    		       {
		               @Override
		               public String getValue(ItemsDataClan object) {

		                       return object.getClan_emblem_url();
		               }

    		       };

   
		    //////////////////////
//		    TextColumn<ItemsDataClan> emblemColumn = new TextColumn<ItemsDataClan>() {
//		      @Override
//		      public String getValue(ItemsDataClan object) {
//		        return String.valueOf(object.getClan_emblem_url() );
//		      }
//		    };
	    tableClan.addColumn(emblemColumn, "Emblem");
	    
//		    emblemColumn.setSortable(true);
//		    
//		    // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(emblemColumn,
//		        new Comparator<ItemsDataClan>() {
//		          public int compare(ItemsDataClan o1, ItemsDataClan o2) {
//		            if (o1 == o2) {
//		              return 0;
//		            }
//	
//		            // Compare the columns.
//		            if (o1 != null) {
//		            	String val1 = o1.getClan_emblem_url();
//		            	String val2 = o2.getClan_emblem_url();
//		              return (o2 != null) ? val1.compareTo(val2) : 1;
//		            }
//		            return -1;
//		          }
//		        });
	    //////////////////////////////////////////////////
	    
	    
	    // Add a text column 
	    TextColumn<ItemsDataClan> membersColumn = new TextColumn<ItemsDataClan>() {
	      @Override
	      public String getValue(ItemsDataClan object) {
	        return String.valueOf(object.getMember_count() );
	      }
	    };
	    tableClan.addColumn(membersColumn, "Nb members");
	    
	    membersColumn.setSortable(true);
	    
	 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(membersColumn,
	        new Comparator<ItemsDataClan>() {
	          public int compare(ItemsDataClan o1, ItemsDataClan o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	int val1 = Integer.valueOf(o1.getMember_count());
	            	int val2 = Integer.valueOf(o2.getMember_count());
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });
	    
	    // Add a text column to show the motto.
	    TextColumn<ItemsDataClan> mottoColumn = new TextColumn<ItemsDataClan>() {
	      @Override
	      public String getValue(ItemsDataClan object) {
	        return String.valueOf(object.getMotto());
	      }
	    };
	    tableClan.addColumn(mottoColumn, "Motto");

	    //////
	    mottoColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(mottoColumn,
	        new Comparator<ItemsDataClan>() {
	          public int compare(ItemsDataClan o1, ItemsDataClan o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	String val1 = o1.getMotto();
	            	String val2 = o2.getMotto();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });
	    
		    
	    // Add a text column to show owner.
	    TextColumn<ItemsDataClan> ownerColumn = new TextColumn<ItemsDataClan>() {
	      @Override
	      public String getValue(ItemsDataClan object) {
	        return String.valueOf(object.getOwner());
	      }
	    };
	    tableClan.addColumn(ownerColumn, "Owner");

	    ownerColumn.setSortable(true);
	    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    columnSortHandler.setComparator(ownerColumn,
	        new Comparator<ItemsDataClan>() {
	          public int compare(ItemsDataClan o1, ItemsDataClan o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the columns.
	            if (o1 != null) {
	            	String val1 = o1.getOwner();
	            	String val2 = o2.getOwner();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });
    
	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<ItemsDataClan> selectionModel = new SingleSelectionModel<ItemsDataClan>();
	    tableClan.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	  ItemsDataClan selected = selectionModel.getSelectedObject();
	    	  
	        if (selected != null) {
	          //Window.alert("You selected: " + selected.getName() +". You can find members now !");
	          idClan = selected.getId();
	        }
	        
	      }
	    });

	    // Set the total row count. This isn't strictly necessary, but it affects
	    // paging calculations, so its good habit to keep the row count up to date.
	    
	    tableClan.setRowCount(listClan.getItems().size(), true); //no need to do here because we have add list to data provider

	    // Push the data into the widget.
	    tableClan.setRowData(0, listClan.getItems());            //idem no nedd dataprovider
	    
	 // Connect the table to the data provider.
	    dataClanProvider.addDataDisplay(tableClan);
	    dataClanProvider.refresh();
		    
	}


	
	/**
	   * Get a cell value from a record.
	   * 
	   * @param <C> the cell type
	   */
	  private static interface GetValue<C> {
	    C getValue(CommunityAccount contact);
	  }
	
	 /**
	   * Add a column with a header.
	   * 
	   * @param <C> the cell type
	   * @param cell the cell used to render the column
	   * @param headerText the header string
	   * @param getter the value getter for the cell
	   */
	  private <C> Column<CommunityAccount, C> addColumn(Cell<C> cell, String headerText,
	      final GetValue<C> getter, FieldUpdater<CommunityAccount, C> fieldUpdater) {
	    Column<CommunityAccount, C> column = new Column<CommunityAccount, C>(cell) {
	      @Override
	      public C getValue(CommunityAccount object) {
	        return getter.getValue(object);
	      }
	      
	    };
	    column.setFieldUpdater(fieldUpdater);
//	    if (cell instanceof AbstractEditableCell<?, ?>) {
//	      editableCells.add((AbstractEditableCell<?, ?>) cell);
//	    }
	    tableAchivementCommAcc.addColumn(column, headerText);
	    return column;
	  }
	
	
	/**
		 * This is the entry point method.
		 */
	public void onModuleLoad() {
		
		
			
			final Label errorLabel = new Label();
			
			 /**
			   * An instance of the constants.
			   */
			final CwConstants constants = GWT.create(CwConstants.class);
			
			//final CwConstants constants = null;

	
			// Add the nameField and sendButton to the RootPanel
			// Use RootPanel.get() to get the entire body element
			rootPanel = RootPanel.get();
			//RootPanel.get("errorLabelContainer").add(errorLabel);
			
			dockPanel = new DockPanel();
			rootPanel.add(dockPanel, 29, 265);
			dockPanel.setSize("1193px", "550px");
			

			//button search Clans
			int posLeft = 10;
			int posTop = 10;
			Button searchClansButton = new Button("Clans");
			rootPanel.add(searchClansButton, 10, posTop);
			searchClansButton.setSize("80px", "28px");

			//bouton plus de clans
			final Button searchClansButtonMore = new Button("100 Clans +");
			rootPanel.add(searchClansButtonMore, 95, posTop);
			searchClansButtonMore.setSize("120px", "28px");
			searchClansButtonMore.setEnabled(false);
			
			
			//label clan
			Label lblNewLabel = new Label("Clan");
			//lblNewLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			rootPanel.add(lblNewLabel, 300, posTop + 4);
			lblNewLabel.setSize("50px", "24px");
			
			
			//nom du clan a rechercher
			final TextBox nameClan = new TextBox();
			rootPanel.add(nameClan, 350, posTop);
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
			// Focus the name clan 
			nameClan.setFocus(true);
			
			//bouton login for admin functions
			final Button loginAdminButton = new Button("Admin login");
			rootPanel.add(loginAdminButton, 700, posTop);
			loginAdminButton.setSize("125px", "28px");
			loginAdminButton.setEnabled(true);
			
			//nom du login 
			final TextBox nameLogin = new TextBox();
			rootPanel.add(nameLogin, 850, posTop);
			nameLogin.setText("t..");
			nameLogin.setSize("125px", "18px");
			
			nameLogin.addFocusHandler(new FocusHandler() {
				
				@Override
				public void onFocus(FocusEvent event) {
					// TODO Auto-generated method stub
					passSaving = 0;
					nbUsersToTreat = 30;
				}
			});
			
			final Button persistStatsButton = new Button("Save stats");
			rootPanel.add(persistStatsButton, 1000, posTop);
			persistStatsButton.setSize("125px", "28px");
			persistStatsButton.setEnabled(false);
			
			// Add a handler to set admin login true
			loginAdminButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (nameLogin.getText().equalsIgnoreCase("thleconn")){
						persistStatsButton.setEnabled(true);
						adminLogin = true;
					}else {
						persistStatsButton.setEnabled(false);
						adminLogin = false;
					}
				}
			});
			//

			
			
			//next row button search Clan's users
			posTop = posTop + 35;
			Button searchUsersClanButton = new Button("Clan's Users");
			rootPanel.add(searchUsersClanButton, 10, posTop);
			searchUsersClanButton.setSize("210px", "28px");

			// Add a drop box with the clan's users
		    final ListBox dropBoxClanUsers = new ListBox(true);
		    dropBoxClanUsers.setSize("300px", "150px");
		    dropBoxClanUsers.ensureDebugId("cwListBox-dropBox");
		    dropBoxClanUsers.setVisibleItemCount(20);
		    //dropBoxClanUsers.setMultipleSelect(true);
		    rootPanel.add(dropBoxClanUsers, 300, posTop);

	
			//next row -- button search stats member's clan
			posTop = posTop + 35 ;
			final Button findMembersClanButton = new Button("Send");
			findMembersClanButton.setText("Stats");
			rootPanel.add(findMembersClanButton, 10, posTop);
			findMembersClanButton.setSize("210px", "28px");
			findMembersClanButton.setEnabled(false);

			//findHistorizedStatsButton
			posTop = posTop + 35 ;
			final Button findHistorizedStatsButton = new Button("Send");
			findHistorizedStatsButton.setText("Histo Battles");
			rootPanel.add(findHistorizedStatsButton, 10, posTop);
			findHistorizedStatsButton.setSize("210px", "28px");
			findHistorizedStatsButton.setEnabled(false);

			//findHistorizedStatsTanksButton
			posTop = posTop + 35 ;
			final Button findHistorizedStatsTanksButton = new Button("Send");
			findHistorizedStatsTanksButton.setText("Histo Battles Tanks");
			rootPanel.add(findHistorizedStatsTanksButton, 10, posTop);
			findHistorizedStatsTanksButton.setSize("210px", "28px");
			findHistorizedStatsTanksButton.setEnabled(false);

			
			//next row - button achievement's member
		    posTop = posTop + 70 ;
			final Button findAchievementsMemberButton = new Button("Send");
			findAchievementsMemberButton.setText("Achievements");
			rootPanel.add(findAchievementsMemberButton, 10, posTop);
			findAchievementsMemberButton.setSize("210px", "28px");
			findAchievementsMemberButton.setEnabled(false);

		    // Add a drop box with the category of achievement
		    final ListBox dropBoxCategoryAchievement = new ListBox(false);
		    dropBoxCategoryAchievement.setSize("300px", "28px");
		    dropBoxCategoryAchievement.ensureDebugId("cwListBox-dropBox");
		    rootPanel.add(dropBoxCategoryAchievement, 300, posTop);
			
		    
		    final ListBox dropBoxAchievement = new ListBox(false);
		    dropBoxAchievement.setSize("300px", "28px");
		    dropBoxAchievement.ensureDebugId("cwListBox-dropBox");
		    rootPanel.add(dropBoxAchievement, 650, posTop);
			
			
		    
			//loading .gif
		    posTop = posTop + 35 ;
			Image image = new Image("loading.gif");
			final HorizontalPanel hPanelLoading = new HorizontalPanel();
			hPanelLoading.add(image);
			hPanelLoading.setVisible(false);
		    rootPanel.add(hPanelLoading, 350, posTop);
		    
			// Create the popup dialog box in case of error
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
//					searchClanButton.setEnabled(true);
//					searchClanButton.setFocus(true);
				}
			});
			
			// Create a handler for the Button search all clans
			class HandlerGetClans implements ClickHandler, KeyUpHandler {
				/**
				 * Fired when the user clicks on the sendButton.
				 */
				public void onClick(ClickEvent event) {
					//si c'est bouton find more on incr�mente offset por trouver les 100 clans suivnats
					if ( ((Button)event.getSource()).getText().equalsIgnoreCase(searchClansButtonMore.getText())) {
						offsetClan = offsetClan + 100;
						limitClan = 100;
					} else {
						//bouton find clan offset 0
						offsetClan = 0;
						limitClan = 100;
					}
					//recherche des clans
					getClans();
				}
	
				/**
				 * Fired when the user types in the nameField.
				 */
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						if ( event.getSource() instanceof Button && ((Button)event.getSource()).getText().equalsIgnoreCase(searchClansButtonMore.getText())) {
							offsetClan = offsetClan + 100;
							limitClan = 100;
						} else {
							offsetClan = 0;
							limitClan = 100;
						}
						getClans();
					}
				}
	
				
				/**
				 * Send the name from the nameField to the server and wait for a response.
				 */
				private void getClans() {
					hPanelLoading.setVisible(true);
					tableStatsCommAcc.setVisible(false);
					// First, we validate the input.
					errorLabel.setText("");
					String textToServer = nameClan.getText();
					if (!FieldVerifier.isValidName(textToServer)) {
						errorLabel.setText("Please enter at least four characters");
						return;
					}
	
					// Then, we send the input to the server.
					//searchClanButton.setEnabled(false);
					textToServerLabel.setText(textToServer);
					serverResponseLabel.setText("");
					wotService.getClans(textToServer , offsetClan,
							new AsyncCallback<Clan>() {
								public void onFailure(Throwable caught) {
									hPanelLoading.setVisible(false);
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
									hPanelLoading.setVisible(false);
									try {
										//String translatedText =Translate.execute("Bonjour le monde", Languages.FRENCH, Languages.ENGLISH);
										//System.out.println("Bonjour le monde : " + translatedText);
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									String status_code= listClan.getStatus_code();
									String status= listClan.getStatus();
//									"status": "ok", 
//									  "status_code": "NO_ERROR", 
									
									if (status.equalsIgnoreCase("ok")) {  
										dockPanel.remove(tableStatsCommAcc);
										dockPanel.remove(tableClan);
										if (pagerStatsCommunityAccount != null) 
											dockPanel.remove(pagerStatsCommunityAccount);
										if (pagerClan != null) 
											dockPanel.remove(pagerClan);
										
										if (dataClanProvider.getDataDisplays()!= null && !dataClanProvider.getDataDisplays().isEmpty()) 
											dataClanProvider.removeDataDisplay(tableClan);
										
										tableClan = new  CellTable<ItemsDataClan> (ItemsDataClan.KEY_PROVIDER);
										
										//construct column in celltable tableClan , set data set sort handler etc ..
										buildACellTableForCommunityClan(listClan);
										
										//get wiki wot (pour les m�dailles)
										xmlWiki = listClan.getWiki();
										
										//add items to listbox of category achievement
										final HashMap<String, List<XmlListAchievement>> hashMapAch = buidHashMapCategoryAchievement(xmlWiki);//Battle Hero Achievements - Commemorative Achievements - Epic Achievements (medals) - Special Achievements (titles) - Step Achievements (medals) 
										Set<String> setKeysCat = hashMapAch.keySet();
										Object[] listCat = (Object[])setKeysCat.toArray();

										dropBoxCategoryAchievement.addItem("All Achievements");
									    for (int i = 0; i < listCat.length; i++) {
										      dropBoxCategoryAchievement.addItem((String)listCat[i]);
										}
									    
									    // Add a handler to handle dropBoxCategoryAchievement
									    dropBoxCategoryAchievement.addChangeHandler(new ChangeHandler() {
									      public void onChange(ChangeEvent event) {
									        //showCategory(dropBoxAchievement, dropBoxCategoryAchievement.getSelectedIndex());
									    	  
									        dropBoxAchievement.ensureDebugId("cwListBox-multiBox");
									        
									        //on efface la liste box des m�dailles
									        dropBoxAchievement.clear();
									        int indexSelected = dropBoxCategoryAchievement.getSelectedIndex();
									        
									        if(indexSelected >= 0 ) {
									        	List<XmlListAchievement> listAchievement = new ArrayList<XmlListAchievement>();
									        	String valueSelected = dropBoxCategoryAchievement.getValue(indexSelected);
									        	if("All Achievements".equalsIgnoreCase(valueSelected)) {
									        		Collection<List<XmlListAchievement>>  col = hashMapAch.values();
									        		for (List<XmlListAchievement> list : col ) {
									        			for (XmlListAchievement ach : list ) {
									        				
									        				listAchievement.add(ach);
									        			}
									        		}
									        	}else {
									        		listAchievement = hashMapAch.get(valueSelected);
									        	}
										        
										        for ( XmlListAchievement ach : listAchievement)	{
										        	String nameAch = ach.getNAME();
										        	dropBoxAchievement.addItem(nameAch);
										        }
									        }
									        
									      }
									    });
									    //set all achievement of listbox
									    List<XmlListAchievement> listAchievement = new ArrayList<XmlListAchievement>();
						        		Collection<List<XmlListAchievement>>  col = hashMapAch.values();
						        		for (List<XmlListAchievement> list : col ) {
						        			for (XmlListAchievement ach : list ) {
						        				listAchievement.add(ach);
						        			}
						        		}
						        
							        	for ( XmlListAchievement ach : listAchievement)	{
								        	String nameAch = ach.getNAME();
								        	dropBoxAchievement.addItem(nameAch);
								        }
									    
										//Create a Pager to control the table.
									    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
									    pagerClan = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
									    pagerClan.setDisplay(tableClan);
										
								    
									    //add to dock panel ======
									    dockPanel.add(pagerClan, DockPanel.SOUTH);
									    pagerClan.setPage(10);
									    pagerClan.setVisible(true);
										
										dockPanel.add(tableClan, DockPanel.SOUTH);
										tableClan.setVisible(true);
										tableClan.setFocus(true);

										findMembersClanButton.setEnabled(true);
										findHistorizedStatsButton.setEnabled(true);
										findHistorizedStatsTanksButton.setEnabled(true);
										findAchievementsMemberButton.setEnabled(true);
										
										//on autorise le bouton  more clans s'il y a en core 100 �lments dans TAB
										if(listClan.getItems().size()== 100)
											searchClansButtonMore.setEnabled(true);
										else {
											searchClansButtonMore.setEnabled(false);
										}
									}else {
										dialogBox
										.setText(status_code);
										serverResponseLabel
												.addStyleName("serverResponseLabelError");
										serverResponseLabel.setHTML(status_code + " An error arrived , please Retry again ! " );
										dialogBox.center();
										closeButton.setFocus(true);
									}
									
								}
						});
					//searchClanButton.setEnabled(true);
					//searchClanButton.setFocus(true);
				}
				
				
				
			}
			/////////////////////////
			//creare a handler for persist data in datastore
			// Create a handler for the Button search all clans
			class HandlerPersistStats implements ClickHandler, KeyUpHandler {
				/**
				 * Fired when the user clicks on the sendButton.
				 */
				public void onClick(ClickEvent event) {
					//persist stats
					persisStats(passSaving, nbUsersToTreat);
					passSaving = 1 ;
					nbUsersToTreat = 30;
				}
	
				/**
				 * Fired when the user types in the nameField.
				 */
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						persisStats(passSaving, nbUsersToTreat);
						passSaving = 1 ;
						nbUsersToTreat = 30;
					}
				}
	
				
				/**
				 * Send the name from the nameField to the server and wait for a response.
				 */
				private void persisStats(int indexBeginSaveStatsUser, int indexEndSaveStatsUser) {
					hPanelLoading.setVisible(true);
				    // recup des users selected in dropBoxClanUsers
					List<String> listIdUser = new ArrayList<String>();
					int itemCount = dropBoxClanUsers.getItemCount();
					for(int i = 0 ;  i< itemCount ; i++) {
						if (dropBoxClanUsers.isItemSelected(i)) {
							listIdUser.add(dropBoxClanUsers.getValue(i));
						}
					}
					
					// First, we validate the input.
					errorLabel.setText("");
					String textToServer = idClan;
					if (!FieldVerifier.isValidName(textToServer)) {
						errorLabel.setText("Please enter at least four characters");
						return;
					}
	
					// Then, we send the input to the server.
					//searchClanButton.setEnabled(false);
					textToServerLabel.setText(textToServer);
					serverResponseLabel.setText("");
					wotService.persistStats(textToServer , indexBeginSaveStatsUser, indexEndSaveStatsUser, listIdUser,//offsetClan,
							new AsyncCallback<List<String>>() {
								public void onFailure(Throwable caught) {
									hPanelLoading.setVisible(false);
									// Show the RPC error message to the user
									dialogBox
											.setText("Remote Procedure Call - Failure");
									serverResponseLabel
											.addStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML(SERVER_ERROR);
									dialogBox.center();
									closeButton.setFocus(true);
								}
	
								
								public void onSuccess(List<String> listClan) {
									hPanelLoading.setVisible(false);
									if (listClan.size()== 0) {

										dialogBox
										.setText(idClan);
										serverResponseLabel
												.addStyleName("serverResponseLabelError");
										serverResponseLabel.setHTML(idClan + " An error arrived , please Retry again ! " );
										dialogBox.center();
										closeButton.setFocus(true);
									}
									
								}
						});
					//searchClanButton.setEnabled(true);
					//searchClanButton.setFocus(true);
				}
				
				
				
			}
			
			
			////
			///////////
			// Create a handler for search achivement's member
			class HandlerGetAchievementsMember implements ClickHandler, KeyUpHandler {
				/**
				 * Fired when the user clicks on the sendButton.
				 */
				public void onClick(ClickEvent event) {
					int indexSelected = dropBoxCategoryAchievement.getSelectedIndex();
					if (indexSelected >= 0 )
					{
						String valueSelected  = dropBoxCategoryAchievement.getValue(indexSelected);
						getAchievementMember2(valueSelected);
					}
//					offsetClan = 0;
//					limitClan = 100;
				}
	
				/**
				 * Fired when the user types in the nameField.
				 */
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						int indexSelected = dropBoxCategoryAchievement.getSelectedIndex();
						if (indexSelected >= 0 )
						{
							String valueSelected  = dropBoxCategoryAchievement.getValue(indexSelected);
							getAchievementMember2(valueSelected);
						}
//						offsetClan = 0;
//						limitClan = 100;
					}
				}
				private void getAchievementMember2(String valueSelected) {
					hPanelLoading.setVisible(true);
					// First, we validate the input.
					errorLabel.setText("");
					String textToServer = idClan;
					if (!FieldVerifier.isValidName(textToServer)) {
						errorLabel.setText("Please enter at least four characters");
						
						/////
						dialogBox
						.setText("Select a Clan before!!");
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML("Click on a clan before find members !"  );
						dialogBox.center();
						closeButton.setFocus(true);
						hPanelLoading.setVisible(false);
						return;
					}
					dockPanel.remove(tableAchivementCommAcc);
					dockPanel.remove(tableStatsCommAcc);
					dockPanel. remove(tableClan);
					
					if (pagerAchievementsCommunityAccount != null) 
						dockPanel.remove(pagerAchievementsCommunityAccount);
					if (pagerStatsCommunityAccount != null) 
						dockPanel.remove(pagerStatsCommunityAccount);
					if (pagerClan != null) 
						dockPanel.remove(pagerClan);
					
//					
//					if (dataAchievementsProvider.getDataDisplays()!= null && !dataAchievementsProvider.getDataDisplays().isEmpty()) 
//						dataAchievementsProvider.removeDataDisplay(tableAchivementCommAcc);
//					
					//on re-construit 1 nouveau tableau
					//tableAchivementCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);
					
					//construct column in celltable tableCommAcc , set data set sort handler etc ..
					//buildACellTableForAchivementsCommunityAccount(dataStatsProvider.getList(), xmlWiki, valueSelected);
					  
					//Create a Pager to control the table.
				    //SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
				    //pagerAchievementsCommunityAccount = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
				    //pagerAchievementsCommunityAccount.setDisplay(tableAchivementCommAcc);
					
			    
				    //add to dock panel ======
				    //add tab achievement a the end 
				    //dockPanel.add(pagerAchievementsCommunityAccount, DockPanel.SOUTH);
				    //pagerAchievementsCommunityAccount.setPage(10);
				    //pagerAchievementsCommunityAccount.setVisible(true);
					
					//dockPanel.add(tableAchivementCommAcc, DockPanel.SOUTH);
					//tableAchivementCommAcc.setVisible(true);
					//tableAchivementCommAcc.setFocus(true);
				    //add tab stats 
				    dockPanel.add(pagerStatsCommunityAccount, DockPanel.SOUTH);
					pagerStatsCommunityAccount.setPage(10);
					pagerStatsCommunityAccount.setVisible(true);
					
					dockPanel.add(tableStatsCommAcc, DockPanel.SOUTH);
					tableStatsCommAcc.setVisible(false);
				    
					//add tab clan at the begin
					dockPanel.add(pagerClan, DockPanel.SOUTH);
					dockPanel.add(tableClan, DockPanel.SOUTH);
					tableClan.setVisible(true);
					pagerClan.setVisible(true);
					hPanelLoading.setVisible(false);
				}
			}
			
			
			///////////
			// Create a handler for search clan's members
			class HandlerGetAllMembersClanAndStats implements ClickHandler, KeyUpHandler {
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
					hPanelLoading.setVisible(true);
					
				    // recup des users selected in dropBoxClanUsers
					List<String> listIdUser = new ArrayList<String>();
					int itemCount = dropBoxClanUsers.getItemCount();
					for(int i = 0 ;  i< itemCount ; i++) {
						if (dropBoxClanUsers.isItemSelected(i)) {
							listIdUser.add(dropBoxClanUsers.getValue(i));
						}
					}
					
					
					errorLabel.setText("");
					String textToServer = idClan;
					if (!FieldVerifier.isValidName(textToServer)) {
						errorLabel.setText("Please enter at least four characters");
						
						/////
						dialogBox
						.setText("Select a Clan before!!");
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML("Click on a clan before find members !"  );
						dialogBox.center();
						closeButton.setFocus(true);
						return;
					}
	
					// Then, we send the input to the server.
					//searchClanButton.setEnabled(false);
					textToServerLabel.setText(textToServer);
					serverResponseLabel.setText("");
					wotService.getAllMembersClanAndStats(listIdUser,
							new AsyncCallback<AllCommunityAccount>() {
								public void onFailure(Throwable caught) {
									hPanelLoading.setVisible(false);
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
									hPanelLoading.setVisible(false);
									dockPanel.remove(tableStatsCommAcc);
									dockPanel. remove(tableClan);
									
									if (pagerStatsCommunityAccount != null) 
										dockPanel.remove(pagerStatsCommunityAccount);
									if (pagerClan != null) 
										dockPanel.remove(pagerClan);
									
									if (dataStatsProvider.getDataDisplays()!= null && !dataStatsProvider.getDataDisplays().isEmpty()) 
										dataStatsProvider.removeDataDisplay(tableStatsCommAcc);
									
									//on re-construit 1 nouveau tableau
									tableStatsCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);
									
									//construct column in celltable tableCommAcc , set data set sort handler etc ..
									buildACellTableForStatsCommunityAccount(listAccount.getListCommunityAccount());
									  
									//Create a Pager to control the table.
								    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
								    pagerStatsCommunityAccount = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
								    pagerStatsCommunityAccount.setDisplay(tableStatsCommAcc);
									
							    
								    //add to dock panel ======
								    dockPanel.add(pagerStatsCommunityAccount, DockPanel.SOUTH);
									pagerStatsCommunityAccount.setPage(10);
									pagerStatsCommunityAccount.setVisible(true);
									
									dockPanel.add(tableStatsCommAcc, DockPanel.SOUTH);
									tableStatsCommAcc.setVisible(true);
								    
									dockPanel.add(pagerClan, DockPanel.SOUTH);
									dockPanel.add(tableClan, DockPanel.SOUTH);
									tableClan.setVisible(true);
									pagerClan.setVisible(true);
									
									tableStatsCommAcc.setFocus(true);
									//dialogBox.center();
									//closeButton.setFocus(true);
								}
							});
					//searchClanButton.setEnabled(true);
					//searchClanButton.setFocus(true);
				}
				
				
				
			}
			///////////
			// Create a handler for search clan's members
			class HandlerGetHistorizedStats implements ClickHandler, KeyUpHandler {
				/**
				 * Fired when the user clicks on the sendButton.
				 */
				public void onClick(ClickEvent event) {
					getHistorizedStats();
					offsetClan = 0;
					limitClan = 100;
				}
	
				/**
				 * Fired when the user types in the nameField.
				 */
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						getHistorizedStats();
						offsetClan = 0;
						limitClan = 100;
					}
				}
	
				/**
				 * Send the name from the nameField to the server and wait for a response.
				 */
				private void getHistorizedStats() {
					// First, we validate the input.
					hPanelLoading.setVisible(true);
					
				    // recup des users selected in dropBoxClanUsers
					List<String> listIdUser = new ArrayList<String>();
					int itemCount = dropBoxClanUsers.getItemCount();
					for(int i = 0 ;  i< itemCount ; i++) {
						if (dropBoxClanUsers.isItemSelected(i)) {
							listIdUser.add(dropBoxClanUsers.getValue(i));
						}
					}
					
					
					errorLabel.setText("");
					String textToServer = idClan;
					if (!FieldVerifier.isValidName(textToServer)) {
						errorLabel.setText("Please enter at least four characters");
						
						/////
						dialogBox
						.setText("Select a Clan before!!");
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML("Click on a clan before find members !"  );
						dialogBox.center();
						closeButton.setFocus(true);
						return;
					}
	
					// Then, we send the input to the server.
					//searchClanButton.setEnabled(false);
					textToServerLabel.setText(textToServer);
					serverResponseLabel.setText("");
					wotService.getHistorizedStats( listIdUser,
							new AsyncCallback<List<CommunityAccount>>() {
								public void onFailure(Throwable caught) {
									hPanelLoading.setVisible(false);
									// Show the RPC error message to the user
									dialogBox
											.setText("Remote Procedure Call - Failure");
									serverResponseLabel
											.addStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML(SERVER_ERROR);
									dialogBox.center();
									closeButton.setFocus(true);
									
								}
	
								public void onSuccess(List<CommunityAccount> listAccount) {
									hPanelLoading.setVisible(false);
									dockPanel.remove(tableHistorizedStatsCommAcc);
									dockPanel.remove(tableClan);
									
									if (pagerHistorizedStatsCommunityAccount != null) 
										dockPanel.remove(pagerHistorizedStatsCommunityAccount);
									if (pagerClan != null) 
										dockPanel.remove(pagerClan);
									
									if (dataHistorizedStatsProvider.getDataDisplays()!= null && !dataHistorizedStatsProvider.getDataDisplays().isEmpty()) 
										dataHistorizedStatsProvider.removeDataDisplay(tableHistorizedStatsCommAcc);
									
									//on re-construit 1 nouveau tableau
									tableHistorizedStatsCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);
									
									
									//construct column in celltable tableCommAcc , set data set sort handler etc ..
									buildACellTableForHistorizedStatsCommunityAccount(listAccount);
									
									//Create a Pager to control the table.
								    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
								    pagerHistorizedStatsCommunityAccount = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
								    pagerHistorizedStatsCommunityAccount.setDisplay(tableHistorizedStatsCommAcc);
									
							    
								    //add to dock panel ======
								    dockPanel.add(pagerHistorizedStatsCommunityAccount, DockPanel.SOUTH);
								    pagerHistorizedStatsCommunityAccount.setPage(10);
								    pagerHistorizedStatsCommunityAccount.setVisible(true);
									
									dockPanel.add(tableHistorizedStatsCommAcc, DockPanel.SOUTH);
									tableHistorizedStatsCommAcc.setVisible(true);
								    
									dockPanel.add(pagerClan, DockPanel.SOUTH);
									dockPanel.add(tableClan, DockPanel.SOUTH);
									tableClan.setVisible(true);
									pagerClan.setVisible(true);
									
									tableHistorizedStatsCommAcc.setFocus(true);
									//dialogBox.center();
									//closeButton.setFocus(true);
								}
							});
					//searchClanButton.setEnabled(true);
					//searchClanButton.setFocus(true);
				}
				
				
				
			}
		////

			///////////
			// Create a handler for search clan's members
			class HandlerGetHistorizedStatsTanks implements ClickHandler, KeyUpHandler {
				/**
				 * Fired when the user clicks on the sendButton.
				 */
				public void onClick(ClickEvent event) {
					getHistorizedStatsTanks();
					offsetClan = 0;
					limitClan = 100;
				}
	
				/**
				 * Fired when the user types in the nameField.
				 */
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						getHistorizedStatsTanks();
						offsetClan = 0;
						limitClan = 100;
					}
				}
	
				/**
				 * Send the name from the nameField to the server and wait for a response.
				 */
				private void getHistorizedStatsTanks() {
					// First, we validate the input.
					hPanelLoading.setVisible(true);
					
				    // recup des users selected in dropBoxClanUsers
					List<String> listIdUser = new ArrayList<String>();
					int itemCount = dropBoxClanUsers.getItemCount();
					for(int i = 0 ;  i< itemCount ; i++) {
						if (dropBoxClanUsers.isItemSelected(i)) {
							listIdUser.add(dropBoxClanUsers.getValue(i));
						}
					}
					
					
					errorLabel.setText("");
					String textToServer = idClan;
					if (!FieldVerifier.isValidName(textToServer)) {
						errorLabel.setText("Please enter at least four characters");
						
						/////
						dialogBox
						.setText("Select a Clan before!!");
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML("Click on a clan before find members !"  );
						dialogBox.center();
						closeButton.setFocus(true);
						return;
					}
	
					// Then, we send the input to the server.
					//searchClanButton.setEnabled(false);
					textToServerLabel.setText(textToServer);
					serverResponseLabel.setText("");
					wotService.getHistorizedStatsTanks( listIdUser,
							new AsyncCallback<List<CommunityAccount>>() {
								public void onFailure(Throwable caught) {
									hPanelLoading.setVisible(false);
									// Show the RPC error message to the user
									dialogBox
											.setText("Remote Procedure Call - Failure");
									serverResponseLabel
											.addStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML(SERVER_ERROR);
									dialogBox.center();
									closeButton.setFocus(true);
									
								}
	
								public void onSuccess(List<CommunityAccount> listAccount) {
									hPanelLoading.setVisible(false);
									dockPanel.remove(tableHistorizedStatsTanksCommAcc);
									dockPanel.remove(tableClan);
									
									if (pagerHistorizedStatsTanksCommunityAccount != null) 
										dockPanel.remove(pagerHistorizedStatsTanksCommunityAccount);
									if (pagerClan != null) 
										dockPanel.remove(pagerClan);
									
									if (dataHistorizedStatsTanksProvider.getDataDisplays()!= null && !dataHistorizedStatsTanksProvider.getDataDisplays().isEmpty()) 
										dataHistorizedStatsTanksProvider.removeDataDisplay(tableHistorizedStatsTanksCommAcc);
									
									//on re-construit 1 nouveau tableau
									tableHistorizedStatsTanksCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);
									
									
									//construct column in celltable tableCommAcc , set data set sort handler etc ..
									buildACellTableForHistorizedStatsTanksCommunityAccount(listAccount);
									
									//Create a Pager to control the table.
								    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
								    pagerHistorizedStatsTanksCommunityAccount = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
								    pagerHistorizedStatsTanksCommunityAccount.setDisplay(tableHistorizedStatsTanksCommAcc);
									
							    
								    //add to dock panel ======
								    dockPanel.add(pagerHistorizedStatsTanksCommunityAccount, DockPanel.SOUTH);
								    pagerHistorizedStatsTanksCommunityAccount.setPage(10);
								    pagerHistorizedStatsTanksCommunityAccount.setVisible(true);
									
									dockPanel.add(tableHistorizedStatsTanksCommAcc, DockPanel.SOUTH);
									tableHistorizedStatsTanksCommAcc.setVisible(true);
								    
									dockPanel.add(pagerClan, DockPanel.SOUTH);
									dockPanel.add(tableClan, DockPanel.SOUTH);
									tableClan.setVisible(true);
									pagerClan.setVisible(true);
									
									tableHistorizedStatsTanksCommAcc.setFocus(true);
									//dialogBox.center();
									//closeButton.setFocus(true);
								}
							});
					//searchClanButton.setEnabled(true);
					//searchClanButton.setFocus(true);
				}
				
				
				
			}
		////
			
			
		////
			class HandlerGetAllMembersClan implements ClickHandler, KeyUpHandler {
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
					hPanelLoading.setVisible(true);
				    
					errorLabel.setText("");
					String textToServer = idClan;
					if (!FieldVerifier.isValidName(textToServer)) {
						errorLabel.setText("Please enter at least four characters");
						
						/////
						dialogBox
						.setText("Select a Clan before!!");
						serverResponseLabel
								.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML("Click on a clan before find members !"  );
						dialogBox.center();
						closeButton.setFocus(true);
						hPanelLoading.setVisible(false);
						return;
					}
	
					// Then, we send the input to the server.
					//searchClanButton.setEnabled(false);
					textToServerLabel.setText(textToServer);
					serverResponseLabel.setText("");
					wotService.getAllMembersClan(textToServer,
							new AsyncCallback<CommunityClan>() {
								public void onFailure(Throwable caught) {
									hPanelLoading.setVisible(false);
									// Show the RPC error message to the user
									dialogBox
											.setText("Remote Procedure Call - Failure");
									serverResponseLabel
											.addStyleName("serverResponseLabelError");
									serverResponseLabel.setHTML(SERVER_ERROR);
									dialogBox.center();
									closeButton.setFocus(true);
									
								}
	
								public void onSuccess(CommunityClan listAccount) {
									hPanelLoading.setVisible(false);
									//dropBoxClanUsers
									dropBoxClanUsers.clear();
									List<String> listAccName = new ArrayList<String>();
									hmAccNameAccId =new HashMap<String, String >();
									hmAccIdAccName =new HashMap<String, String >();
									
									for (DataCommunityClanMembers dataCom :  listAccount.getData().getMembers()) {
										for (DataCommunityMembers dataComMembers : dataCom.getMembers()) {
											listAccName.add(dataComMembers.getAccount_name());
											hmAccNameAccId.put(dataComMembers.getAccount_name(), dataComMembers.getAccount_id());
											hmAccIdAccName.put(dataComMembers.getAccount_id(), dataComMembers.getAccount_name());
											//dropBoxClanUsers.addItem(dataCom.getAccount_name());
										}
									}
									//sort the list 
									Collections.sort(listAccName);
									
									//add to the list 
									for (String accName : listAccName) {
										//list box contain  name of user and id of user
										dropBoxClanUsers.addItem(accName, hmAccNameAccId.get(accName));
									}
									dropBoxClanUsers.setFocus(true);
								}
							});
				}
				
				
				
			}
		////

			// Add a handler to send the name to the server
			HandlerGetAllMembersClanAndStats handlerFindMembers = new HandlerGetAllMembersClanAndStats();
			findMembersClanButton.addClickHandler(handlerFindMembers);
				
			// Add a handler to find historized stats 
			HandlerGetHistorizedStats handlerGetHistorizedStats = new HandlerGetHistorizedStats();
			findHistorizedStatsButton.addClickHandler(handlerGetHistorizedStats);

			// Add a handler to find historized stats tanks
			HandlerGetHistorizedStatsTanks handlerGetHistorizedStatsTanks = new HandlerGetHistorizedStatsTanks();
			findHistorizedStatsTanksButton.addClickHandler(handlerGetHistorizedStatsTanks);

			// Add a handler to find clans
			HandlerGetClans handlerGetClans = new HandlerGetClans();
			searchClansButton.addClickHandler(handlerGetClans);
			
			// Add a handler to find clans more
			searchClansButtonMore.addClickHandler(handlerGetClans);
			nameClan.addKeyUpHandler(handlerGetClans);
	
			//HandlerPersistStats
			// Add a handler to persist stats
			HandlerPersistStats handlerPersistStats = new HandlerPersistStats();
			persistStatsButton.addClickHandler(handlerPersistStats);
			//nameClan.addKeyUpHandler(handlerGetClans);
			
			
			//Add a handler to find clan's users button : searchUsersClanButton
			HandlerGetAllMembersClan handlerFindMembersClan = new HandlerGetAllMembersClan();
			searchUsersClanButton.addClickHandler(handlerFindMembersClan);
			
			
			// button HandlerGetAchivementsMember
			HandlerGetAchievementsMember myHandlerGetAchivementsMember = new HandlerGetAchievementsMember();
			findAchievementsMemberButton.addClickHandler(myHandlerGetAchivementsMember);

			
			
			
		}


	
			/*
		 * call this when we have data to put in table
		 */
		public  void buildACellTableForHistorizedStatsCommunityAccount(List<CommunityAccount> listCommAcc) {
	
			tableHistorizedStatsCommAcc.setTitle("Historical Battles");
			tableHistorizedStatsCommAcc.setPageSize(30);
			
		    //update dataprovider with some known list 
		    dataHistorizedStatsProvider.setList(listCommAcc);
			
			// Create a CellTable.
		    tableHistorizedStatsCommAcc.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		    
		    
		    ListHandler<CommunityAccount> columnSortHandler =
			        new ListHandler<CommunityAccount>(dataHistorizedStatsProvider.getList());
		    tableHistorizedStatsCommAcc.addColumnSortHandler(columnSortHandler);
		    
		    //
		    int sizeDate = 0;
		    final List<String> listDates = new ArrayList<String>(); 
		    for (CommunityAccount commAcc :  listCommAcc) {
		    	int size = commAcc.listDates.size();
		    	if (size > sizeDate) { 
		    		sizeDate = size ;
		    		for(String date : commAcc.listDates) {
		    			listDates.add(date);
		    		}
		    	}
		    }
		    // Add a text column to show the name.
		    TextColumn<CommunityAccount> nameColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return hmAccIdAccName.get(object.getIdUser());
		      }
		    };
		    tableHistorizedStatsCommAcc.addColumn(nameColumn, "Name");

		    nameColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nameColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		              return (o2 != null) ? o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase()) : 1;
		            }else
		            	return -1;
		          }
		        });
		    
		 // We know that the data is sorted alphabetically by default.
		    tableHistorizedStatsCommAcc.getColumnSortList().push(nameColumn);

	    	///
		    // JOUR SUIVANT ///////////////////////
		    TextColumn<CommunityAccount> jour1 = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listbattles.size() >= 2  ) {
		    		  int diff = object.listbattles.get(0) - object.listbattles.get(1);
		    		  return String.valueOf(diff) ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    String strDate =  listDates.get(0);
		    tableHistorizedStatsCommAcc.addColumn(jour1, "Battles-" + strDate);
	
		    jour1.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null) {
		            	Integer val1 = o1.listbattles.get(0) - o1.listbattles.get(1);
		            	Integer val2 = o2.listbattles.get(0)- o2.listbattles.get(1);
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
			 // We know that the data is sorted alphabetically by default.
		    tableHistorizedStatsCommAcc.getColumnSortList().push(jour1);
		    ////////////////////////////////////////////////////////
		    
		    // WR JOUR SUIVANT ///////////////////////
		    TextColumn<CommunityAccount> jour1Wr = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listbattles.size() >= 2  ) {
		    		  int diff = object.listbattles.get(0) - object.listbattles.get(1);
		    		  int diffWins = object.listBattlesWins.get(0) - object.listBattlesWins.get(1);
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    		  
		    		  return   wr ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(0);
		    tableHistorizedStatsCommAcc.addColumn(jour1Wr, "WR-" + strDate);
	
		    jour1Wr.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1Wr,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null) {
		            	int diff1 = o1.listbattles.get(0) - o1.listbattles.get(1);
			    		int diffWins1 = o1.listBattlesWins.get(0) - o1.listBattlesWins.get(1);
			    		Double wrCal1 = (double) ((double)diffWins1/(double)diff1);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal1 = wrCal1 * 100; //ex : 51,844444
			    		int intWrCal1 = (int) (wrCal1 * 100); //ex : 5184
			    		//wrCal1 = (double)intWrCal1 / 100 ; //ex : 51,84
			    		//String wr1 = String.valueOf(wrCal1);
		            	
			    		int diff2 = o2.listbattles.get(0) - o2.listbattles.get(1);
			    		int diffWins2 = o2.listBattlesWins.get(0) - o2.listBattlesWins.get(1);
			    		Double wrCal2 = (double) ((double)diffWins2/(double)diff2);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal2 = wrCal2 * 100; //ex : 51,844444
			    		int intWrCal2 = (int) (wrCal2 * 100); //ex : 5184
			    		//wrCal2 = (double)intWrCal2 / 100 ; //ex : 51,84
			    		//String wr2 = String.valueOf(wrCal2);
		            	//
		            	Integer val1 = intWrCal1;
		            	Integer val2 = intWrCal2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
		    
		    // JOUR SUIVANT ///////////////////////
		    // Add a text column to show the second day of battlle.
		    TextColumn<CommunityAccount> jour2 = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listbattles.size() >= 3  ) {
		    		  int diff = object.listbattles.get(1) - object.listbattles.get(2);
		    		  return String.valueOf(diff);
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    
		    strDate =  listDates.get(1);
		    tableHistorizedStatsCommAcc.addColumn(jour2, "Battles-" + strDate);
	
		    jour2.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null) {
		            	Integer val1 = o1.listbattles.get(1) - o1.listbattles.get(2);
		            	Integer val2 = o2.listbattles.get(1)- o2.listbattles.get(2);
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
	
		    // WR JOUR SUIVANT ///////////////////////
		    TextColumn<CommunityAccount> jour2Wr = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listbattles.size() >= 2  ) {
		    		  int a = 1;
		    		  int b = 2;
		    		  int diff = object.listbattles.get(a) - object.listbattles.get(b);
		    		  int diffWins = object.listBattlesWins.get(a) - object.listBattlesWins.get(b);
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    		  
		    		  return   wr ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(1);
		    tableHistorizedStatsCommAcc.addColumn(jour2Wr, "WR-" + strDate);
	
		    jour2Wr.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2Wr,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null) {
		            	int a = 1;
			    		int b = 2;
		            	int diff1 = o1.listbattles.get(a) - o1.listbattles.get(b);
			    		int diffWins1 = o1.listBattlesWins.get(a) - o1.listBattlesWins.get(b);
			    		Double wrCal1 = (double) ((double)diffWins1/(double)diff1);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal1 = wrCal1 * 100; //ex : 51,844444
			    		int intWrCal1 = (int) (wrCal1 * 100); //ex : 5184
			    		//wrCal1 = (double)intWrCal1 / 100 ; //ex : 51,84
			    		//String wr1 = String.valueOf(wrCal1);
		            	
			    		int diff2 = o2.listbattles.get(a) - o2.listbattles.get(b);
			    		int diffWins2 = o2.listBattlesWins.get(a) - o2.listBattlesWins.get(b);
			    		Double wrCal2 = (double) ((double)diffWins2/(double)diff2);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal2 = wrCal2 * 100; //ex : 51,844444
			    		int intWrCal2 = (int) (wrCal2 * 100); //ex : 5184
			    		//wrCal2 = (double)intWrCal2 / 100 ; //ex : 51,84
			    		//String wr2 = String.valueOf(wrCal2);
		            	//
		            	Integer val1 = intWrCal1;
		            	Integer val2 = intWrCal2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
		    
		    // JOUR SUIVANT ///////////////////////
		    // Add a text column to show the second day of battlle.
		    TextColumn<CommunityAccount> jour3 = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listbattles.size() >= 4  ) {
		    		  int diff = object.listbattles.get(2) - object.listbattles.get(3);
		    		  return String.valueOf(diff);
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    
		    strDate =  listDates.get(2);
		    tableHistorizedStatsCommAcc.addColumn(jour3, "Battles-" + strDate);
	
		    jour3.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour3,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null) {
		            	Integer val1 = o1.listbattles.get(2) - o1.listbattles.get(3);
		            	Integer val2 = o2.listbattles.get(2)- o2.listbattles.get(3);
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
	
		    // WR JOUR SUIVANT ///////////////////////
		    TextColumn<CommunityAccount> jour3Wr = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listbattles.size() >= 2  ) {
		    		  int a = 2;
		    		  int b = 3;
		    		  int diff = object.listbattles.get(a) - object.listbattles.get(b);
		    		  int diffWins = object.listBattlesWins.get(a) - object.listBattlesWins.get(b);
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    		  
		    		  return   wr ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(2);
		    tableHistorizedStatsCommAcc.addColumn(jour3Wr, "WR-" + strDate);
	
		    jour3Wr.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour3Wr,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null) {
		            	int a = 2;
			    		int b = 3;
		            	int diff1 = o1.listbattles.get(a) - o1.listbattles.get(b);
			    		int diffWins1 = o1.listBattlesWins.get(a) - o1.listBattlesWins.get(b);
			    		Double wrCal1 = (double) ((double)diffWins1/(double)diff1);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal1 = wrCal1 * 100; //ex : 51,844444
			    		int intWrCal1 = (int) (wrCal1 * 100); //ex : 5184
			    		//wrCal1 = (double)intWrCal1 / 100 ; //ex : 51,84
			    		//String wr1 = String.valueOf(wrCal1);
		            	
			    		int diff2 = o2.listbattles.get(a) - o2.listbattles.get(b);
			    		int diffWins2 = o2.listBattlesWins.get(a) - o2.listBattlesWins.get(b);
			    		Double wrCal2 = (double) ((double)diffWins2/(double)diff2);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal2 = wrCal2 * 100; //ex : 51,844444
			    		int intWrCal2 = (int) (wrCal2 * 100); //ex : 5184
			    		//wrCal2 = (double)intWrCal2 / 100 ; //ex : 51,84
			    		//String wr2 = String.valueOf(wrCal2);
		            	//
		            	Integer val1 = intWrCal1;
		            	Integer val2 = intWrCal2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
		    
		    // === JOUR SUIVANT === ///////////////////////
		    // Add a text column to show the second day of battlle.
		    TextColumn<CommunityAccount> jour4 = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listbattles.size() >= 5  ) {
		    		  int diff = object.listbattles.get(3) - object.listbattles.get(4);
		    		  return String.valueOf(diff);
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    if (listDates.size() >= 4 )
		    	strDate =  listDates.get(3);
		    else
		    	strDate = "";
		    
		    tableHistorizedStatsCommAcc.addColumn(jour4, "Battles-" + strDate);
	
		    jour4.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour4,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null &&  o1.listbattles.size() >= 5   &&  o2.listbattles.size() >= 5) {
		            	Integer val1 = o1.listbattles.get(3) - o1.listbattles.get(4);
		            	Integer val2 = o2.listbattles.get(3)- o2.listbattles.get(4);
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else 
		            	return -1;
		          }
		        });
	    	//
	   /////////////////////////////////////////////////////////////////
		    
		    // WR JOUR SUIVANT ///////////////////////
		    TextColumn<CommunityAccount> jour4Wr = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listbattles.size() >= 5  ) {
		    		  int a = 3;
		    		  int b = 4;
		    		  int diff = object.listbattles.get(a) - object.listbattles.get(b);
		    		  int diffWins = object.listBattlesWins.get(a) - object.listBattlesWins.get(b);
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    		  
		    		  return   wr ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    if (listDates.size() >= 4)
		    	strDate =  listDates.get(3);
		    else 
		    	strDate = "";
		    tableHistorizedStatsCommAcc.addColumn(jour4Wr, "WR-" + strDate);
	
		    jour4Wr.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour4Wr,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null) {
		            	int a = 3;
			    		int b = 4;
		            	int diff1 = o1.listbattles.get(a) - o1.listbattles.get(b);
			    		int diffWins1 = o1.listBattlesWins.get(a) - o1.listBattlesWins.get(b);
			    		Double wrCal1 = (double) ((double)diffWins1/(double)diff1);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal1 = wrCal1 * 100; //ex : 51,844444
			    		int intWrCal1 = (int) (wrCal1 * 100); //ex : 5184
			    		//wrCal1 = (double)intWrCal1 / 100 ; //ex : 51,84
			    		//String wr1 = String.valueOf(wrCal1);
		            	
			    		int diff2 = o2.listbattles.get(a) - o2.listbattles.get(b);
			    		int diffWins2 = o2.listBattlesWins.get(a) - o2.listBattlesWins.get(b);
			    		Double wrCal2 = (double) ((double)diffWins2/(double)diff2);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal2 = wrCal2 * 100; //ex : 51,844444
			    		int intWrCal2 = (int) (wrCal2 * 100); //ex : 5184
			    		//wrCal2 = (double)intWrCal2 / 100 ; //ex : 51,84
			    		//String wr2 = String.valueOf(wrCal2);
		            	//
		            	Integer val1 = intWrCal1;
		            	Integer val2 = intWrCal2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    
		    // === TOTAL DES JOURS  === ///////////////////////
		    TextColumn<CommunityAccount> totalJour = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  int total = 0;
		    	  int max  = object.listbattles.size() ; 
		    	  if (max > 5 ) 
		    		  max = 5 ;
		    	  for (int i =0; i < (max - 1) ; i++) {
		    		  total = total + object.listbattles.get(i) - object.listbattles.get(i+1);
		    	  }
		    	  return String.valueOf(total);
		      }
		    };
		    
		    strDate =  "Total";
		    tableHistorizedStatsCommAcc.addColumn(totalJour, strDate);
	
		    totalJour.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(totalJour,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null) {
		            	int total1 = 0 ;
		            	int total2 = 0 ;
		            	int maxO1  = o1.listbattles.size() ; 
				    	if (maxO1 > 5 ) 
				    		  maxO1 = 5 ;
				    	for (int i =0; i < (maxO1 - 1) ; i++) {
				    		  total1 = total1 + o1.listbattles.get(i) - o1.listbattles.get(i+1);
				    		  //total2 = total2 + o2.listbattles.get(i) - o2.listbattles.get(i+1);
				    	}
		            	int maxO2  = o2.listbattles.size() ; 
				    	  if (maxO2 > 5 ) 
				    		  maxO2 = 5 ;
				    	  for (int i =0; i < (maxO2 - 1) ; i++) {
				    		  //total2 = total1 + o1.listbattles.get(i) - o1.listbattles.get(i+1);
				    		  total2 = total2 + o2.listbattles.get(i) - o2.listbattles.get(i+1);
				    	  }
		            	
		            	
		            	Integer val1 = total1;
		            	Integer val2 = total2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else 
		            	return -1;
		          }
		        });
	    	//
	    /////////////////////////////////////////////////////////////////
		    // === WR TOTAL DES JOURS  === ///////////////////////
		    TextColumn<CommunityAccount> WrTotalJour = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  /**
		    	   * int a = 3;
		    		  int b = 4;
		    		  int diff = object.listbattles.get(a) - object.listbattles.get(b);
		    		  int diffWins = object.listBattlesWins.get(a) - object.listBattlesWins.get(b);
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    	   */
		    	  int totalBattles = 0;
		    	  int max  = object.listbattles.size() ; 
		    	  if (max > 5 ) 
		    		  max = 5 ;
		    	  for (int i =0; i < (max - 1) ; i++) {
		    		  totalBattles = totalBattles + object.listbattles.get(i) - object.listbattles.get(i+1);
		    	  }

		    	  int totalBattlesWins = 0;
		    	  int maxWins  = object.listBattlesWins.size() ; 
		    	  if (maxWins > 5 ) 
		    		  maxWins = 5 ;
		    	  for (int i =0; i < (maxWins - 1) ; i++) {
		    		  totalBattlesWins = totalBattlesWins + object.listBattlesWins.get(i) - object.listBattlesWins.get(i+1);
		    	  }

		    	  Double wrCal = (double) ((double)totalBattlesWins/(double)totalBattles);
	    		  //on ne conserve que 2 digits après la virgule 
	    		  wrCal = wrCal * 100; //ex : 51,844444
	    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
	    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
	    		  String wr = String.valueOf(wrCal);
	    		  
		    	  return wr;
		      }
		    };
		    
		    strDate =  "WR Total";
		    tableHistorizedStatsCommAcc.addColumn(WrTotalJour, strDate);
	
		    WrTotalJour.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(WrTotalJour,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null) {
		            	Double totalBattle1 = 0.0 ;
		            	Double totalBattle2 = 0.0 ;
		            	Double totalBattleWins1 = 0.0 ;
		            	Double totalBattleWins2 = 0.0 ;
		            	
		            	int maxBattleO1  = o1.listbattles.size() ; 
				    	if (maxBattleO1 > 5 ) 
				    		  maxBattleO1 = 5 ;
				    	for (int i =0; i < (maxBattleO1 - 1) ; i++) {
				    		  totalBattle1 = totalBattle1 + o1.listbattles.get(i) - o1.listbattles.get(i+1);
				    		  totalBattleWins1 = totalBattleWins1 + o1.listBattlesWins.get(i) - o1.listBattlesWins.get(i+1);
				    		  //total2 = total2 + o2.listbattles.get(i) - o2.listbattles.get(i+1);
				    	}
		            	int maxBattleO2  = o2.listbattles.size() ; 
				    	  if (maxBattleO2 > 5 ) 
				    		  maxBattleO2 = 5 ;
				    	  for (int i =0; i < (maxBattleO2 - 1) ; i++) {
				    		  //total2 = total1 + o1.listbattles.get(i) - o1.listbattles.get(i+1);
				    		  totalBattle2 = totalBattle2 + o2.listbattles.get(i) - o2.listbattles.get(i+1);
				    		  totalBattleWins2 = totalBattleWins2 + o2.listBattlesWins.get(i) - o2.listBattlesWins.get(i+1);
				    	  }
		            	
				    	Double val1 =0.0;
				    	Double val2 =0.0;
		            	if (totalBattle1 != 0.0   ) { 
		            		val1 = totalBattleWins1/totalBattle1; val1 = val1 *100;
		            	}
		            	if ( totalBattle2 != 0.0 ) {
		            		val2 = totalBattleWins2/totalBattle2;val2 = val2 * 100 ;
		            	}
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else 
		            	return -1;
		          }
		        });
	    	//
	    /////////////////////////////////////////////////////////////////
		    
		    //////////////////////////////////////////////////////////////////
		    // Add a selection model to handle user selection.
		    final SingleSelectionModel<CommunityAccount> selectionModel = new SingleSelectionModel<CommunityAccount>();
		    tableHistorizedStatsCommAcc.setSelectionModel(selectionModel);
		    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event) {
		    	  CommunityAccount selected = selectionModel.getSelectedObject();
		        if (selected != null) {
		          //Window.alert("You selected: " + selected.getName());
		        }
		      }
		    });
	
		    // Set the total row count. This isn't strictly necessary, but it affects
		    // paging calculations, so its good habit to keep the row count up to date.
		    tableHistorizedStatsCommAcc.setRowCount(listCommAcc.size(), true); //no need to do here because we have add list to data provider
	
		    // Push the data into the widget.
		    tableHistorizedStatsCommAcc.setRowData(0, listCommAcc);            //idem no nedd dataprovider
		    
		 // Connect the table to the data provider.
		    dataHistorizedStatsProvider.addDataDisplay(tableHistorizedStatsCommAcc);
		    dataHistorizedStatsProvider.refresh();
	   }


		/*
		 * call this when we have data to put in table
		 */
		public  void buildACellTableForHistorizedStatsTanksCommunityAccount(List<CommunityAccount> listCommAcc) {
	
			tableHistorizedStatsTanksCommAcc.setTitle("Historical Battles Tanks");
			tableHistorizedStatsTanksCommAcc.setPageSize(30);
			
		    //update dataprovider with some known list 
		    dataHistorizedStatsTanksProvider.setList(listCommAcc);
			
			// Create a CellTable.
		    tableHistorizedStatsTanksCommAcc.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		    
		    
		    ListHandler<CommunityAccount> columnSortHandler =
			        new ListHandler<CommunityAccount>(dataHistorizedStatsTanksProvider.getList());
		    tableHistorizedStatsTanksCommAcc.addColumnSortHandler(columnSortHandler);
		    
		    //
		    int sizeDate = 0;
		    final List<String> listDates = new ArrayList<String>(); 
		    for (CommunityAccount commAcc :  listCommAcc) {
		    	int size = commAcc.listDates.size();
		    	if (size > sizeDate) { 
		    		sizeDate = size ;
		    		for(String date : commAcc.listDates) {
		    			listDates.add(date);
		    		}
		    	}
		    }
		    // Add a text column to show the name.
		    TextColumn<CommunityAccount> nameColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return object.getName();
		      }
		    };
		    tableHistorizedStatsTanksCommAcc.addColumn(nameColumn, "Name");

		    nameColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nameColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		              return (o2 != null) ? o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase()) : 1;
		            }else
		            	return -1;
		          }
		        });
		    
		 // We know that the data is sorted alphabetically by default.
		    tableHistorizedStatsTanksCommAcc.getColumnSortList().push(nameColumn);

	    	///
		    // TANK 1 JOUR 1  ///////////////////////
		    TextColumn<CommunityAccount> jour1Tank1 = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 2  ) {
		    		  List<DataCommunityAccountVehicules> listVehPlayed = new ArrayList<DataCommunityAccountVehicules>();
		    		  
		    		  DataCommunityAccount dataCommAccOfDay0 = object.listBattlesTanks.get(0);
		    		  DataCommunityAccount dataCommAccOfDay1 = object.listBattlesTanks.get(1);
		    		  //bcl sur les stats vehicules du joueur pour le jour en question (1) 
		    		  for (DataCommunityAccountVehicules dataCommAccVeh0 : dataCommAccOfDay0.getVehicles()) {
		    			  //pour chaque vehicule du jour 0, il faut trouver le véhicule correspondant dans  ceux du jour 1 pour éventuellement détecté qu'il a été joué ( + battle)
		    			  //et le mémoriser dans une liste de tanks joués
		    			  //A la fin on ne prendra que ceux qui ont été le + joués) 
		    			  //
		    			  for (DataCommunityAccountVehicules dataCommAccVeh1 : dataCommAccOfDay1.getVehicles()) {
			    			  //char trouvé dans liste
			    			  if(dataCommAccVeh0.getName().equalsIgnoreCase(dataCommAccVeh1.getName())) {
			    				  if (dataCommAccVeh0.getBattle_count() >  dataCommAccVeh1.getBattle_count()) {
			    					  //le char a été joué il faut l'ajouter  à la liste
			    					  dataCommAccVeh0.setCountBattleSincePreviousDay(dataCommAccVeh0.getBattle_count() - dataCommAccVeh1.getBattle_count());
			    					  dataCommAccVeh0.setWinCountBattleSincePreviousDay(dataCommAccVeh0.getWin_count() - dataCommAccVeh1.getWin_count());
			    					  listVehPlayed.add(dataCommAccVeh0);
			    				  }
			    				  break;
			    			  }
			    		  }
		    			  
		    		  }
		    		  
		    		  //Trier listVehPlayed selon getBattle_count 
		    		  Collections.sort(listVehPlayed);
		    		  object.setListVehPlayedSincePreviousDay0(listVehPlayed);
		    		  
		    		  if(listVehPlayed.size() > 0)
		    			  return String.valueOf(listVehPlayed.get(0).getName() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    String strDate =  listDates.get(0);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour1Tank1, "1er Tank Most played-" + strDate);
	
		    jour1Tank1.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1Tank1,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		String val1 = "";
			    		if (o1.getListVehPlayedDay0() != null && o1.getListVehPlayedDay0().size() > 0)
			    			 val1 =  o1.getListVehPlayedDay0().get(0).getName();
			    		else
			    			val1="";
			    		
			    		String val2 = "";
			    		if (o2.getListVehPlayedDay0() != null && o2.getListVehPlayedDay0().size() > 0)
			    			 val2 =  o2.getListVehPlayedDay0().get(0).getName();
			    		else
			    			val2="";
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
	    	///
		    // TANK 1 BATTLE JOUR 1 ///////////////////////
		    TextColumn<CommunityAccount> jour1Tank1Battle = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 2  ) {
		    		  if(object.getListVehPlayedDay0().size() > 0)
		    			  return String.valueOf(object.getListVehPlayedDay0().get(0).getCountBattleSincePreviousDay() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(0);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour1Tank1Battle, "Nb Battles tank" );
	
		    jour1Tank1Battle.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1Tank1Battle,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		Integer val1 = 0;
			    		if (o1.getListVehPlayedDay0() != null && o1.getListVehPlayedDay0().size() > 0)
			    			 val1 =  o1.getListVehPlayedDay0().get(0).getCountBattleSincePreviousDay();
			    		else
			    			val1=0;
			    		
			    		Integer val2 = 0;
			    		if (o2.getListVehPlayedDay0() != null && o2.getListVehPlayedDay0().size() > 0)
			    			 val2 =  o2.getListVehPlayedDay0().get(0).getCountBattleSincePreviousDay();
			    		else
			    			val2=0;
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
		    
		    /////////////////////////////////////////////////////////////
		    // WR TANK 1 JOUR 1 ///////////////////////
		    TextColumn<CommunityAccount> jour1Tank1Wr = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  //////
		    	  if ( object.getListVehPlayedDay0().size() > 0 &&  object.getListVehPlayedDay0().get(0) != null  ) {
		    		  int diff =  object.getListVehPlayedDay0().get(0).getCountBattleSincePreviousDay();
		    		  
		    		  int diffWins =  object.getListVehPlayedDay0().get(0).getWinCountBattleSincePreviousDay();
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    		  
		    		  return   wr ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(0);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour1Tank1Wr, "Wr Tank" );
	
		    jour1Tank1Wr.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1Tank1Wr,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null && o1.getListVehPlayedDay0().size()> 0 && o2.getListVehPlayedDay0().size()> 0) {
		            	/////////////
		            	int diff1 = o1.getListVehPlayedDay0().get(0).getCountBattleSincePreviousDay();
			    		int diffWins1 = o1.getListVehPlayedDay0().get(0).getWinCountBattleSincePreviousDay();
			    		Double wrCal1 = (double) ((double)diffWins1/(double)diff1);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal1 = wrCal1 * 100; //ex : 51,844444
			    		int intWrCal1 = (int) (wrCal1 * 100); //ex : 5184
			    		//wrCal1 = (double)intWrCal1 / 100 ; //ex : 51,84
			    		//String wr1 = String.valueOf(wrCal1);
		            	
			    		int diff2 = o2.getListVehPlayedDay0().get(0).getCountBattleSincePreviousDay();
			    		int diffWins2 = o2.getListVehPlayedDay0().get(0).getWinCountBattleSincePreviousDay();
			    		Double wrCal2 = (double) ((double)diffWins2/(double)diff2);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal2 = wrCal2 * 100; //ex : 51,844444
			    		int intWrCal2 = (int) (wrCal2 * 100); //ex : 5184
			    		//wrCal2 = (double)intWrCal2 / 100 ; //ex : 51,84
			    		//String wr2 = String.valueOf(wrCal2);
		            	//
		            	Integer val1 = intWrCal1;
		            	Integer val2 = intWrCal2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
//		    ////////////////////////////////////////////////////////
		    
		    
		    
	    	///
		    // TANK 2 JOUR 1  ///////////////////////
		    TextColumn<CommunityAccount> jour1Tank2 = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 3  ) {
		    		  
		    		  if(object.getListVehPlayedDay0().size() > 1)
		    			  return String.valueOf(object.getListVehPlayedDay0().get(1).getName() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(0);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour1Tank2, "2nd Tank Most played-" + strDate);
	
		    jour1Tank2.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1Tank2,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		String val1 = "";
			    		if (o1.getListVehPlayedDay0().size() > 1)
			    			 val1 =  o1.getListVehPlayedDay0().get(1).getName();
			    		else
			    			val1="";
			    		
			    		String val2 = "";
			    		if (o2.getListVehPlayedDay0().size() > 1)
			    			 val2 =  o2.getListVehPlayedDay0().get(1).getName();
			    		else
			    			val2="";
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
	    	///
		    // BATTLE TANK 2 JOUR 1 ///////////////////////
		    TextColumn<CommunityAccount> jour1Tank2Battle = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 3  ) {

		    		  
		    		  if(object.getListVehPlayedDay0().size() > 1)
		    			  return String.valueOf(object.getListVehPlayedDay0().get(1).getCountBattleSincePreviousDay() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(0);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour1Tank2Battle, "Nb Battles tank" );
	
		    jour1Tank2Battle.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1Tank2Battle,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		Integer val1 = 0;
			    		if (o1.getListVehPlayedDay0().size() > 1)
			    			 val1 =  o1.getListVehPlayedDay0().get(1).getCountBattleSincePreviousDay();
			    		else
			    			val1=0;
			    		
			    		Integer val2 = 0;
			    		if (o2.getListVehPlayedDay0().size() > 1)
			    			 val2 =  o2.getListVehPlayedDay0().get(1).getCountBattleSincePreviousDay();
			    		else
			    			val2=0;
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
		    
		    /////////////////////////////////////////////////////////////
		    // WR TANK 2 JOUR 1 ///////////////////////
		    TextColumn<CommunityAccount> jour1Tank2Wr = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  //////
		    	  if ( object.getListVehPlayedDay0().size() > 1 &&  object.getListVehPlayedDay0().get(1) != null  ) {
		    		  int diff =  object.getListVehPlayedDay0().get(1).getCountBattleSincePreviousDay();
		    		  
		    		  int diffWins =  object.getListVehPlayedDay0().get(1).getWinCountBattleSincePreviousDay();
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    		  
		    		  return   wr ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(0);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour1Tank2Wr, "Wr Tank" );
	
		    jour1Tank2Wr.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1Tank2Wr,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null && o1.getListVehPlayedDay0().size()> 1 && o2.getListVehPlayedDay0().size()> 1) {
		            	/////////////
		            	int diff1 = o1.getListVehPlayedDay0().get(1).getCountBattleSincePreviousDay();
			    		int diffWins1 = o1.getListVehPlayedDay0().get(1).getWinCountBattleSincePreviousDay();
			    		Double wrCal1 = (double) ((double)diffWins1/(double)diff1);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal1 = wrCal1 * 100; //ex : 51,844444
			    		int intWrCal1 = (int) (wrCal1 * 100); //ex : 5184
			    		//wrCal1 = (double)intWrCal1 / 100 ; //ex : 51,84
			    		//String wr1 = String.valueOf(wrCal1);
		            	
			    		int diff2 = o2.getListVehPlayedDay0().get(1).getCountBattleSincePreviousDay();
			    		int diffWins2 = o2.getListVehPlayedDay0().get(1).getWinCountBattleSincePreviousDay();
			    		Double wrCal2 = (double) ((double)diffWins2/(double)diff2);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal2 = wrCal2 * 100; //ex : 51,844444
			    		int intWrCal2 = (int) (wrCal2 * 100); //ex : 5184
			    		//wrCal2 = (double)intWrCal2 / 100 ; //ex : 51,84
			    		//String wr2 = String.valueOf(wrCal2);
		            	//
		            	Integer val1 = intWrCal1;
		            	Integer val2 = intWrCal2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
//		    ////////////////////////////////////////////////////////
		    
		    
		    // TANK 3 JOUR 1  ///////////////////////
		    TextColumn<CommunityAccount> jour1Tank3 = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 4  ) {
		    		  
		    		  if(object.getListVehPlayedDay0().size() > 2)
		    			  return String.valueOf(object.getListVehPlayedDay0().get(2).getName() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(0);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour1Tank3, "Third Tank Most played-" + strDate);
	
		    jour1Tank3.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1Tank3,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		String val1 = "";
			    		if (o1.getListVehPlayedDay0().size() > 2)
			    			 val1 =  o1.getListVehPlayedDay0().get(2).getName();
			    		else
			    			val1="";
			    		
			    		String val2 = "";
			    		if (o2.getListVehPlayedDay0().size() > 2)
			    			 val2 =  o2.getListVehPlayedDay0().get(2).getName();
			    		else
			    			val2="";
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
	    	///
		    // BATTLE TANK 3 JOUR 1  ///////////////////////
		    TextColumn<CommunityAccount> jour1Tank3Battle = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 4  ) {

		    		  
		    		  if(object.getListVehPlayedDay0().size() > 2)
		    			  return String.valueOf(object.getListVehPlayedDay0().get(2).getCountBattleSincePreviousDay() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(0);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour1Tank3Battle, "Nb Battles tank" );
	
		    jour1Tank2Battle.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1Tank3Battle,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		Integer val1 = 0;
			    		if (o1.getListVehPlayedDay0().size() > 2)
			    			 val1 =  o1.getListVehPlayedDay0().get(2).getCountBattleSincePreviousDay();
			    		else
			    			val1=0;
			    		
			    		Integer val2 = 0;
			    		if (o2.getListVehPlayedDay0().size() > 2)
			    			 val2 =  o2.getListVehPlayedDay0().get(2).getCountBattleSincePreviousDay();
			    		else
			    			val2=0;
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
		    
		    /////////////////////////////////////////////////////////////
		    // WR TANK 3 JOUR 1 ///////////////////////
		    TextColumn<CommunityAccount> jour1Tank3Wr = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  //////
		    	  if ( object.getListVehPlayedDay0().size() > 2 &&  object.getListVehPlayedDay0().get(2) != null  ) {
		    		  int diff =  object.getListVehPlayedDay0().get(2).getCountBattleSincePreviousDay();
		    		  
		    		  int diffWins =  object.getListVehPlayedDay0().get(2).getWinCountBattleSincePreviousDay();
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    		  
		    		  return   wr ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(0);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour1Tank3Wr, "Wr Tank" );
	
		    jour1Tank3Wr.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour1Tank3Wr,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null && o1.getListVehPlayedDay0().size()> 2 && o2.getListVehPlayedDay0().size()> 2) {
		            	/////////////
		            	int diff1 = o1.getListVehPlayedDay0().get(2).getCountBattleSincePreviousDay();
			    		int diffWins1 = o1.getListVehPlayedDay0().get(2).getWinCountBattleSincePreviousDay();
			    		Double wrCal1 = (double) ((double)diffWins1/(double)diff1);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal1 = wrCal1 * 100; //ex : 51,844444
			    		int intWrCal1 = (int) (wrCal1 * 100); //ex : 5184
			    		//wrCal1 = (double)intWrCal1 / 100 ; //ex : 51,84
			    		//String wr1 = String.valueOf(wrCal1);
		            	
			    		int diff2 = o2.getListVehPlayedDay0().get(2).getCountBattleSincePreviousDay();
			    		int diffWins2 = o2.getListVehPlayedDay0().get(2).getWinCountBattleSincePreviousDay();
			    		Double wrCal2 = (double) ((double)diffWins2/(double)diff2);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal2 = wrCal2 * 100; //ex : 51,844444
			    		int intWrCal2 = (int) (wrCal2 * 100); //ex : 5184
			    		//wrCal2 = (double)intWrCal2 / 100 ; //ex : 51,84
			    		//String wr2 = String.valueOf(wrCal2);
		            	//
		            	Integer val1 = intWrCal1;
		            	Integer val2 = intWrCal2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//

	    /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////
	    	///
		    // TANK 1 JOUR 2  ///////////////////////
		    TextColumn<CommunityAccount> jour2Tank1 = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 2  ) {
		    		  List<DataCommunityAccountVehicules> listVehPlayed = new ArrayList<DataCommunityAccountVehicules>();
		    		  
		    		  DataCommunityAccount dataCommAccOfDay0 = object.listBattlesTanks.get(1);
		    		  DataCommunityAccount dataCommAccOfDay1 = object.listBattlesTanks.get(2);
		    		  //bcl sur les stats vehicules du joueur pour le jour en question (1) 
		    		  for (DataCommunityAccountVehicules dataCommAccVeh0 : dataCommAccOfDay0.getVehicles()) {
		    			  //pour chaque vehicule du jour 0, il faut trouver le véhicule correspondant dans  ceux du jour 1 pour éventuellement détecté qu'il a été joué ( + battle)
		    			  //et le mémoriser dans une liste de tanks joués
		    			  //A la fin on ne prendra que ceux qui ont été le + joués) 
		    			  //
		    			  for (DataCommunityAccountVehicules dataCommAccVeh1 : dataCommAccOfDay1.getVehicles()) {
			    			  //char trouvé dans liste
			    			  if(dataCommAccVeh0.getName().equalsIgnoreCase(dataCommAccVeh1.getName())) {
			    				  if (dataCommAccVeh0.getBattle_count() >  dataCommAccVeh1.getBattle_count()) {
			    					  //le char a été joué il faut l'ajouter  à la liste
			    					  dataCommAccVeh0.setCountBattleSincePreviousDay(dataCommAccVeh0.getBattle_count() - dataCommAccVeh1.getBattle_count());
			    					  dataCommAccVeh0.setWinCountBattleSincePreviousDay(dataCommAccVeh0.getWin_count() - dataCommAccVeh1.getWin_count());
			    					  listVehPlayed.add(dataCommAccVeh0);
			    				  }
			    				  break;
			    			  }
			    		  }
		    			  
		    		  }
		    		  
		    		  //Trier listVehPlayed selon getBattle_count 
		    		  Collections.sort(listVehPlayed);
		    		  object.setListVehPlayedSincePreviousDay1(listVehPlayed);
		    		  
		    		  if(listVehPlayed.size() > 0)
		    			  return String.valueOf(listVehPlayed.get(0).getName() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(1);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour2Tank1, "1er Tank Most played-" + strDate);
	
		    jour2Tank1.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2Tank1,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		String val1 = "";
			    		if (o1.getListVehPlayedDay1().size() > 0)
			    			 val1 =  o1.getListVehPlayedDay1().get(0).getName();
			    		else
			    			val1="";
			    		
			    		String val2 = "";
			    		if (o2.getListVehPlayedDay1().size() > 0)
			    			 val2 =  o2.getListVehPlayedDay1().get(0).getName();
			    		else
			    			val2="";
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
	    	///
		    // TANK 1 BATTLE JOUR 1 ///////////////////////
		    TextColumn<CommunityAccount> jour2Tank1Battle = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 2  ) {
		    		  if(object.getListVehPlayedDay1().size() > 0)
		    			  return String.valueOf(object.getListVehPlayedDay1().get(0).getCountBattleSincePreviousDay() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(1);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour2Tank1Battle, "Nb Battles tank" );
	
		    jour2Tank1Battle.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2Tank1Battle,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		Integer val1 = 0;
			    		if (o1.getListVehPlayedDay1().size() > 0)
			    			 val1 =  o1.getListVehPlayedDay1().get(0).getCountBattleSincePreviousDay();
			    		else
			    			val1=0;
			    		
			    		Integer val2 = 0;
			    		if (o2.getListVehPlayedDay1().size() > 0)
			    			 val2 =  o2.getListVehPlayedDay1().get(0).getCountBattleSincePreviousDay();
			    		else
			    			val2=0;
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
		    
		    /////////////////////////////////////////////////////////////
		    // WR TANK 1 JOUR 2 ///////////////////////
		    TextColumn<CommunityAccount> jour2Tank1Wr = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  //////
		    	  if ( object.getListVehPlayedDay1().size() > 0 &&  object.getListVehPlayedDay1().get(0) != null  ) {
		    		  int diff =  object.getListVehPlayedDay1().get(0).getCountBattleSincePreviousDay();
		    		  
		    		  int diffWins =  object.getListVehPlayedDay1().get(0).getWinCountBattleSincePreviousDay();
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    		  
		    		  return   wr ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(1);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour2Tank1Wr, "Wr Tank" );
	
		    jour2Tank1Wr.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2Tank1Wr,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null && o1.getListVehPlayedDay1().size()> 0 && o2.getListVehPlayedDay1().size()> 0) {
		            	/////////////
		            	int diff1 = o1.getListVehPlayedDay1().get(0).getCountBattleSincePreviousDay();
			    		int diffWins1 = o1.getListVehPlayedDay1().get(0).getWinCountBattleSincePreviousDay();
			    		Double wrCal1 = (double) ((double)diffWins1/(double)diff1);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal1 = wrCal1 * 100; //ex : 51,844444
			    		int intWrCal1 = (int) (wrCal1 * 100); //ex : 5184
			    		//wrCal1 = (double)intWrCal1 / 100 ; //ex : 51,84
			    		//String wr1 = String.valueOf(wrCal1);
		            	
			    		int diff2 = o2.getListVehPlayedDay1().get(0).getCountBattleSincePreviousDay();
			    		int diffWins2 = o2.getListVehPlayedDay1().get(0).getWinCountBattleSincePreviousDay();
			    		Double wrCal2 = (double) ((double)diffWins2/(double)diff2);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal2 = wrCal2 * 100; //ex : 51,844444
			    		int intWrCal2 = (int) (wrCal2 * 100); //ex : 5184
			    		//wrCal2 = (double)intWrCal2 / 100 ; //ex : 51,84
			    		//String wr2 = String.valueOf(wrCal2);
		            	//
		            	Integer val1 = intWrCal1;
		            	Integer val2 = intWrCal2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
//		    ////////////////////////////////////////////////////////
	    	///
		    // TANK 2 JOUR 2  ///////////////////////
		    TextColumn<CommunityAccount> jour2Tank2 = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 3  ) {
		    		  
		    		  if(object.getListVehPlayedDay1().size() > 1)
		    			  return String.valueOf(object.getListVehPlayedDay1().get(1).getName() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(1);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour2Tank2, "2nd Tank Most played-" + strDate);
	
		    jour2Tank2.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2Tank2,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		String val1 = "";
			    		if (o1.getListVehPlayedDay1().size() > 1)
			    			 val1 =  o1.getListVehPlayedDay1().get(1).getName();
			    		else
			    			val1="";
			    		
			    		String val2 = "";
			    		if (o2.getListVehPlayedDay1().size() > 1)
			    			 val2 =  o2.getListVehPlayedDay1().get(1).getName();
			    		else
			    			val2="";
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
	    	///
		    // BATTLE TANK 2 JOUR 2 ///////////////////////
		    TextColumn<CommunityAccount> jour2Tank2Battle = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 3  ) {

		    		  
		    		  if(object.getListVehPlayedDay1().size() > 1)
		    			  return String.valueOf(object.getListVehPlayedDay1().get(1).getCountBattleSincePreviousDay() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(1);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour2Tank2Battle, "Nb Battles tank" );
	
		    jour2Tank2Battle.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2Tank2Battle,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		Integer val1 = 0;
			    		if (o1.getListVehPlayedDay1().size() > 1)
			    			 val1 =  o1.getListVehPlayedDay1().get(1).getCountBattleSincePreviousDay();
			    		else
			    			val1=0;
			    		
			    		Integer val2 = 0;
			    		if (o2.getListVehPlayedDay1().size() > 1)
			    			 val2 =  o2.getListVehPlayedDay1().get(1).getCountBattleSincePreviousDay();
			    		else
			    			val2=0;
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
		    
		    /////////////////////////////////////////////////////////////
		    // WR TANK 2 JOUR 2 ///////////////////////
		    TextColumn<CommunityAccount> jour2Tank2Wr = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  //////
		    	  if ( object.getListVehPlayedDay1().size() > 1 &&  object.getListVehPlayedDay1().get(1) != null  ) {
		    		  int diff =  object.getListVehPlayedDay1().get(1).getCountBattleSincePreviousDay();
		    		  
		    		  int diffWins =  object.getListVehPlayedDay1().get(1).getWinCountBattleSincePreviousDay();
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    		  
		    		  return   wr ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(1);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour2Tank2Wr, "Wr Tank" );
	
		    jour2Tank2Wr.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2Tank2Wr,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null && o1.getListVehPlayedDay1().size()> 1 && o2.getListVehPlayedDay1().size()> 1) {
		            	/////////////
		            	int diff1 = o1.getListVehPlayedDay1().get(1).getCountBattleSincePreviousDay();
			    		int diffWins1 = o1.getListVehPlayedDay1().get(1).getWinCountBattleSincePreviousDay();
			    		Double wrCal1 = (double) ((double)diffWins1/(double)diff1);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal1 = wrCal1 * 100; //ex : 51,844444
			    		int intWrCal1 = (int) (wrCal1 * 100); //ex : 5184
			    		//wrCal1 = (double)intWrCal1 / 100 ; //ex : 51,84
			    		//String wr1 = String.valueOf(wrCal1);
		            	
			    		int diff2 = o2.getListVehPlayedDay1().get(1).getCountBattleSincePreviousDay();
			    		int diffWins2 = o2.getListVehPlayedDay1().get(1).getWinCountBattleSincePreviousDay();
			    		Double wrCal2 = (double) ((double)diffWins2/(double)diff2);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal2 = wrCal2 * 100; //ex : 51,844444
			    		int intWrCal2 = (int) (wrCal2 * 100); //ex : 5184
			    		//wrCal2 = (double)intWrCal2 / 100 ; //ex : 51,84
			    		//String wr2 = String.valueOf(wrCal2);
		            	//
		            	Integer val1 = intWrCal1;
		            	Integer val2 = intWrCal2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
//		    ////////////////////////////////////////////////////////
		    
		    
		    // TANK 3 JOUR 2  ///////////////////////
		    TextColumn<CommunityAccount> jour2Tank3 = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 4  ) {
		    		  
		    		  if(object.getListVehPlayedDay1().size() > 2)
		    			  return String.valueOf(object.getListVehPlayedDay1().get(2).getName() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(1);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour2Tank3, "Third Tank Most played-" + strDate);
	
		    jour2Tank3.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2Tank3,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		String val1 = "";
			    		if (o1.getListVehPlayedDay1().size() > 2)
			    			 val1 =  o1.getListVehPlayedDay1().get(2).getName();
			    		else
			    			val1="";
			    		
			    		String val2 = "";
			    		if (o2.getListVehPlayedDay1().size() > 2)
			    			 val2 =  o2.getListVehPlayedDay1().get(2).getName();
			    		else
			    			val2="";
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
	    	///
		    // BATTLE TANK 3 JOUR 2  ///////////////////////
		    TextColumn<CommunityAccount> jour2Tank3Battle = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  if (object.listBattlesTanks.size() >= 4  ) {

		    		  
		    		  if(object.getListVehPlayedDay1().size() > 2)
		    			  return String.valueOf(object.getListVehPlayedDay1().get(2).getCountBattleSincePreviousDay() ) ;
		    		  else
		    			  return "";
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(1);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour2Tank3Battle, "Nb Battles tank" );
	
		    jour2Tank3Battle.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2Tank3Battle,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
	            if (o1 != null) {
			    		Integer val1 = 0;
			    		if (o1.getListVehPlayedDay1().size() > 2)
			    			 val1 =  o1.getListVehPlayedDay1().get(2).getCountBattleSincePreviousDay();
			    		else
			    			val1=0;
			    		
			    		Integer val2 = 0;
			    		if (o2.getListVehPlayedDay1().size() > 2)
			    			 val2 =  o2.getListVehPlayedDay1().get(2).getCountBattleSincePreviousDay();
			    		else
			    			val2=0;
			    		
			    		return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//
		    ////////////////////////////////////////////////////////
		    
		    
		    /////////////////////////////////////////////////////////////
		    // WR TANK 3 JOUR 2 ///////////////////////
		    TextColumn<CommunityAccount> jour2Tank3Wr = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  //////
		    	  if ( object.getListVehPlayedDay1().size() > 2 &&  object.getListVehPlayedDay1().get(2) != null  ) {
		    		  int diff =  object.getListVehPlayedDay1().get(2).getCountBattleSincePreviousDay();
		    		  
		    		  int diffWins =  object.getListVehPlayedDay1().get(2).getWinCountBattleSincePreviousDay();
		    		  Double wrCal = (double) ((double)diffWins/(double)diff);
		    		  //on ne conserve que 2 digits après la virgule 
		    		  wrCal = wrCal * 100; //ex : 51,844444
		    		  int intWrCal = (int) (wrCal * 100); //ex : 5184
		    		  wrCal = (double)intWrCal / 100 ; //ex : 51,84
		    		  String wr = String.valueOf(wrCal);
		    		  
		    		  return   wr ;
		    	  }
		    	  else
		    		  return "";
		      }
		    };
		    strDate =  listDates.get(1);
		    tableHistorizedStatsTanksCommAcc.addColumn(jour2Tank3Wr, "Wr Tank" );
	
		    jour2Tank3Wr.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(jour2Tank3Wr,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }
	
		            // Compare the name columns.
		            if (o1 != null && o1.getListVehPlayedDay1().size()> 2 && o2.getListVehPlayedDay1().size()> 2) {
		            	/////////////
		            	int diff1 = o1.getListVehPlayedDay1().get(2).getCountBattleSincePreviousDay();
			    		int diffWins1 = o1.getListVehPlayedDay1().get(2).getWinCountBattleSincePreviousDay();
			    		Double wrCal1 = (double) ((double)diffWins1/(double)diff1);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal1 = wrCal1 * 100; //ex : 51,844444
			    		int intWrCal1 = (int) (wrCal1 * 100); //ex : 5184
			    		//wrCal1 = (double)intWrCal1 / 100 ; //ex : 51,84
			    		//String wr1 = String.valueOf(wrCal1);
		            	
			    		int diff2 = o2.getListVehPlayedDay1().get(2).getCountBattleSincePreviousDay();
			    		int diffWins2 = o2.getListVehPlayedDay1().get(2).getWinCountBattleSincePreviousDay();
			    		Double wrCal2 = (double) ((double)diffWins2/(double)diff2);
			    		//on ne conserve que 2 digits après la virgule 
			    		wrCal2 = wrCal2 * 100; //ex : 51,844444
			    		int intWrCal2 = (int) (wrCal2 * 100); //ex : 5184
			    		//wrCal2 = (double)intWrCal2 / 100 ; //ex : 51,84
			    		//String wr2 = String.valueOf(wrCal2);
		            	//
		            	Integer val1 = intWrCal1;
		            	Integer val2 = intWrCal2;
		              return (o2 != null) ? val1.compareTo(val2) : 1;
		            }else
		            	return -1;
		          }
		        });
	    	//

	    
		    //////////////////////////////////////////////////////////////////
		    // Add a selection model to handle user selection.
		    final SingleSelectionModel<CommunityAccount> selectionModel = new SingleSelectionModel<CommunityAccount>();
		    tableHistorizedStatsTanksCommAcc.setSelectionModel(selectionModel);
		    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event) {
		    	  CommunityAccount selected = selectionModel.getSelectedObject();
		        if (selected != null) {
		          //Window.alert("You selected: " + selected.getName());
		        }
		      }
		    });
	
		    // Set the total row count. This isn't strictly necessary, but it affects
		    // paging calculations, so its good habit to keep the row count up to date.
		    tableHistorizedStatsTanksCommAcc.setRowCount(listCommAcc.size(), true); //no need to do here because we have add list to data provider
	
		    // Push the data into the widget.
		    tableHistorizedStatsTanksCommAcc.setRowData(0, listCommAcc);            //idem no nedd dataprovider
		    
		 // Connect the table to the data provider.
		    dataHistorizedStatsTanksProvider.addDataDisplay(tableHistorizedStatsTanksCommAcc);
		    dataHistorizedStatsTanksProvider.refresh();
	   }


			/**
			 * build a hashMap of achievement from wiki
			 * @param xmlWiki
			 * @return
			 */
			public static HashMap<String, XmlListAchievement> buidHashMapAchievement (XmlWiki xmlWiki) {
				HashMap<String, XmlListAchievement> hashMapAchievement = new HashMap<String, XmlListAchievement>();
				
				
				//parcours de toutes les cat�gories de m�dailles
				for(XmlListCategoryAchievement listCatAch	:	xmlWiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT() ) {
					for (XmlListAchievement ach : listCatAch.getACHIEVEMENT()) {
						String nameCategory = listCatAch.getNAME();
						for (XmlSrc src : ach.getSRCIMG().getSRC()) {
							String srcValue = src.getVALUE();
							int posLastSlash  = srcValue.lastIndexOf("/");
							String nameFile = srcValue.substring(posLastSlash+1);
							hashMapAchievement.put(nameFile, ach);
						}
						
					}
				}
				
				return hashMapAchievement;
				
			}
			
			
			/**
			 * build a hashMap of achievement in a category  from wiki (if category is null return all) key : path of image -- value : the object achievement  
			 * @param xmlWiki
			 * @return
			 */
			public static HashMap<String, XmlListAchievement> buidHashMapAchievement (XmlWiki xmlWiki, String nameCategoryToFilter) {
				HashMap<String, XmlListAchievement> hashMapAchievement = new HashMap<String, XmlListAchievement>();
				
				
				//parcours de toutes les cat�gories de m�dailles
				for(XmlListCategoryAchievement listCatAch	:	xmlWiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT() ) {
					for (XmlListAchievement ach : listCatAch.getACHIEVEMENT()) {
						String nameCategory = listCatAch.getNAME();
						if (nameCategoryToFilter != null && nameCategory != null && nameCategory.equalsIgnoreCase(nameCategoryToFilter)) {
							for (XmlSrc src : ach.getSRCIMG().getSRC()) {
								String srcValue = src.getVALUE();
								int posLastSlash  = srcValue.lastIndexOf("/");
								String nameFile = srcValue.substring(posLastSlash+1);
								hashMapAchievement.put(nameFile, ach);
							}
						}else {
							if (nameCategoryToFilter.equalsIgnoreCase("All Achievements" )) {
								for (XmlSrc src : ach.getSRCIMG().getSRC()) {
									String srcValue = src.getVALUE();
									int posLastSlash  = srcValue.lastIndexOf("/");
									String nameFile = srcValue.substring(posLastSlash+1);
									hashMapAchievement.put(nameFile, ach);
								}
							}
						}
						
					}
				}
				
				return hashMapAchievement;
				
			}

			/**
			 * build a hashMap of category / and list achievement from wiki  key : category  -- value : list of achievement   
			 * @param xmlWiki
			 * @return
			 */
			public static HashMap<String, List<XmlListAchievement>> buidHashMapCategoryAchievement (XmlWiki xmlWiki) {
				HashMap<String, List<XmlListAchievement>> hashMapAchievement = new HashMap<String,  List<XmlListAchievement>>();
				
				
				//parcours de toutes les cat�gories de m�dailles
				for(XmlListCategoryAchievement listCatAch	:	xmlWiki.getACHIEVEMENTS().getCATEGORYACHIEVEMENT() ) {
					List<XmlListAchievement> listAchievement = new ArrayList<XmlListAchievement>();
					String nameCategory = listCatAch.getNAME();
					
					//Parcours des achievements 
					for (XmlListAchievement ach : listCatAch.getACHIEVEMENT()) {
						//String nameCategory = listCatAch.getNAME();
						
						//ajout de l'achievement à la liste
						if ( nameCategory != null ) {
							listAchievement.add(ach);
						}
					}
					hashMapAchievement.put(nameCategory, listAchievement);
				}
				
				return hashMapAchievement;
				
			}

			
			///////
			
//			static public SafeHtmlBuilder buildHtml(HashMap<String, XmlListAchievement> hashMapAch, String nameAch, CommunityAccount object) {
//				//String nameAch = "Beasthunter";
//				XmlListAchievement ach = hashMapAch.get(nameAch+".png");
//				String urlImgSrc2 =  ach.getSRCIMG().getSRC().get(0).getVALUE();
//				SafeHtmlBuilder sb = new SafeHtmlBuilder();
//				
//				String nb= String.valueOf(object.getData().getAchievements().getBeasthunter());
//				sb.appendEscaped(
//						"<div id=\"achievement\" >" + " <div class=\"floatleft\"> " +
//						" <img alt=\"" + nameAch+ ".png\" src=\"" + urlImgSrc2 + "\" width=\"67\" height=\"71\" />" + nb + "</div>");
//				
//				return sb;
//			}
			
			static public String buildHtmlHeader(HashMap<String, XmlListAchievement> hashMapAch, String nameAch) {
				//String nameAch = "Beasthunter";
				XmlListAchievement ach = hashMapAch.get(nameAch+".png");
			    String title2 = ach.getDESCRIPTION().getVALUE();
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String html = "<div id=\"achievement\" >" + " <div class=\"floatleft\"> " +	  title2 + "</div>";
				return html;
			}
			
			static public String buildImgAch(HashMap<String, XmlListAchievement> hashMapAch, String nameAch, CommunityAccount object, int val) {
				//String nameAch = "Beasthunter";
				XmlListAchievement ach = hashMapAch.get(nameAch+".png");
				String urlImgSrc2 = "";
				if (ach.getSRCIMG().getSRC().size() == 1 ) {
					urlImgSrc2 =  ach.getSRCIMG().getSRC().get(0).getVALUE();
				} else {
					//urlImgSrc2
					for (XmlSrc xmlSrc : ach.getSRCIMG().getSRC()) {
						if (xmlSrc.getVALUE().contains(nameAch+".png")) {
							urlImgSrc2 = xmlSrc.getVALUE();
							break ;
						}
					}
				}
				return urlImgSrc2;
			}
			
			static public String getNameAch(HashMap<String, XmlListAchievement> hashMapAch, String nameAch) {
				//String nameAch = "Beasthunter";
				XmlListAchievement ach = hashMapAch.get(nameAch+".png");
				if(ach != null)
					return ach.getNAME();
				else 
					return null;
			}
			
			static void buildPopup(String nameAch, final HashMap<String, XmlListAchievement> hashMapAch) {
		    	//String nameAch = "Defender";
				String html = buildHtmlHeader(hashMapAch, nameAch);		    	    
				// Create the popup dialog box in case of error
				final DialogBox dialogBox = new DialogBox();
				dialogBox.setText("Achievement Description");
				dialogBox.setAnimationEnabled(true);
				Button closeButton = new Button("Close");
				// We can set the id of a widget by accessing its Element
				closeButton.getElement().setId("closeButtonAch");
				VerticalPanel dialogVPanel = new VerticalPanel();
				
				dialogVPanel.addStyleName("dialogVPanel");
				dialogVPanel.add(new HTML(html));
				dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
				dialogVPanel.add(closeButton);
				dialogBox.setWidget(dialogVPanel);
				//dialogBox.showRelativeTo(tableStatsCommAcc);
				dialogBox.center();
				
				closeButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						dialogBox.hide();
//							searchClanButton.setEnabled(true);
//							searchClanButton.setFocus(true);
					}
				});
			
			}


	
			
			
			
			
	
}
