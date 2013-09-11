package com.wot.client;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;










import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
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
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.wot.server.BuildWikiFile;
import com.wot.shared.AllCommunityAccount;
import com.wot.shared.Clan;
import com.wot.shared.CommunityAccount;
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
	static String noData = "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQ8KRYghA2Xyp8gWTkK4ZNtBQL2nixsiYdAFDeFBCaj_ylXcfhK";
	XmlWiki xmlWiki = null;
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
		public  void buildACellTableForAchivementsCommunityAccount(List<CommunityAccount> listCommAcc, XmlWiki xmlWiki) {
		    
			final HashMap<String, XmlListAchievement> hashMapAch = buidHashMapAchievement(xmlWiki);
			
			tableAchivementCommAcc.setPageSize(30);
			
		    //update dataprovider with some known list 
			dataAchievementsProvider.setList(listCommAcc);
			
			// Create a CellTable.
		    tableAchivementCommAcc.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		    
		    
		    ListHandler<CommunityAccount> columnSortHandler =
			        new ListHandler<CommunityAccount>(dataAchievementsProvider.getList());
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
	    
	
		    Column<CommunityAccount, String> hunter2Column = new Column<CommunityAccount, String>(new  ClickableTextCell()){
		        @Override
		        public String getValue(CommunityAccount obj){
		            return "ououououou";
		        }

		    };
		    hunter2Column.setFieldUpdater(new FieldUpdater<CommunityAccount, String>(){
		        @Override
		        public void update(int index, CommunityAccount obj, String value){
		        	 ///////////////////
			    	String nameAch = "Beasthunter";
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
					dialogBox.showRelativeTo(tableStatsCommAcc);
					dialogBox.show();
				
					
					// Add a handler to close the DialogBox
					closeButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							dialogBox.hide();
//							searchClanButton.setEnabled(true);
//							searchClanButton.setFocus(true);
						}
					});
		        }
		    });
		     
		    //add column Hunter
//		    Column<CommunityAccount, SafeHtml > hunter2Column = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//		    	
//		    	
//				@Override
//				public SafeHtml getValue(CommunityAccount object) {
//					String nameAch = "Beasthunter";
//					SafeHtmlBuilder sb = new SafeHtmlBuilder();
//					String html = buildHtml(hashMapAch, nameAch, object); //img src + nb --> il faut remettre la description en clickant sur img src 
//					sb.appendHtmlConstant(html);
//					return sb.toSafeHtml();
//				}
//				
//			};
			
			//pour le HEADER de la colonne Hunter
			Header<String> header = new Header<String>(new ClickableTextCell())
			{
		      @Override
		      public String getValue()
		      {
		            return "Hunter";
		      }
//		      @Override
//		    	public void onBrowserEvent(Context context, Element elem, NativeEvent event) {
//		    		super.onBrowserEvent(context, elem, event);
//		    	}
			};
			
			header.setUpdater(new ValueUpdater<String>() {
			    @Override
			    public void update(String value) {
	    	    ///////////////////
			    	String nameAch = "Beasthunter";
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
					dialogBox.showRelativeTo(tableStatsCommAcc);
					dialogBox.show();
				
					
					// Add a handler to close the DialogBox
					closeButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							dialogBox.hide();
//							searchClanButton.setEnabled(true);
//							searchClanButton.setFocus(true);
						}
					});
		    	    
		    	    
		    	    //tableAchivementCommAcc.add(popup);  
			    }
			});
			
			
			//SafeHtmlBuilder sbHeadercolumn = new SafeHtmlBuilder();
			//sbHeadercolumn.appendHtmlConstant(html);
			//Header header = new Header
		    tableAchivementCommAcc.addColumn(hunter2Column, header);
		    //tableAchivementCommAcc.addColumn(hunter2Column, sbHeadercolumn.toSafeHtml());
		    hunter2Column.setSortable(false);
		    
		    
//		    //-- Add column number Hunter
//		    TextColumn<CommunityAccount> nbHunterColumn = new TextColumn<CommunityAccount>() {
//		      @Override
//		      public String getValue(CommunityAccount object) {
//		        return String.valueOf(object.getData().getAchievements().getBeasthunter());
//		      }
//		    };
//		    tableAchivementCommAcc.addColumn(nbHunterColumn, "Nb");
//		    nbHunterColumn.setSortable(true);
//		    
//		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//		    columnSortHandler.setComparator(nbHunterColumn,
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
//		    
//		    
		    
		    //add column defender
		    Column<CommunityAccount, SafeHtml > defenderColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
//					SafeHtmlBuilder sb = new SafeHtmlBuilder();
//					String urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/Defender.png";
//					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//					String title ="Defender - Reduce the amount of enemy capture points on a friendly base by 70 or more. If two or more players have reduced equal amount of capture points, the achievement is granted to the player who has earned more XP in the battle (including additional XP provided to Premium account users). If the amount of XP is equal as well, the achievement is not awarded.";
//					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					String nameAch = "Defender";
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					String html = buildHtml(hashMapAch, nameAch, object);
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
					
					
				}
				
			};
			
			String nameAch = "Defender";
			String html2 = buildHtmlHeader(hashMapAch, nameAch);
			SafeHtmlBuilder sbHeadercolumn = new SafeHtmlBuilder();
			sbHeadercolumn.appendHtmlConstant(html2);

		    tableAchivementCommAcc.addColumn(defenderColumn, "Defender");
		    defenderColumn.setSortable(false);
		    
		    
		    //-- Add column number Hunter
		    TextColumn<CommunityAccount> nbDefenderColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getDefender());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbDefenderColumn, "Nb");
		    nbDefenderColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbDefenderColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getDefender();
		            	int val2 = o2.getData().getAchievements().getDefender();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    
		    //add column diehard
		    Column<CommunityAccount, SafeHtml > diehardColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					String urlImgSrc = "http://wiki.worldoftanks.com/images/7/7e/Diehard.png";
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Survive 20 or more consecutive battles. Battles fought using self-propelled guns do not break the sequence, but are not included either.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(diehardColumn, "Survivor");
		    diehardColumn.setSortable(false);
		    
		    
		    //-- Add column number diehard
		    TextColumn<CommunityAccount> nbDiehardColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getDiehard());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbDiehardColumn, "Nb");
		    nbDiehardColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbDiehardColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getDiehard();
		            	int val2 = o2.getData().getAchievements().getDiehard();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    //add column Invader
		    Column<CommunityAccount, SafeHtml > invaderColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					String urlImgSrc = "http://wiki.worldoftanks.com/images/7/77/Invader.png";
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Invader - Capture the maximum number of points from the enemy base, but not less than 80. The achievement is granted on successful base capture, including only the points that were part of the base capture. If the battle ends in a draw, the achievement is granted to the first player to receive 80 or more capture points.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(invaderColumn, "Invader");
		    invaderColumn.setSortable(false);
		    
		    
		    //-- Add column number Invader
		    TextColumn<CommunityAccount> nbInvaderColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getInvader());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbInvaderColumn, "Nb");
		    nbInvaderColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbInvaderColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getInvader();
		            	int val2 = o2.getData().getAchievements().getInvader();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    //add column MedalAbrams
		    Column<CommunityAccount, SafeHtml > MedalAbramsColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalAbrams();
					String urlImgSrc = noData;
					
					switch (val) {
						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png"; break ;
						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png"; break ;
						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png"; break ;
						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png"; break ;
						
					};
					// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
					// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
					// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
					// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
					
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Abrams' Medal - Awarded in one of four classes for the total number of team victories in which the player survived the battle: \nClass IV - 5 victories \nClass III - 50 victories \nClass II - 500 victories \nClass I - 5,000 victories.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(MedalAbramsColumn, "Medal Abrams");
		    MedalAbramsColumn.setSortable(false);
		    
		    
		    //-- Add column number MedalAbrams
		    TextColumn<CommunityAccount> nbMedalAbrams = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalAbrams());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbMedalAbrams, "Class");
		    nbMedalAbrams.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbMedalAbrams,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalAbrams();
		            	int val2 = o2.getData().getAchievements().getMedalAbrams();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    //===add column getMedalBillotte
		    Column<CommunityAccount, SafeHtml > medalBillotteColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalBillotte();
					String urlImgSrc = "http://wiki.worldoftanks.com/images/b/b0/MedalBillotte.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png"; break ;
//						
//					};
					// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
					// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
					// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
					// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
					
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Billotte's Medal - Awarded to players who destroy at least one enemy vehicle and survive the battle to victory despite receiving at least five different critical hits and 80% or more loss of hit points. ";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(medalBillotteColumn, "Medal Billotte");
		    medalBillotteColumn.setSortable(false);
		    
		    
		    //====  Add column number medalBillotte
		    TextColumn<CommunityAccount> nbMedalBillotteColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalBillotte());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbMedalBillotteColumn, "Nb");
		    nbMedalBillotteColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbMedalBillotteColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalBillotte();
		            	int val2 = o2.getData().getAchievements().getMedalBillotte();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    //===add column MedalBurda
		    Column<CommunityAccount, SafeHtml > MedalBurdaColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalBurda();
					String urlImgSrc = "http://wiki.worldoftanks.com/images/d/d1/MedalBurda.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png"; break ;
//						
//					};
					// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
					// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
					// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
					// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
					
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Burda's Medal - Awarded for the destruction of five or more enemy self-propelled guns. \nGuards Colonel Alexander Burda was a Soviet tank ace and a Hero of the Soviet Union. On October 4, 1941, Burda organized an ambush and destroyed an enemy armored column, including 10 medium and light tanks, 2 trucks with antitank guns and 5 infantry vehicles. Not awarded to SPG drivers.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(MedalBurdaColumn, "Medal Burda");
		    
		    
		    //====  Add column number MedalBurda
		    TextColumn<CommunityAccount> nbMedalBurdaColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalBurda());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbMedalBurdaColumn, "Nb");
		    nbMedalBurdaColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbMedalBurdaColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalBurda();
		            	int val2 = o2.getData().getAchievements().getMedalBurda();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    //===add column MedalCarius
		    Column<CommunityAccount, SafeHtml > MedalCariusColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalCarius();
					String urlImgSrc = noData;
					
					switch (val) {
						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/c/ce/MedalCarius1.png"; break ;
						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/7/7d/MedalCarius2.png"; break ;
						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/98/MedalCarius3.png"; break ;
						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/7/71/MedalCarius4.png"; break ;
						
					};
					// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
					// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
					// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
					// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
					
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Carius' Medal - Awarded for the destruction of enemy tanks and self-propelled guns in four classes:\nClass IV - 10 vehicles \nClass III - 100 vehicles \nClass II - 1,000 vehicles \nClass I - 10,000 vehicles. \nOtto Carius was one of the most efficient tank aces of WWII. He commanded the Pz.Kpfw. 38 (t), the Pz.Kpfw. VI Tiger and the Jagdtiger tank destroyer during his impressive career.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(MedalCariusColumn, "Medal Carius");
		    
		    
		    //====  Add column number MedalCarius
		    TextColumn<CommunityAccount> nbMedalCariusColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalCarius());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbMedalCariusColumn, "Class");
		    nbMedalCariusColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbMedalCariusColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalCarius();
		            	int val2 = o2.getData().getAchievements().getMedalCarius();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    //===add column MedalCarius
		    Column<CommunityAccount, SafeHtml > MedalMedalEkinsColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalEkins();
					String urlImgSrc = noData;
					
					switch (val) {
						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/87/MedalEkins1.png"; break ;
						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/9b/MedalEkins2.png"; break ;
						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/13/MedalEkins3.png"; break ;
						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/94/MedalEkins4.png"; break ;
						
					};
					// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
					// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
					// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
					// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
					
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Ekins' Medal - Awarded in four classes for destroying tier 8, 9 or 10 enemy tanks and self-propelled guns: \nClass IV - 3 vehicles \nClass III - 30 vehicles \nClass II - 300 vehicles \nClass I - 3,000 vehicles. \nJoe Ekins was a private in the Northamptonshire Division of the British Territorial Army. A number of sources confirm Ekins as the final nemesis of famous German tank ace Michael Wittmann.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(MedalMedalEkinsColumn, "Medal Ekins");
		    
		    
		    //====  Add column number MedalCarius
		    TextColumn<CommunityAccount> nbMedalEkinsColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalEkins());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbMedalEkinsColumn, "Class");
		    nbMedalEkinsColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbMedalEkinsColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalEkins();
		            	int val2 = o2.getData().getAchievements().getMedalEkins();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    //===add column MedalFadin
		    Column<CommunityAccount, SafeHtml > MedalFadinColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalFadin();
					String urlImgSrc = noData;
					urlImgSrc = "http://wiki.worldoftanks.com/images/e/e7/MedalFadin.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/87/MedalEkins1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/9b/MedalEkins2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/13/MedalEkins3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/94/MedalEkins4.png"; break ;
//						
//					};
					// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
					// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
					// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
					// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
					
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Fadin's Medal - Awarded for destroying the last enemy vehicle in the battle with the last shell remaining in the player's tank. \nA hero of the Soviet Union, Alexander Fadin was a T-34 commander. Supported by one infantry platoon, Fadin managed to capture and hold the Dashukovka village for 5 hours with one tank, and destroyed 3 tanks, 1 halftrack, 2 mortars and 12 machinegun nests. His crew also shot down an enemy plane with his tank's coaxial machinegun.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(MedalFadinColumn, "Medal Fadin");
		    
		    
		    //====  Add column number Medal Fadin
		    TextColumn<CommunityAccount> nbMedalFadinColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalFadin());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbMedalFadinColumn, "Nb");
		    nbMedalFadinColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbMedalFadinColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalFadin();
		            	int val2 = o2.getData().getAchievements().getMedalFadin();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    
		    //===add column MedalHalonen
		    Column<CommunityAccount, SafeHtml > MedalHalonenColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalHalonen();
					String urlImgSrc = noData;
					urlImgSrc = "http://wiki.worldoftanks.com/images/3/3a/MedalHalonen.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/87/MedalEkins1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/9b/MedalEkins2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/13/MedalEkins3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/94/MedalEkins4.png"; break ;
//						
//					};
					// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
					// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
					// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
					// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
					
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Halonen's Medal - Awarded for destroying three or more enemy vehicles with a tank destroyer. \nThe targets must be at least two tiers higher than the player's vehicle.\nErkki Halonen, a sergeant in the Finnish Army and a tank ace, destroyed three T-34, two KV-1, and two ISU-152 with his StuG III in battles during June and July, 1944.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(MedalHalonenColumn, "Medal Halonen ");
		    
		    
		    //====  Add column number Medal halonen
		    TextColumn<CommunityAccount> nbMedalHalonenColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalHalonen());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbMedalHalonenColumn, "Nb");
		    nbMedalHalonenColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbMedalHalonenColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalHalonen();
		            	int val2 = o2.getData().getAchievements().getMedalHalonen();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    //===add column getMedalKay
		    Column<CommunityAccount, SafeHtml > getMedalKayColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalKay();
					String urlImgSrc = noData;
//					urlImgSrc = "http://wiki.worldoftanks.com/images/3/3a/MedalHalonen.png";
					
					switch (val) {
						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/c/c6/MedalKay1.png"; break ;
						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/e/e8/MedalKay2.png"; break ;
						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/10/MedalKay3.png"; break ;
						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/6/6f/MedalKay4.png"; break ;
						
					};
					// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
					// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
					// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
					// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
					
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Kay's Medal - Awarded for achieving the Battle Hero status in four classes:\nClass IV - 1 time \nClass III - 10 times \nClass II - 100 times \nClass I - 1,000 times.\nDouglas Kay, a British Army sergeant, and gunner on a Sherman Firefly, participated in the Allied landing in Normandy and was famous for the popularization of the history of tank warfare.\nNote: Only the medals listed under 'Battle Hero Achievements' above are counted in the 'Kay�s Medal' achievement.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getMedalKayColumn, "Medal Kay ");
		    
		    
		    //====  Add column number Medal Kay
		    TextColumn<CommunityAccount> nbgetMedalKayColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalKay());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetMedalKayColumn, "Class");
		    nbgetMedalKayColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetMedalKayColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalKay();
		            	int val2 = o2.getData().getAchievements().getMedalKay();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    
		    
		    //===add column Knispel
		    Column<CommunityAccount, SafeHtml > getMedalKnispelColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalKnispel();
					String urlImgSrc = noData;
//					urlImgSrc = "http://wiki.worldoftanks.com/images/3/3a/MedalHalonen.png";
					
					switch (val) {
						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/9c/MedalKnispel1.png"; break ;
						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/d/d6/MedalKnispel2.png"; break ;
						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/c/cc/MedalKnispel3.png"; break ;
						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/91/MedalKnispel4.png"; break ;
						
					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Knispel's Medal - Awarded for the total amount of damage caused and received in four classes: \nClass IV - 10,000 HP \nClass III - 100,000 HP \nClass II - 1,000,000 HP \nClass I - 10,000,000 HP.\nKurt Knispel, a German tank ace during WWII, participated in battles on both the Western and Eastern Fronts fighting on Pz.Kpfw. II, Pz.Kpfw. III, Pz.Kpfw. IV, Pz.Kpfw. VI Tiger, and Pz.Kpfw. Tiger II.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getMedalKnispelColumn, "Medal Knispel ");
		    
		    
		    //====  Add column number Medal Knispel
		    TextColumn<CommunityAccount> nbgetMedalKnispelColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalKnispel());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetMedalKnispelColumn, "Class");
		    nbgetMedalKnispelColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetMedalKnispelColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalKnispel();
		            	int val2 = o2.getData().getAchievements().getMedalKnispel();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    
		    
		    //===add column getMedalKolobanov
		    Column<CommunityAccount, SafeHtml > getMedalKolobanovColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalKolobanov();
					String urlImgSrc = noData;
					urlImgSrc = "http://wiki.worldoftanks.com/images/7/75/MedalKolobanov.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/9c/MedalKnispel1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/d/d6/MedalKnispel2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/c/cc/MedalKnispel3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/91/MedalKnispel4.png"; break ;
//						
//					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Kolobanov's Medal - Awarded to a player who stands alone against five or more enemy tanks or self-propelled guns and wins (this means that you can capture the enemy base by yourself when you are against five enemies and you will recieve the achievement.)\nColonel Zinoviy Kolobanov was a Soviet tank ace who destroyed 22 German tanks, 2 guns and 2 halftracks with his KV in battle on August 19, 1941.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getMedalKolobanovColumn, "Medal Kolobanov ");
		    
		    
		    //====  Add column number Medal getMedalKolobanov
		    TextColumn<CommunityAccount> nbgetMedalKolobanovColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalKolobanov());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetMedalKolobanovColumn, "Nb");
		    nbgetMedalKolobanovColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetMedalKolobanovColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalKolobanov();
		            	int val2 = o2.getData().getAchievements().getMedalKolobanov();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    
		    
		    //===add column getMedalLavrinenko
		    Column<CommunityAccount, SafeHtml >getMedalLavrinenkoColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalLavrinenko();
					String urlImgSrc = noData;
					//urlImgSrc = "http://wiki.worldoftanks.com/images/7/75/MedalKolobanov.png";
					
					switch (val) {
						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/a/a5/MedalLavrinenko1.png"; break ;
						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/e/ee/MedalLavrinenko2.png"; break ;
						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/4/4a/MedalLavrinenko3.png"; break ;
						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/c/c2/MedalLavrinenko4.png"; break ;
						
					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Lavrinenko's Medal - Awarded for reducing the total number of capture points of a friendly base, up to 100 points per battle. This award is established in four classes: \nClass IV - 30 points \nClass III - 300 points\n Class II - 3,000 points \nClass I - 30,000 points.\nDmitry Lavrinenko, a Hero of the Soviet Union, Guards Lieutenant, and tank ace was recognized as the most efficient Soviet tanker, destroying 52 tanks in 28 battles over the course of just two months.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getMedalLavrinenkoColumn, "Medal Lavrinenko ");
		    
		    
		    //====  Add column number Medal getMedalLavrinenko
		    TextColumn<CommunityAccount> nbgetMedalLavrinenkoColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalLavrinenko());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetMedalLavrinenkoColumn, "Class");
		    nbgetMedalLavrinenkoColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetMedalLavrinenkoColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalLavrinenko();
		            	int val2 = o2.getData().getAchievements().getMedalLavrinenko();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		
		    
		    //===ADD column getMedalLeClerc
		    Column<CommunityAccount, SafeHtml > getMedalLeClercColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalLeClerc();
					String urlImgSrc = noData;
					//urlImgSrc = "http://wiki.worldoftanks.com/images/7/75/MedalKolobanov.png";
					
					switch (val) {
						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/83/MedalLeClerc1.png"; break ;
						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/6/62/MedalLeClerc2.png"; break ;
						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/3/3d/MedalLeClerc3.png"; break ;
						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalLeClerc4.png"; break ;
						
					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Leclerc's Medal - Awarded for the total amount of the player's enemy base capture points. An unsuccessful or reduced capture does not count toward this number. The award is established in four classes: Class IV - 30 points Class III - 300 points Class II - 3,000 points Class I - 30,000 points\nPhilippe Leclerc was a General of the Free French Forces during WWII and one of the leaders of the Paris liberation operation.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getMedalLeClercColumn, "Medal Le Clerc ");
		    
		    
		    //====  ADD column number Medal getMedalLeClerc
		    TextColumn<CommunityAccount> nbgetMedalLeClercColumnColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalLeClerc());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetMedalLeClercColumnColumn, "Class");
		    nbgetMedalLeClercColumnColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetMedalLeClercColumnColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalLeClerc();
		            	int val2 = o2.getData().getAchievements().getMedalLeClerc();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		 
		    
		    
		    
		    //===ADD column getMedalOrlik
		    Column<CommunityAccount, SafeHtml > getMedalOrlikColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalOrlik();
					String urlImgSrc = noData;
					urlImgSrc = "http://wiki.worldoftanks.com/images/2/2b/MedalOrlik.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/83/MedalLeClerc1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/6/62/MedalLeClerc2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/3/3d/MedalLeClerc3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalLeClerc4.png"; break ;
//						
//					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Orlik's Medal - Awarded for destroying three or more enemy tanks or tank destroyers with a light tank. The targets must be at least two tiers higher than the player's tank.\nRoman Edmund Orlik, a Polish Army sergeant, was a tank ace who knocked out 13 German tanks with his light TKS tankette in September, 1939.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getMedalOrlikColumn, "Medal Orlik");
		    
		    
		    //====  ADD column number Medal getMedalOrlik
		    TextColumn<CommunityAccount> nbgetMedalOrlikColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalOrlik());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetMedalOrlikColumn, "Nb");
		    nbgetMedalOrlikColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetMedalOrlikColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalOrlik();
		            	int val2 = o2.getData().getAchievements().getMedalOrlik();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    //===ADD column getMedalOskin
		    Column<CommunityAccount, SafeHtml > getMedalOskinColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalOskin();
					String urlImgSrc = noData;
					urlImgSrc = "http://wiki.worldoftanks.com/images/5/5f/MedalOskin.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/83/MedalLeClerc1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/6/62/MedalLeClerc2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/3/3d/MedalLeClerc3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalLeClerc4.png"; break ;
//						
//					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Oskin's Medal - Awarded for destroying three enemy vehicles with a medium tank. The targets must be at least two tiers higher than the player's tank.\nAlexander Oskin, a Hero of the Soviet Union, was a tank commander who destroyed three King Tigers with his T-34 during a reconnaissance operation near Oglenduv on August 11, 1944.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getMedalOskinColumn, "Medal Oskin");
		    
		    
		    //====  ADD column number Medal getMedalOskin
		    TextColumn<CommunityAccount> nbgetMedalOskinColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalOskin());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetMedalOskinColumn, "Nb");
		    nbgetMedalOskinColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetMedalOskinColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalOskin();
		            	int val2 = o2.getData().getAchievements().getMedalOskin();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    
		    //===ADD column getMedalOskin
		    Column<CommunityAccount, SafeHtml > getMedalPoppelColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMedalPoppel();
					String urlImgSrc = noData;
					//urlImgSrc = "http://wiki.worldoftanks.com/images/5/5f/MedalOskin.png";
					
					switch (val) {
						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
						
					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Popel's Medal - Awarded for detecting enemy tanks and self-propelled guns in all battles, in four classes: Class IV - 20 vehicles Class III - 200 vehicles Class II - 2,000 vehicles Class I - 20,000 vehicles\nLieutenant General of Tank Forces, Nikolay Popel, a Soviet military leader and political worker, organized a raid against the enemy rear using captured vehicles during the battle of Dubno in the Summer of 1941.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getMedalPoppelColumn, "Medal Poppel");
		    
		    
		    //====  ADD column number Medal getMedalOskin
		    TextColumn<CommunityAccount> nbgetMedalPoppelColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMedalPoppel());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetMedalPoppelColumn, "Class");
		    nbgetMedalPoppelColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetMedalPoppelColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMedalPoppel();
		            	int val2 = o2.getData().getAchievements().getMedalPoppel();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    //===ADD column getMousebane
		    Column<CommunityAccount, SafeHtml > getMousebaneColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getMousebane();
					String urlImgSrc = noData;
					urlImgSrc = "http://wiki.worldoftanks.com/images/d/db/Mousebane.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//						
//					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Mouse Trap - Destroy 10 or more PzKpfw VIII Maus tanks. The icon in the service record displays the number of times the achievement was awarded.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getMousebaneColumn, "Medal Mouse Trap");
		    
		    
		    //====  ADD column number Medal getMousebane
		    TextColumn<CommunityAccount> nbgetMousebaneColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getMousebane());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetMousebaneColumn, "Nb");
		    nbgetMousebaneColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetMousebaneColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getMousebane();
		            	int val2 = o2.getData().getAchievements().getMousebane();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		
		    //===ADD column getRaider
		    Column<CommunityAccount, SafeHtml > getRaiderColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getRaider();
					String urlImgSrc = noData;
					urlImgSrc = "http://wiki.worldoftanks.com/images/e/e7/Raider.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//						
//					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Raider - Capture the enemy base and remain undetected during the entire battle. The icon in the service record displays the number of times the achievement was awarded.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getRaiderColumn, "Medal Raider");
		    
		    
		    //====  ADD column number Medal getRaider
		    TextColumn<CommunityAccount> nbgetRaiderColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getRaider());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetRaiderColumn, "Nb");
		    nbgetRaiderColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetRaiderColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getRaider();
		            	int val2 = o2.getData().getAchievements().getRaider();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    //===ADD column getScout
		    Column<CommunityAccount, SafeHtml > getScoutColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getScout();
					String urlImgSrc = noData;
					urlImgSrc = "http://wiki.worldoftanks.com/images/6/69/Scout.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//						
//					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Scout - Detect most enemy tanks and self-propelled guns than anyone else on your team (at least nine). The achievement is granted to the winning team only. \nIf two or more players have detected equal number of enemy vehicles, the achievement is granted to the player who has earned more XP, including additional XP provided to Premium account users. If the amount of XP is equal as well, the achievement is not granted.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getScoutColumn, "Medal Scout");
		    
		    
		    //====  ADD column number Medal getScout
		    TextColumn<CommunityAccount> nbgetScoutColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getScout());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetScoutColumn, "Nb");
		    nbgetScoutColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetScoutColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getScout();
		            	int val2 = o2.getData().getAchievements().getScout();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    
		    
		    //===ADD column getSniper
		    Column<CommunityAccount, SafeHtml > getSniperColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getSniper();
					String urlImgSrc = noData;
					urlImgSrc = "http://wiki.worldoftanks.com/images/8/8f/Sniper.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//						
//					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Sniper - Achieve at least 85% hits out of a minimum of ten shots fired with the potential damage of 1,000 HP and more. Non-penetrating hits are included, but hits on friendly units are not included. \nIf two or more players have an equal hit ratio, the achievement is granted to the player with the highest potential damage. If two or more players have an equal amount of potential damage, the achievement is granted to the player who earned more XP for the battle, including additional XP provided to Premium Account users. If the amount of XP is equal as well, the achievement is not granted.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getSniperColumn, "Medal Sniper");
		    
		    
		    //====  ADD column number Medal getSniper
		    TextColumn<CommunityAccount> nbgetSniperColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getSniper());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetSniperColumn, "Nb");
		    nbgetSniperColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetSniperColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getSniper();
		            	int val2 = o2.getData().getAchievements().getSniper();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		   
		    
		    
		    //===ADD column getTankExpert
		    Column<CommunityAccount, SafeHtml > getTankExpertColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CommunityAccount object) {
					// TODO Auto-generated method stub
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					//the img depend of value
					int val = object.getData().getAchievements().getTankExpert();
					String urlImgSrc = noData;
					urlImgSrc = "http://wiki.worldoftanks.com/images/b/be/TankExpert.png";
					
//					switch (val) {
//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//						
//					};
					
					String urlTarget = "http://wiki.worldoftanks.com/Achievements";
					String title ="Master Tanker - Destroy at least one of every type of enemy vehicle currently available in the game. In the event that new vehicles are added to any tech tree, the icon becomes gray in the player's Service Record.";
					String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
					
					sb.appendHtmlConstant(html);
					return sb.toSafeHtml();
				}
				
			};
		    tableAchivementCommAcc.addColumn(getTankExpertColumn, "Medal Tank expert");
		    
		    
		    //====  ADD column number Medal getTankExpert
		    TextColumn<CommunityAccount> nbgetTankExpertColumn = new TextColumn<CommunityAccount>() {
		      @Override
		      public String getValue(CommunityAccount object) {
		        return String.valueOf(object.getData().getAchievements().getTankExpert());
		      }
		    };
		    tableAchivementCommAcc.addColumn(nbgetTankExpertColumn, "Nb");
		    nbgetTankExpertColumn.setSortable(true);
		    
		 // Add a ColumnSortEvent.ListHandler to connect sorting to the
		    columnSortHandler.setComparator(nbgetTankExpertColumn,
		        new Comparator<CommunityAccount>() {
		          public int compare(CommunityAccount o1, CommunityAccount o2) {
		            if (o1 == o2) {
		              return 0;
		            }

		            // Compare the name columns.
		            if (o1 != null) {
		            	int val1 = o1.getData().getAchievements().getTankExpert();
		            	int val2 = o2.getData().getAchievements().getTankExpert();
		            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
		            }
		            return -1;
		          }
		        });
		    
		    
		    
	
		    
		    
		    
		    
		    /////////////////////////////////////////////////
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
										
										//get wiki wot (pour les m�dailles)
										xmlWiki = listClan.getWiki();
										
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
					getAchievementMember2();
//					offsetClan = 0;
//					limitClan = 100;
				}
	
				/**
				 * Fired when the user types in the nameField.
				 */
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						getAchievementMember2();
//						offsetClan = 0;
//						limitClan = 100;
					}
				}
				private void getAchievementMember2() {
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
					buildACellTableForAchivementsCommunityAccount(dataStatsProvider.getList(), xmlWiki);
					  
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
									buildACellTableForAchivementsCommunityAccount(listAccount.getListCommunityAccount(), xmlWiki);
									  
									
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


	/*
			 * call this when we have data to put in table
			 */
//			public  void buildACellTableForAchivementsCommunityAccountWithWiki(List<CommunityAccount> listCommAcc, XmlWiki xmlWiki) {
//			    
//				
//				tableAchivementCommAcc.setPageSize(30);
//				
//			    //update dataprovider with some known list 
//				dataAchievementsProvider.setList(listCommAcc);
//				
//				// Create a CellTable.
//			    tableAchivementCommAcc.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
//			    
//			    
//			    ListHandler<CommunityAccount> columnSortHandler =
//				        new ListHandler<CommunityAccount>(dataAchievementsProvider.getList());
//			    tableAchivementCommAcc.addColumnSortHandler(columnSortHandler);
//			    
//			    
//			    // Add a text column to show the name.
//			    TextColumn<CommunityAccount> nameColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return object.getName();
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nameColumn, "Name");
//		
//			    nameColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nameColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//		
//			            // Compare the name columns.
//			            if (o1 != null) {
//			              return (o2 != null) ? o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase()) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    // We know that the data is sorted alphabetically by default.
//			    tableAchivementCommAcc.getColumnSortList().push(nameColumn);
//		    
//		
//			    //add column Hunter
//			    Column<CommunityAccount, SafeHtml > hunter2Column = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						String urlImgSrc = "http://wiki.worldoftanks.com/images/4/44/Beasthunter.png";
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String titleSave ="<b>B�lter's Medal</b> Tank Hunter Destroy 100 or more: <br />" +"Jagdpanther, Jagdtiger, PzKpfw V Panther, Panther II, PzKpfw VI Tiger, PzKpfw VI Ausf. B Tiger II, Gw-Panther, Gw-Tiger. ";
//						
//						String title = 	"<b>B�lter's Medal</b> - With the release of Version 0.8.0, this medal is no longer being awarded."+  
//								"Prior to 0.8.0, this was awarded for destroying seven or more enemy tanks and self-propelled guns with a tank or tank destroyer,"+ 
//								"or 10 or more vehicles with a self-propelled gun in one battle.	The targets must be at least tier four enemy vehicles."+  
//								"<br />"+
//								"<a rel=\"nofollow\" target=\"_blank\" class=\"external text\" href=\"http://en.wikipedia.org/wiki/Johannes_B%C3%B6lter\">Johannes B�lter</a>"+
//								"was one of the most successful German tank aces of WWII. He participated in operations in the invasions of Poland, France, "+
//								"Greece and the Soviet Union, and the defense of France.";
//	
//						String html = "<a title =\"" + titleSave + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(hunter2Column, "Title Hunter");
//			    hunter2Column.setSortable(false);
//			    
//			    
//			    //-- Add column number Hunter
//			    TextColumn<CommunityAccount> nbHunterColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getBeasthunter());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbHunterColumn, "Nb");
//			    nbHunterColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbHunterColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getBeasthunter();
//			            	int val2 = o2.getData().getAchievements().getBeasthunter();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    
//			    //add column defender
//			    Column<CommunityAccount, SafeHtml > defenderColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						String urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/Defender.png";
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Defender - Reduce the amount of enemy capture points on a friendly base by 70 or more. If two or more players have reduced equal amount of capture points, the achievement is granted to the player who has earned more XP in the battle (including additional XP provided to Premium account users). If the amount of XP is equal as well, the achievement is not awarded.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(defenderColumn, "Defender");
//			    defenderColumn.setSortable(false);
//			    
//			    
//			    //-- Add column number Hunter
//			    TextColumn<CommunityAccount> nbDefenderColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getDefender());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbDefenderColumn, "Nb");
//			    nbDefenderColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbDefenderColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getDefender();
//			            	int val2 = o2.getData().getAchievements().getDefender();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    
//			    //add column diehard
//			    Column<CommunityAccount, SafeHtml > diehardColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						String urlImgSrc = "http://wiki.worldoftanks.com/images/7/7e/Diehard.png";
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Survive 20 or more consecutive battles. Battles fought using self-propelled guns do not break the sequence, but are not included either.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(diehardColumn, "Survivor");
//			    diehardColumn.setSortable(false);
//			    
//			    
//			    //-- Add column number diehard
//			    TextColumn<CommunityAccount> nbDiehardColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getDiehard());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbDiehardColumn, "Nb");
//			    nbDiehardColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbDiehardColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getDiehard();
//			            	int val2 = o2.getData().getAchievements().getDiehard();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    //add column Invader
//			    Column<CommunityAccount, SafeHtml > invaderColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						String urlImgSrc = "http://wiki.worldoftanks.com/images/7/77/Invader.png";
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Invader - Capture the maximum number of points from the enemy base, but not less than 80. The achievement is granted on successful base capture, including only the points that were part of the base capture. If the battle ends in a draw, the achievement is granted to the first player to receive 80 or more capture points.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(invaderColumn, "Invader");
//			    invaderColumn.setSortable(false);
//			    
//			    
//			    //-- Add column number Invader
//			    TextColumn<CommunityAccount> nbInvaderColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getInvader());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbInvaderColumn, "Nb");
//			    nbInvaderColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbInvaderColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getInvader();
//			            	int val2 = o2.getData().getAchievements().getInvader();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    //add column MedalAbrams
//			    Column<CommunityAccount, SafeHtml > MedalAbramsColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalAbrams();
//						String urlImgSrc = noData;
//						
//						switch (val) {
//							case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png"; break ;
//							case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png"; break ;
//							case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png"; break ;
//							case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png"; break ;
//							
//						};
//						// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
//						// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
//						// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
//						// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
//						
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Abrams' Medal - Awarded in one of four classes for the total number of team victories in which the player survived the battle: \nClass IV - 5 victories \nClass III - 50 victories \nClass II - 500 victories \nClass I - 5,000 victories.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(MedalAbramsColumn, "Medal Abrams");
//			    MedalAbramsColumn.setSortable(false);
//			    
//			    
//			    //-- Add column number MedalAbrams
//			    TextColumn<CommunityAccount> nbMedalAbrams = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalAbrams());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbMedalAbrams, "Class");
//			    nbMedalAbrams.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbMedalAbrams,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalAbrams();
//			            	int val2 = o2.getData().getAchievements().getMedalAbrams();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    //===add column getMedalBillotte
//			    Column<CommunityAccount, SafeHtml > medalBillotteColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalBillotte();
//						String urlImgSrc = "http://wiki.worldoftanks.com/images/b/b0/MedalBillotte.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png"; break ;
//	//						
//	//					};
//						// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
//						// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
//						// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
//						// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
//						
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Billotte's Medal - Awarded to players who destroy at least one enemy vehicle and survive the battle to victory despite receiving at least five different critical hits and 80% or more loss of hit points. ";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(medalBillotteColumn, "Medal Billotte");
//			    medalBillotteColumn.setSortable(false);
//			    
//			    
//			    //====  Add column number medalBillotte
//			    TextColumn<CommunityAccount> nbMedalBillotteColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalBillotte());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbMedalBillotteColumn, "Nb");
//			    nbMedalBillotteColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbMedalBillotteColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalBillotte();
//			            	int val2 = o2.getData().getAchievements().getMedalBillotte();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    //===add column MedalBurda
//			    Column<CommunityAccount, SafeHtml > MedalBurdaColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalBurda();
//						String urlImgSrc = "http://wiki.worldoftanks.com/images/d/d1/MedalBurda.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png"; break ;
//	//						
//	//					};
//						// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
//						// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
//						// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
//						// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
//						
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Burda's Medal - Awarded for the destruction of five or more enemy self-propelled guns. \nGuards Colonel Alexander Burda was a Soviet tank ace and a Hero of the Soviet Union. On October 4, 1941, Burda organized an ambush and destroyed an enemy armored column, including 10 medium and light tanks, 2 trucks with antitank guns and 5 infantry vehicles. Not awarded to SPG drivers.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(MedalBurdaColumn, "Medal Burda");
//			    
//			    
//			    //====  Add column number MedalBurda
//			    TextColumn<CommunityAccount> nbMedalBurdaColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalBurda());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbMedalBurdaColumn, "Nb");
//			    nbMedalBurdaColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbMedalBurdaColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalBurda();
//			            	int val2 = o2.getData().getAchievements().getMedalBurda();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    //===add column MedalCarius
//			    Column<CommunityAccount, SafeHtml > MedalCariusColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalCarius();
//						String urlImgSrc = noData;
//						
//						switch (val) {
//							case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/c/ce/MedalCarius1.png"; break ;
//							case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/7/7d/MedalCarius2.png"; break ;
//							case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/98/MedalCarius3.png"; break ;
//							case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/7/71/MedalCarius4.png"; break ;
//							
//						};
//						// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
//						// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
//						// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
//						// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
//						
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Carius' Medal - Awarded for the destruction of enemy tanks and self-propelled guns in four classes:\nClass IV - 10 vehicles \nClass III - 100 vehicles \nClass II - 1,000 vehicles \nClass I - 10,000 vehicles. \nOtto Carius was one of the most efficient tank aces of WWII. He commanded the Pz.Kpfw. 38 (t), the Pz.Kpfw. VI Tiger and the Jagdtiger tank destroyer during his impressive career.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(MedalCariusColumn, "Medal Carius");
//			    
//			    
//			    //====  Add column number MedalCarius
//			    TextColumn<CommunityAccount> nbMedalCariusColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalCarius());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbMedalCariusColumn, "Class");
//			    nbMedalCariusColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbMedalCariusColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalCarius();
//			            	int val2 = o2.getData().getAchievements().getMedalCarius();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    //===add column MedalCarius
//			    Column<CommunityAccount, SafeHtml > MedalMedalEkinsColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalEkins();
//						String urlImgSrc = noData;
//						
//						switch (val) {
//							case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/87/MedalEkins1.png"; break ;
//							case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/9b/MedalEkins2.png"; break ;
//							case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/13/MedalEkins3.png"; break ;
//							case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/94/MedalEkins4.png"; break ;
//							
//						};
//						// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
//						// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
//						// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
//						// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
//						
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Ekins' Medal - Awarded in four classes for destroying tier 8, 9 or 10 enemy tanks and self-propelled guns: \nClass IV - 3 vehicles \nClass III - 30 vehicles \nClass II - 300 vehicles \nClass I - 3,000 vehicles. \nJoe Ekins was a private in the Northamptonshire Division of the British Territorial Army. A number of sources confirm Ekins as the final nemesis of famous German tank ace Michael Wittmann.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(MedalMedalEkinsColumn, "Medal Ekins");
//			    
//			    
//			    //====  Add column number MedalCarius
//			    TextColumn<CommunityAccount> nbMedalEkinsColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalEkins());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbMedalEkinsColumn, "Class");
//			    nbMedalEkinsColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbMedalEkinsColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalEkins();
//			            	int val2 = o2.getData().getAchievements().getMedalEkins();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    //===add column MedalFadin
//			    Column<CommunityAccount, SafeHtml > MedalFadinColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalFadin();
//						String urlImgSrc = noData;
//						urlImgSrc = "http://wiki.worldoftanks.com/images/e/e7/MedalFadin.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/87/MedalEkins1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/9b/MedalEkins2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/13/MedalEkins3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/94/MedalEkins4.png"; break ;
//	//						
//	//					};
//						// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
//						// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
//						// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
//						// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
//						
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Fadin's Medal - Awarded for destroying the last enemy vehicle in the battle with the last shell remaining in the player's tank. \nA hero of the Soviet Union, Alexander Fadin was a T-34 commander. Supported by one infantry platoon, Fadin managed to capture and hold the Dashukovka village for 5 hours with one tank, and destroyed 3 tanks, 1 halftrack, 2 mortars and 12 machinegun nests. His crew also shot down an enemy plane with his tank's coaxial machinegun.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(MedalFadinColumn, "Medal Fadin");
//			    
//			    
//			    //====  Add column number Medal Fadin
//			    TextColumn<CommunityAccount> nbMedalFadinColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalFadin());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbMedalFadinColumn, "Nb");
//			    nbMedalFadinColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbMedalFadinColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalFadin();
//			            	int val2 = o2.getData().getAchievements().getMedalFadin();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    
//			    //===add column MedalHalonen
//			    Column<CommunityAccount, SafeHtml > MedalHalonenColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalHalonen();
//						String urlImgSrc = noData;
//						urlImgSrc = "http://wiki.worldoftanks.com/images/3/3a/MedalHalonen.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/87/MedalEkins1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/9b/MedalEkins2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/13/MedalEkins3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/94/MedalEkins4.png"; break ;
//	//						
//	//					};
//						// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
//						// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
//						// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
//						// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
//						
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Halonen's Medal - Awarded for destroying three or more enemy vehicles with a tank destroyer. \nThe targets must be at least two tiers higher than the player's vehicle.\nErkki Halonen, a sergeant in the Finnish Army and a tank ace, destroyed three T-34, two KV-1, and two ISU-152 with his StuG III in battles during June and July, 1944.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(MedalHalonenColumn, "Medal Halonen ");
//			    
//			    
//			    //====  Add column number Medal halonen
//			    TextColumn<CommunityAccount> nbMedalHalonenColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalHalonen());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbMedalHalonenColumn, "Nb");
//			    nbMedalHalonenColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbMedalHalonenColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalHalonen();
//			            	int val2 = o2.getData().getAchievements().getMedalHalonen();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    //===add column getMedalKay
//			    Column<CommunityAccount, SafeHtml > getMedalKayColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalKay();
//						String urlImgSrc = noData;
//	//					urlImgSrc = "http://wiki.worldoftanks.com/images/3/3a/MedalHalonen.png";
//						
//						switch (val) {
//							case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/c/c6/MedalKay1.png"; break ;
//							case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/e/e8/MedalKay2.png"; break ;
//							case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/10/MedalKay3.png"; break ;
//							case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/6/6f/MedalKay4.png"; break ;
//							
//						};
//						// http://wiki.worldoftanks.com/images/1/1d/MedalAbrams4.png
//						// http://wiki.worldoftanks.com/images/b/b8/MedalAbrams3.png
//						// http://wiki.worldoftanks.com/images/8/80/MedalAbrams2.png
//						// http://wiki.worldoftanks.com/images/2/27/MedalAbrams1.png
//						
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Kay's Medal - Awarded for achieving the Battle Hero status in four classes:\nClass IV - 1 time \nClass III - 10 times \nClass II - 100 times \nClass I - 1,000 times.\nDouglas Kay, a British Army sergeant, and gunner on a Sherman Firefly, participated in the Allied landing in Normandy and was famous for the popularization of the history of tank warfare.\nNote: Only the medals listed under 'Battle Hero Achievements' above are counted in the 'Kay�s Medal' achievement.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getMedalKayColumn, "Medal Kay ");
//			    
//			    
//			    //====  Add column number Medal Kay
//			    TextColumn<CommunityAccount> nbgetMedalKayColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalKay());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetMedalKayColumn, "Class");
//			    nbgetMedalKayColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetMedalKayColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalKay();
//			            	int val2 = o2.getData().getAchievements().getMedalKay();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    
//			    
//			    //===add column Knispel
//			    Column<CommunityAccount, SafeHtml > getMedalKnispelColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalKnispel();
//						String urlImgSrc = noData;
//	//					urlImgSrc = "http://wiki.worldoftanks.com/images/3/3a/MedalHalonen.png";
//						
//						switch (val) {
//							case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/9c/MedalKnispel1.png"; break ;
//							case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/d/d6/MedalKnispel2.png"; break ;
//							case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/c/cc/MedalKnispel3.png"; break ;
//							case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/91/MedalKnispel4.png"; break ;
//							
//						};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Knispel's Medal - Awarded for the total amount of damage caused and received in four classes: \nClass IV - 10,000 HP \nClass III - 100,000 HP \nClass II - 1,000,000 HP \nClass I - 10,000,000 HP.\nKurt Knispel, a German tank ace during WWII, participated in battles on both the Western and Eastern Fronts fighting on Pz.Kpfw. II, Pz.Kpfw. III, Pz.Kpfw. IV, Pz.Kpfw. VI Tiger, and Pz.Kpfw. Tiger II.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getMedalKnispelColumn, "Medal Knispel ");
//			    
//			    
//			    //====  Add column number Medal Knispel
//			    TextColumn<CommunityAccount> nbgetMedalKnispelColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalKnispel());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetMedalKnispelColumn, "Class");
//			    nbgetMedalKnispelColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetMedalKnispelColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalKnispel();
//			            	int val2 = o2.getData().getAchievements().getMedalKnispel();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    
//			    
//			    //===add column getMedalKolobanov
//			    Column<CommunityAccount, SafeHtml > getMedalKolobanovColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalKolobanov();
//						String urlImgSrc = noData;
//						urlImgSrc = "http://wiki.worldoftanks.com/images/7/75/MedalKolobanov.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/9c/MedalKnispel1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/d/d6/MedalKnispel2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/c/cc/MedalKnispel3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/91/MedalKnispel4.png"; break ;
//	//						
//	//					};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Kolobanov's Medal - Awarded to a player who stands alone against five or more enemy tanks or self-propelled guns and wins (this means that you can capture the enemy base by yourself when you are against five enemies and you will recieve the achievement.)\nColonel Zinoviy Kolobanov was a Soviet tank ace who destroyed 22 German tanks, 2 guns and 2 halftracks with his KV in battle on August 19, 1941.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getMedalKolobanovColumn, "Medal Kolobanov ");
//			    
//			    
//			    //====  Add column number Medal getMedalKolobanov
//			    TextColumn<CommunityAccount> nbgetMedalKolobanovColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalKolobanov());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetMedalKolobanovColumn, "Nb");
//			    nbgetMedalKolobanovColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetMedalKolobanovColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalKolobanov();
//			            	int val2 = o2.getData().getAchievements().getMedalKolobanov();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    
//			    
//			    //===add column getMedalLavrinenko
//			    Column<CommunityAccount, SafeHtml >getMedalLavrinenkoColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalLavrinenko();
//						String urlImgSrc = noData;
//						//urlImgSrc = "http://wiki.worldoftanks.com/images/7/75/MedalKolobanov.png";
//						
//						switch (val) {
//							case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/a/a5/MedalLavrinenko1.png"; break ;
//							case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/e/ee/MedalLavrinenko2.png"; break ;
//							case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/4/4a/MedalLavrinenko3.png"; break ;
//							case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/c/c2/MedalLavrinenko4.png"; break ;
//							
//						};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Lavrinenko's Medal - Awarded for reducing the total number of capture points of a friendly base, up to 100 points per battle. This award is established in four classes: \nClass IV - 30 points \nClass III - 300 points\n Class II - 3,000 points \nClass I - 30,000 points.\nDmitry Lavrinenko, a Hero of the Soviet Union, Guards Lieutenant, and tank ace was recognized as the most efficient Soviet tanker, destroying 52 tanks in 28 battles over the course of just two months.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getMedalLavrinenkoColumn, "Medal Lavrinenko ");
//			    
//			    
//			    //====  Add column number Medal getMedalLavrinenko
//			    TextColumn<CommunityAccount> nbgetMedalLavrinenkoColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalLavrinenko());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetMedalLavrinenkoColumn, "Class");
//			    nbgetMedalLavrinenkoColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetMedalLavrinenkoColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalLavrinenko();
//			            	int val2 = o2.getData().getAchievements().getMedalLavrinenko();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			
//			    
//			    //===ADD column getMedalLeClerc
//			    Column<CommunityAccount, SafeHtml > getMedalLeClercColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalLeClerc();
//						String urlImgSrc = noData;
//						//urlImgSrc = "http://wiki.worldoftanks.com/images/7/75/MedalKolobanov.png";
//						
//						switch (val) {
//							case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/83/MedalLeClerc1.png"; break ;
//							case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/6/62/MedalLeClerc2.png"; break ;
//							case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/3/3d/MedalLeClerc3.png"; break ;
//							case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalLeClerc4.png"; break ;
//							
//						};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Leclerc's Medal - Awarded for the total amount of the player's enemy base capture points. An unsuccessful or reduced capture does not count toward this number. The award is established in four classes: Class IV - 30 points Class III - 300 points Class II - 3,000 points Class I - 30,000 points\nPhilippe Leclerc was a General of the Free French Forces during WWII and one of the leaders of the Paris liberation operation.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getMedalLeClercColumn, "Medal Le Clerc ");
//			    
//			    
//			    //====  ADD column number Medal getMedalLeClerc
//			    TextColumn<CommunityAccount> nbgetMedalLeClercColumnColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalLeClerc());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetMedalLeClercColumnColumn, "Class");
//			    nbgetMedalLeClercColumnColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetMedalLeClercColumnColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalLeClerc();
//			            	int val2 = o2.getData().getAchievements().getMedalLeClerc();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			 
//			    
//			    
//			    
//			    //===ADD column getMedalOrlik
//			    Column<CommunityAccount, SafeHtml > getMedalOrlikColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalOrlik();
//						String urlImgSrc = noData;
//						urlImgSrc = "http://wiki.worldoftanks.com/images/2/2b/MedalOrlik.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/83/MedalLeClerc1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/6/62/MedalLeClerc2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/3/3d/MedalLeClerc3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalLeClerc4.png"; break ;
//	//						
//	//					};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Orlik's Medal - Awarded for destroying three or more enemy tanks or tank destroyers with a light tank. The targets must be at least two tiers higher than the player's tank.\nRoman Edmund Orlik, a Polish Army sergeant, was a tank ace who knocked out 13 German tanks with his light TKS tankette in September, 1939.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getMedalOrlikColumn, "Medal Orlik");
//			    
//			    
//			    //====  ADD column number Medal getMedalOrlik
//			    TextColumn<CommunityAccount> nbgetMedalOrlikColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalOrlik());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetMedalOrlikColumn, "Nb");
//			    nbgetMedalOrlikColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetMedalOrlikColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalOrlik();
//			            	int val2 = o2.getData().getAchievements().getMedalOrlik();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    //===ADD column getMedalOskin
//			    Column<CommunityAccount, SafeHtml > getMedalOskinColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalOskin();
//						String urlImgSrc = noData;
//						urlImgSrc = "http://wiki.worldoftanks.com/images/5/5f/MedalOskin.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/8/83/MedalLeClerc1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/6/62/MedalLeClerc2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/3/3d/MedalLeClerc3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/1/1d/MedalLeClerc4.png"; break ;
//	//						
//	//					};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Oskin's Medal - Awarded for destroying three enemy vehicles with a medium tank. The targets must be at least two tiers higher than the player's tank.\nAlexander Oskin, a Hero of the Soviet Union, was a tank commander who destroyed three King Tigers with his T-34 during a reconnaissance operation near Oglenduv on August 11, 1944.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getMedalOskinColumn, "Medal Oskin");
//			    
//			    
//			    //====  ADD column number Medal getMedalOskin
//			    TextColumn<CommunityAccount> nbgetMedalOskinColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalOskin());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetMedalOskinColumn, "Nb");
//			    nbgetMedalOskinColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetMedalOskinColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalOskin();
//			            	int val2 = o2.getData().getAchievements().getMedalOskin();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    
//			    //===ADD column getMedalOskin
//			    Column<CommunityAccount, SafeHtml > getMedalPoppelColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMedalPoppel();
//						String urlImgSrc = noData;
//						//urlImgSrc = "http://wiki.worldoftanks.com/images/5/5f/MedalOskin.png";
//						
//						switch (val) {
//							case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//							case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//							case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//							case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//							
//						};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Popel's Medal - Awarded for detecting enemy tanks and self-propelled guns in all battles, in four classes: Class IV - 20 vehicles Class III - 200 vehicles Class II - 2,000 vehicles Class I - 20,000 vehicles\nLieutenant General of Tank Forces, Nikolay Popel, a Soviet military leader and political worker, organized a raid against the enemy rear using captured vehicles during the battle of Dubno in the Summer of 1941.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getMedalPoppelColumn, "Medal Poppel");
//			    
//			    
//			    //====  ADD column number Medal getMedalOskin
//			    TextColumn<CommunityAccount> nbgetMedalPoppelColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMedalPoppel());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetMedalPoppelColumn, "Class");
//			    nbgetMedalPoppelColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetMedalPoppelColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMedalPoppel();
//			            	int val2 = o2.getData().getAchievements().getMedalPoppel();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    //===ADD column getMousebane
//			    Column<CommunityAccount, SafeHtml > getMousebaneColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getMousebane();
//						String urlImgSrc = noData;
//						urlImgSrc = "http://wiki.worldoftanks.com/images/d/db/Mousebane.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//	//						
//	//					};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Mouse Trap - Destroy 10 or more PzKpfw VIII Maus tanks. The icon in the service record displays the number of times the achievement was awarded.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getMousebaneColumn, "Medal Mouse Trap");
//			    
//			    
//			    //====  ADD column number Medal getMousebane
//			    TextColumn<CommunityAccount> nbgetMousebaneColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getMousebane());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetMousebaneColumn, "Nb");
//			    nbgetMousebaneColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetMousebaneColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getMousebane();
//			            	int val2 = o2.getData().getAchievements().getMousebane();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			
//			    //===ADD column getRaider
//			    Column<CommunityAccount, SafeHtml > getRaiderColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getRaider();
//						String urlImgSrc = noData;
//						urlImgSrc = "http://wiki.worldoftanks.com/images/e/e7/Raider.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//	//						
//	//					};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Raider - Capture the enemy base and remain undetected during the entire battle. The icon in the service record displays the number of times the achievement was awarded.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getRaiderColumn, "Medal Raider");
//			    
//			    
//			    //====  ADD column number Medal getRaider
//			    TextColumn<CommunityAccount> nbgetRaiderColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getRaider());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetRaiderColumn, "Nb");
//			    nbgetRaiderColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetRaiderColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getRaider();
//			            	int val2 = o2.getData().getAchievements().getRaider();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    //===ADD column getScout
//			    Column<CommunityAccount, SafeHtml > getScoutColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getScout();
//						String urlImgSrc = noData;
//						urlImgSrc = "http://wiki.worldoftanks.com/images/6/69/Scout.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//	//						
//	//					};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Scout - Detect most enemy tanks and self-propelled guns than anyone else on your team (at least nine). The achievement is granted to the winning team only. \nIf two or more players have detected equal number of enemy vehicles, the achievement is granted to the player who has earned more XP, including additional XP provided to Premium account users. If the amount of XP is equal as well, the achievement is not granted.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getScoutColumn, "Medal Scout");
//			    
//			    
//			    //====  ADD column number Medal getScout
//			    TextColumn<CommunityAccount> nbgetScoutColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getScout());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetScoutColumn, "Nb");
//			    nbgetScoutColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetScoutColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getScout();
//			            	int val2 = o2.getData().getAchievements().getScout();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    
//			    
//			    //===ADD column getSniper
//			    Column<CommunityAccount, SafeHtml > getSniperColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getSniper();
//						String urlImgSrc = noData;
//						urlImgSrc = "http://wiki.worldoftanks.com/images/8/8f/Sniper.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//	//						
//	//					};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Sniper - Achieve at least 85% hits out of a minimum of ten shots fired with the potential damage of 1,000 HP and more. Non-penetrating hits are included, but hits on friendly units are not included. \nIf two or more players have an equal hit ratio, the achievement is granted to the player with the highest potential damage. If two or more players have an equal amount of potential damage, the achievement is granted to the player who earned more XP for the battle, including additional XP provided to Premium Account users. If the amount of XP is equal as well, the achievement is not granted.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getSniperColumn, "Medal Sniper");
//			    
//			    
//			    //====  ADD column number Medal getSniper
//			    TextColumn<CommunityAccount> nbgetSniperColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getSniper());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetSniperColumn, "Nb");
//			    nbgetSniperColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetSniperColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getSniper();
//			            	int val2 = o2.getData().getAchievements().getSniper();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			   
//			    
//			    
//			    //===ADD column getTankExpert
//			    Column<CommunityAccount, SafeHtml > getTankExpertColumn = new Column<CommunityAccount, SafeHtml>(new SafeHtmlCell()) {
//					
//					@Override
//					public SafeHtml getValue(CommunityAccount object) {
//						// TODO Auto-generated method stub
//						SafeHtmlBuilder sb = new SafeHtmlBuilder();
//						//the img depend of value
//						int val = object.getData().getAchievements().getTankExpert();
//						String urlImgSrc = noData;
//						urlImgSrc = "http://wiki.worldoftanks.com/images/b/be/TankExpert.png";
//						
//	//					switch (val) {
//	//						case 1  :  urlImgSrc = "http://wiki.worldoftanks.com/images/0/0d/MedalPoppel1.png"; break ;
//	//						case 2  :  urlImgSrc = "http://wiki.worldoftanks.com/images/f/fe/MedalPoppel2.png"; break ;
//	//						case 3  :  urlImgSrc = "http://wiki.worldoftanks.com/images/9/95/MedalPoppel3.png"; break ;
//	//						case 4  :  urlImgSrc = "http://wiki.worldoftanks.com/images/5/55/MedalPoppel4.png"; break ;
//	//						
//	//					};
//						
//						String urlTarget = "http://wiki.worldoftanks.com/Achievements";
//						String title ="Master Tanker - Destroy at least one of every type of enemy vehicle currently available in the game. In the event that new vehicles are added to any tech tree, the icon becomes gray in the player's Service Record.";
//						String html = "<a title =\"" + title + "\"" + " href=\"" +  urlTarget +  " \">" + "<img src=\"" + urlImgSrc + "\"" +  " width=\"25\" height=\"25\" >" + "</a>";
//						
//						sb.appendHtmlConstant(html);
//						return sb.toSafeHtml();
//					}
//					
//				};
//			    tableAchivementCommAcc.addColumn(getTankExpertColumn, "Medal Tank expert");
//			    
//			    
//			    //====  ADD column number Medal getTankExpert
//			    TextColumn<CommunityAccount> nbgetTankExpertColumn = new TextColumn<CommunityAccount>() {
//			      @Override
//			      public String getValue(CommunityAccount object) {
//			        return String.valueOf(object.getData().getAchievements().getTankExpert());
//			      }
//			    };
//			    tableAchivementCommAcc.addColumn(nbgetTankExpertColumn, "Nb");
//			    nbgetTankExpertColumn.setSortable(true);
//			    
//			 // Add a ColumnSortEvent.ListHandler to connect sorting to the
//			    columnSortHandler.setComparator(nbgetTankExpertColumn,
//			        new Comparator<CommunityAccount>() {
//			          public int compare(CommunityAccount o1, CommunityAccount o2) {
//			            if (o1 == o2) {
//			              return 0;
//			            }
//	
//			            // Compare the name columns.
//			            if (o1 != null) {
//			            	int val1 = o1.getData().getAchievements().getTankExpert();
//			            	int val2 = o2.getData().getAchievements().getTankExpert();
//			            	return (o2 != null) ?  Integer.valueOf(val1).compareTo(Integer.valueOf(val2)) : 1;
//			            }
//			            return -1;
//			          }
//			        });
//			    
//			    
//			    
//		
//			    
//			    
//			    
//			    
//			    /////////////////////////////////////////////////
//			    // Add a selection model to handle user selection.
//			    final SingleSelectionModel<CommunityAccount> selectionModel = new SingleSelectionModel<CommunityAccount>();
//			    tableAchivementCommAcc.setSelectionModel(selectionModel);
//			    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//			      public void onSelectionChange(SelectionChangeEvent event) {
//			    	  CommunityAccount selected = selectionModel.getSelectedObject();
//			        if (selected != null) {
//			          //Window.alert("You selected: " + selected.getName());
//			        }
//			      }
//			    });
//		
//			    // Set the total row count. This isn't strictly necessary, but it affects
//			    // paging calculations, so its good habit to keep the row count up to date.
//			    
//			    tableAchivementCommAcc.setRowCount(listCommAcc.size(), true); //no need to do here because we have add list to data provider
//		
//			    // Push the data into the widget.
//			    tableAchivementCommAcc.setRowData(0, listCommAcc);            //idem no nedd dataprovider
//			    
//			 // Connect the table to the data provider.
//			    dataAchievementsProvider.addDataDisplay(tableAchivementCommAcc);
//			    dataAchievementsProvider.refresh();
//		   }
			
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
			///////
			
			static public String buildHtml(HashMap<String, XmlListAchievement> hashMapAch, String nameAch, CommunityAccount object) {
				//String nameAch = "Beasthunter";
				XmlListAchievement ach = hashMapAch.get(nameAch+".png");
				String urlImgSrc2 =  ach.getSRCIMG().getSRC().get(0).getVALUE();
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				
				String nb= String.valueOf(object.getData().getAchievements().getBeasthunter());
				String html = 
						"<div id=\"achievement\" >" + " <div class=\"floatleft\"> " +
						" <img alt=\"" + nameAch+ ".png\" src=\"" + urlImgSrc2 + "\" width=\"67\" height=\"71\" />" + nb + "</div>";
				
				return html;
			}
			
			static public String buildHtmlHeader(HashMap<String, XmlListAchievement> hashMapAch, String nameAch) {
				//String nameAch = "Beasthunter";
				XmlListAchievement ach = hashMapAch.get(nameAch+".png");
			    String title2 = ach.getDESCRIPTION().getVALUE();
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String html = "<div id=\"achievement\" >" + " <div class=\"floatleft\"> " +	  title2 + "</div>";
				return html;
			}
			
}