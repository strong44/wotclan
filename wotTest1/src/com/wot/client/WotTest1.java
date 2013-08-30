package com.wot.client;


import java.util.Comparator;
import java.util.List;




import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;
import com.wot.shared.FieldVerifier;
import com.wot.shared.ItemsDataClan;





/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WotTest1 implements EntryPoint {
	String idClan ="" ;
	int offsetClan = 0;
	int limitClan = 100;
	
	  RootPanel rootPanel ;
	  DockPanel dockPanel;
	  
	  //m�canisme de pagination
	  SimplePager pagerStatsCommunityAccount;
	  SimplePager pagerClan;
	  SimplePager pagerAchievementsCommunityAccount;
	  
	  //tableau des stats joueurs
	  CellTable<CommunityAccount> tableStatsCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);
	  
	  //tableau des stats joueurs
	  CellTable<CommunityAccount> tableAchivementCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);

	  //tableau des clans
	  CellTable<ItemsDataClan> tableClan = new  CellTable<ItemsDataClan> (ItemsDataClan.KEY_PROVIDER);
	  
	  // Create a data provider for tab players.
	  ListDataProvider<CommunityAccount> dataStatsProvider = new ListDataProvider<CommunityAccount>(CommunityAccount.KEY_PROVIDER);
	  
	  // Create a data provider for achievement players.
	  ListDataProvider<CommunityAccount> dataAchievementsProvider = new ListDataProvider<CommunityAccount>(CommunityAccount.KEY_PROVIDER);
	  
	  //create data provider for tab clans
	  ListDataProvider<ItemsDataClan> dataClanProvider = new ListDataProvider<ItemsDataClan>(ItemsDataClan.KEY_PROVIDER);
	     
 

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
	        return object.getName();
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
//	        return String.valueOf(object.getData().getStats().getBattle_avg_performance());
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
//	            	int val1 = o1.getData().getStats().getBattle_avg_performance();
//	            	int val2 = o2.getData().getStats().getBattle_avg_performance();
//	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//	            }
//	            return -1;
//	          }
//	        });

	    
	    
	    // Add a text column to show the win rate.
	    TextColumn<CommunityAccount> wrCalcColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_avg_performanceCalc());
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
	            	Double val1 = o1.getData().getStats().getBattle_avg_performanceCalc();
	            	Double val2 = o2.getData().getStats().getBattle_avg_performanceCalc();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    

	    
	    
	    // Add a text column battleWinsColumn
	    TextColumn<CommunityAccount> battleWinsColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_wins() );
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
	            	int val1 = o1.getData().getStats().getBattle_wins();
	            	int val2 = o2.getData().getStats().getBattle_wins();
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });
	    
	    // Add a text column to show battlesColumn
	    TextColumn<CommunityAccount> battlesColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattles() );
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
	            	int val1 = o1.getData().getStats().getBattles();
	            	int val2 = o2.getData().getStats().getBattles();
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });
	    
		    
//	    // Add a text column to show ctfColumn
//	    TextColumn<CommunityAccount> ctfColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getStats().getCtf_points() );
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
//	            	int val1 = o1.getData().getStats().getCtf_points();
//	            	int val2 = o2.getData().getStats().getCtf_points();
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
	        return String.valueOf(object.getData().getStats().getRatioCtfPoints());
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
	            	Double val1 = o1.getData().getStats().getRatioCtfPoints();
	            	Double val2 = o2.getData().getStats().getRatioCtfPoints();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    
	    
//	    // Add a text column to show dmgColumn
//	    TextColumn<CommunityAccount> dmgColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getStats().getDamage_dealt() );
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
//	            	int val1 = o1.getData().getStats().getDamage_dealt();
//	            	int val2 = o2.getData().getStats().getDamage_dealt();
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
	        return String.valueOf(object.getData().getStats().getRatioDamagePoints());
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
	            	Double val1 = o1.getData().getStats().getRatioDamagePoints();
	            	Double val2 = o2.getData().getStats().getRatioDamagePoints();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    // Add a text column to show dropCtfColumn
//	    TextColumn<CommunityAccount> dropCtfColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getStats().getDropped_ctf_points() );
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
//	            	int val1 = o1.getData().getStats().getDropped_ctf_points();
//	            	int val2 = o2.getData().getStats().getDropped_ctf_points();
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
	        return String.valueOf(object.getData().getStats().getRatioDroppedCtfPoints());
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
	            	Double val1 = o1.getData().getStats().getRatioDroppedCtfPoints();
	            	Double val2 = o2.getData().getStats().getRatioDroppedCtfPoints();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
//	    // Add a text column to show fragsColumn
//	    TextColumn<CommunityAccount> fragsColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getStats().getFrags() );
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
//	            	int val1 = o1.getData().getStats().getFrags();
//	            	int val2 = o2.getData().getStats().getFrags();
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
	        return String.valueOf(object.getData().getStats().getRatioDestroyedPoints());
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
	            	Double val1 = o1.getData().getStats().getRatioDestroyedPoints();
	            	Double val2 = o2.getData().getStats().getRatioDestroyedPoints();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });


	    
	    
//	    // Add a text column to show irColumn
//	    TextColumn<CommunityAccount> irColumn = new TextColumn<CommunityAccount>() {
//	      @Override
//	      public String getValue(CommunityAccount object) {
//	        return String.valueOf(object.getData().getStats().getIntegrated_rating() );
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
//	            	int val1 = o1.getData().getStats().getIntegrated_rating();
//	            	int val2 = o2.getData().getStats().getIntegrated_rating();
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
//	        return String.valueOf(object.getData().getStats().getSpotted() );
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
//	            	int val1 = o1.getData().getStats().getSpotted();
//	            	int val2 = o2.getData().getStats().getSpotted();
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
	        return String.valueOf(object.getData().getStats().getRatioDetectedPoints());
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
	            	Double val1 = o1.getData().getStats().getRatioDetectedPoints();
	            	Double val2 = o2.getData().getStats().getRatioDetectedPoints();
	              return (o2 != null) ? val1.compareTo(val2) : 1;
	            }
	            return -1;
	          }
	        });

	    
	    // Add a text column to show xpColumn
	    TextColumn<CommunityAccount> xpColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getXp() );
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
	            	int val1 = o1.getData().getStats().getXp();
	            	int val2 = o2.getData().getStats().getXp();
	              return (o2 != null) ? Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
	            }
	            return -1;
	          }
	        });
		    
	    // Add a text column to show avgXpColumn
	    TextColumn<CommunityAccount> avgXpColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_avg_xp() );
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
	            	int val1 = o1.getData().getStats().getBattle_avg_xp();
	            	int val2 = o2.getData().getStats().getBattle_avg_xp();
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
		public  void buildACellTableAchivementsForCommunityAccount(List<CommunityAccount> listCommAcc) {
		    
			tableAchivementCommAcc.setPageSize(30);
			
		    //update dataprovider with some known list 
			dataAchievementsProvider.setList(listCommAcc);
			
			// Create a CellTable.
		    tableAchivementCommAcc.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		    
		    
		    ListHandler<CommunityAccount> columnSortHandler =
			        new ListHandler<CommunityAccount>(dataStatsProvider.getList());
		    tableAchivementCommAcc.addColumnSortHandler(columnSortHandler);
		    
		    
		    // Add a text column to show the name.
		    TextColumn<CommunityAccount> nameColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return object.getName();
		      }
		    };
		    tableAchivementCommAcc.addColumn(nameColumn, "Name");
	
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
		    tableAchivementCommAcc.getColumnSortList().push(nameColumn);
	    
		    //add column Hunter
		    Column<CommunityAccount, String > hunterColumn = new Column<CommunityAccount, String>(new ImageCell()) {
		      @Override
		      public String getValue(CommunityAccount object) {
		    	  String url = "http://wiki.worldoftanks.com/images/4/44/Beasthunter.png";
		        return String.valueOf(url );
		      }
		    };
		    tableAchivementCommAcc.addColumn(hunterColumn, "Title Hunter");
	
		    hunterColumn.setSortable(false);
	
		    //-- Add column number Hunter
		    TextColumn<CommunityAccount> nbHunterColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getBeasthunter());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbHunterColumn, "Nb");

		    nbHunterColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbHunterColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	String val1 = String.valueOf(o1.getData().getAchievements().getBeasthunter());
		            	String val2 = String.valueOf(o2.getData().getAchievements().getBeasthunter());
		            	return (o2 != null) ? val1.compareTo(val2) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    //SafeHtml 
		    //add column Hunter
		    Column<CommunityAccount, SafeHtml > hunter2Column = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					String urlImgSrc = "http://wiki.worldoftanks.com/images/4/44/Beasthunter.png";
					String urlTarget = "http://wiki.worldoftanks.com/Images";
					String title ="Hunter";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return null;
				}
				
			};
			
		    tableAchivementCommAcc.addColumn(hunter2Column, "Title Hunter");
	
		    hunter2Column.setSortable(false);
		    
		    
		    
		    // Add a selection model to handle user selection.
		    final SingleSelectionModel<CommunityAccount> selectionModel = new SingleSelectionModel<CommunityAccount>();
		    tableAchivementCommAcc.setSelectionModel(selectionModel);
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
		    
		    tableAchivementCommAcc.setRowCount(listCommAcc.size(), true); //no need to do here because we have add list to data provider
	
		    // Push the data into the widget.
		    tableAchivementCommAcc.setRowData(0, listCommAcc);            //idem no nedd dataprovider
		    
		 // Connect the table to the data provider.
		    dataAchievementsProvider.addDataDisplay(tableAchivementCommAcc);
		    dataAchievementsProvider.refresh();
	   }


	/*
	 * call this when we have data to put in table
	 */
	public  void buildACellTableForCommunityClan(Clan listClan) {
			    
	    //update dataprovider with some known list 
	    dataClanProvider.setList(listClan.getData().getItems());
		
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
	    
	    tableClan.setRowCount(listClan.getData().getItems().size(), true); //no need to do here because we have add list to data provider

	    // Push the data into the widget.
	    tableClan.setRowData(0, listClan.getData().getItems());            //idem no nedd dataprovider
	    
	 // Connect the table to the data provider.
	    dataClanProvider.addDataDisplay(tableClan);
	    dataClanProvider.refresh();
		    
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
			rootPanel.add(dockPanel, 29, 125);
			dockPanel.setSize("1193px", "550px");
			
			//nouveau tableau des membres
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
									
			//label clan
			Label lblNewLabel = new Label("Clan");
			lblNewLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			rootPanel.add(lblNewLabel, 26, 16);
			lblNewLabel.setSize("52px", "24px");
			
			//button search member's clan
			final Button findMembersClanButton = new Button("Send");
			findMembersClanButton.setText("Find Clan's members");
			rootPanel.add(findMembersClanButton, 29, 50);
			findMembersClanButton.setSize("214px", "28px");
			findMembersClanButton.setEnabled(false);
			
			//button search Clans
			Button searchClansButton = new Button("Find Clans");
			rootPanel.add(searchClansButton, 374, 12);
			searchClansButton.setSize("146px", "28px");
			
			//bouton plus de clans
			final Button searchClansButtonMore = new Button("Find 100 Clans More");
			rootPanel.add(searchClansButtonMore, 574, 12);
			searchClansButtonMore.setSize("146px", "28px");
			searchClansButtonMore.setEnabled(false);
	
			
			//button achievement's member
			final Button findAchievementsMemberButton = new Button("Send");
			findAchievementsMemberButton.setText("Find Achivement's member");
			rootPanel.add(findAchievementsMemberButton, 250, 50);
			findAchievementsMemberButton.setSize("250px", "28px");
			findAchievementsMemberButton.setEnabled(false);
			
			
			
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

										findMembersClanButton.setEnabled(true);
										findAchievementsMemberButton.setEnabled(true);
										
										//on autorise le bouton  more clans s'il y a en core 100 �lments dans TAB
										if(listClan.getData().getItems().size()== 100)
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
	
			////
			///////////
			// Create a handler for search achivement's member
			class HandlerGetAchivementsMember implements ClickHandler, KeyUpHandler {
				/**
				 * Fired when the user clicks on the sendButton.
				 */
				public void onClick(ClickEvent event) {
					getAchievementMember();
//					offsetClan = 0;
//					limitClan = 100;
				}
	
				/**
				 * Fired when the user types in the nameField.
				 */
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						getAchievementMember();
//						offsetClan = 0;
//						limitClan = 100;
					}
				}
	
				/**
				 * 
				 */
				private void getAchievementMember() {
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
						return;
					}
	
					// Then, we send the input to the server.
					//searchClanButton.setEnabled(false);
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
									
									dockPanel.remove(tableAchivementCommAcc);
									dockPanel.remove(tableStatsCommAcc);
									dockPanel. remove(tableClan);
									
									if (pagerAchievementsCommunityAccount != null) 
										dockPanel.remove(pagerAchievementsCommunityAccount);
									if (pagerStatsCommunityAccount != null) 
										dockPanel.remove(pagerStatsCommunityAccount);
									if (pagerClan != null) 
										dockPanel.remove(pagerClan);
									
									
									if (dataAchievementsProvider.getDataDisplays()!= null && !dataAchievementsProvider.getDataDisplays().isEmpty()) 
										dataAchievementsProvider.removeDataDisplay(tableAchivementCommAcc);
									
									//on re-construit 1 nouveau tableau
									tableAchivementCommAcc = new  CellTable<CommunityAccount> (CommunityAccount.KEY_PROVIDER);
									
									//construct column in celltable tableCommAcc , set data set sort handler etc ..
									buildACellTableAchivementsForCommunityAccount(listAccount.getListCommunityAccount());
									  
									//Create a Pager to control the table.
								    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
								    pagerAchievementsCommunityAccount = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
								    pagerAchievementsCommunityAccount.setDisplay(tableAchivementCommAcc);
									
							    
								    //add to dock panel ======
								    //add tab achievement a the end 
								    dockPanel.add(pagerAchievementsCommunityAccount, DockPanel.SOUTH);
								    pagerAchievementsCommunityAccount.setPage(10);
								    pagerAchievementsCommunityAccount.setVisible(true);
									
									dockPanel.add(tableAchivementCommAcc, DockPanel.SOUTH);
									tableAchivementCommAcc.setVisible(true);
								    
								    //add tab stats 
								    dockPanel.add(pagerStatsCommunityAccount, DockPanel.SOUTH);
									pagerStatsCommunityAccount.setPage(10);
									pagerStatsCommunityAccount.setVisible(true);
									
									dockPanel.add(tableStatsCommAcc, DockPanel.SOUTH);
									tableStatsCommAcc.setVisible(true);
								    
									//add tab clan at the begin
									dockPanel.add(pagerClan, DockPanel.SOUTH);
									dockPanel.add(tableClan, DockPanel.SOUTH);
									tableClan.setVisible(true);
									pagerClan.setVisible(true);
									
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
									
									//dialogBox.center();
									//closeButton.setFocus(true);
								}
							});
					//searchClanButton.setEnabled(true);
					//searchClanButton.setFocus(true);
				}
				
				
				
			}
		////
			// Add a handler to send the name to the server
			HandlerGetMembersClan handlerFindMembers = new HandlerGetMembersClan();
			findMembersClanButton.addClickHandler(handlerFindMembers);
					
			// Add a handler to find more clans
			HandlerGetClans handlerGetClans = new HandlerGetClans();
			searchClansButton.addClickHandler(handlerGetClans);
			searchClansButtonMore.addClickHandler(handlerGetClans);
			
			nameClan.addKeyUpHandler(handlerGetClans);
	
			
			// button HandlerGetAchivementsMember
			HandlerGetAchivementsMember myHandlerGetAchivementsMember = new HandlerGetAchivementsMember();
			findAchievementsMemberButton.addClickHandler(myHandlerGetAchivementsMember);
			
		}
}
