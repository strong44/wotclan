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
public class WotTest1SAve implements EntryPoint {
	String idClan ="" ;
	int offsetClan = 0;
	int limitClan = 100;
	
	 /**
	   * A simple data type that represents a contact.
	   */
	  private static class Contact {
	    private final String address;
	    private final Date   birthday;
	    private final String name;

	    public Contact(String name, Date birthday, String address) {
	      this.name = name;
	      this.birthday = birthday;
	      this.address = address;
	    }
	  }
	  
	  RootPanel rootPanel ;
	  DockPanel dockPanel;
	  SimplePager pager;
	  CellTable<CommunityAccount> tableCommAcc = new  CellTable<CommunityAccount> ();
	  
	// Create a data provider.
	  ListDataProvider<CommunityAccount> dataProvider = new ListDataProvider<CommunityAccount>();
	    
	  /**
	   * The list of data to display.
	   */
//	  private static final List<CommunityAccount> CONTACTS = java.util.Arrays.asList(
//	      new CommunityAccount("1",  "toto"),
//	      new CommunityAccount("2",  "titi"),
//	      new CommunityAccount("3", "tata"));
	  

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

	
	
	public  CellTable<CommunityAccount> buildACellTableForCommunityAccount() {
	    // Create a CellTable.
	    CellTable<CommunityAccount> table = new CellTable<CommunityAccount>();
	    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

	    /*
	     *  ListHandler<ContactInfo> sortHandler =
        new ListHandler<ContactInfo>(ContactDatabase.get().getDataProvider().getList());
    dataGrid.addColumnSortHandler(sortHandler);
	     */
	    
//	    ListHandler<CommunityAccount> sortHandler =
//		        new ListHandler<CommunityAccount>(table.getVisibleItems());
//	    table.addColumnSortHandler(sortHandler);
	    
	    
	    // Add a text column to show the name.
	    TextColumn<CommunityAccount> nameColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return object.getName();
	      }
	    };
	    table.addColumn(nameColumn, "Name");

	    
	    nameColumn.setSortable(true);
//	    sortHandler.setComparator(nameColumn, new Comparator<CommunityAccount>() {
//	      @Override
//	      public int compare(CommunityAccount o1, CommunityAccount o2) {
//	        return o1.getName().compareTo(o2.getName());
//	      }
//	    });
	    
	    
//	    // Add a date column to show the birthday.
//	    DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
//	    DateCell dateCell = new DateCell();
//	    
//	    Column<Contact, Date> dateColumn = new Column<Contact, Date>(dateCell) {
//	      @Override
//	      public Date getValue(Contact object) {
//	        return object.birthday;
//	      }
//	    };
//	
//	    table.addColumn(dateColumn, "Birthday");

	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> addressColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return object.getIdUser();
	      }
	    };
	    table.addColumn(addressColumn, "User Id");

	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> wrColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_avg_performance());
	      }
	    };
	    table.addColumn(wrColumn, "Win rate");
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> avgXpColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_avg_xp() );
	      }
	    };
	    table.addColumn(avgXpColumn, "Avg Xp");
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> battleWinsColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_wins() );
	      }
	    };
	    table.addColumn(battleWinsColumn, "Victories");
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> battlesColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattles() );
	      }
	    };
	    table.addColumn(battlesColumn, "Battles");

	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> ctfColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getCtf_points() );
	      }
	    };
	    table.addColumn(ctfColumn, "Capture Points");

	
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> dmgColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getDamage_dealt() );
	      }
	    };
	    table.addColumn(dmgColumn, "Damage");

	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> dropCtfColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getDropped_ctf_points() );
	      }
	    };
	    table.addColumn(dropCtfColumn, "Defense Points");

	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> fragsColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getFrags() );
	      }
	    };
	    table.addColumn(fragsColumn, "Destroyed");

	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> irColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getIntegrated_rating() );
	      }
	    };
	    table.addColumn(irColumn, "Integrated Rating");

	
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> spottedColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getSpotted() );
	      }
	    };
	    table.addColumn(spottedColumn, "Detected");

	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> xpColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getXp() );
	      }
	    };
	    table.addColumn(xpColumn, "Experience");

	    
	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<CommunityAccount> selectionModel = new SingleSelectionModel<CommunityAccount>();
	    table.setSelectionModel(selectionModel);
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
	    //table.setRowCount(listCommAcc.size(), true);

	    // Push the data into the widget.
	    //table.setRowData(0, listCommAcc);
		return table;

	    // Add it to the root panel.
	    //RootPanel.get().add(table);
 }
	
	
	/*
	 * call this when we have data to put in table
	 */
	public  CellTable<CommunityAccount> buildACellTableForCommunityAccountWithData(List<CommunityAccount> listCommAcc) {
	    // Create a CellTable.
	    //CellTable<CommunityAccount> table = new CellTable<CommunityAccount>();
		tableCommAcc.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
	    
	    
	    
	 // Add the data to the data provider, which automatically pushes it to the
	    // widget.
//	    List<CommunityAccount> list = dataProvider.getList();
//	    list.addAll(0, listCommAcc);
	    
	    
//	    ListHandler<CommunityAccount> sortHandler =
//		        new ListHandler<CommunityAccount>(listCommAcc);
//	    table.addColumnSortHandler(sortHandler);
	    
	 // Create a Pager to control the table.
//	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
//	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
//	    pager.setDisplay(table);
	    
	    // Add a text column to show the name.
	    TextColumn<CommunityAccount> nameColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return object.getName();
	      }
	    };
	    tableCommAcc.addColumn(nameColumn, "Name");

	    
	    nameColumn.setSortable(true);
	    
	    
	    
//	    sortHandler.setComparator(nameColumn, new Comparator<CommunityAccount>() {
//	      @Override
//	      public int compare(CommunityAccount o1, CommunityAccount o2) {
//	        return o1.getName().compareTo(o2.getName());
//	      }
//	    });
	    
	 // Add a ColumnSortEvent.ListHandler to connect sorting to the
	    // java.util.List.
	    ListHandler<CommunityAccount> columnSortHandler = new ListHandler<CommunityAccount>(
	        dataProvider.getList());
	    columnSortHandler.setComparator(nameColumn,
	        new Comparator<CommunityAccount>() {
	          public int compare(CommunityAccount o1, CommunityAccount o2) {
	            if (o1 == o2) {
	              return 0;
	            }

	            // Compare the name columns.
	            if (o1 != null) {
	              return (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
	            }
	            return -1;
	          }
	        });
	    tableCommAcc.addColumnSortHandler(columnSortHandler);
	    
	 // We know that the data is sorted alphabetically by default.
	    tableCommAcc.getColumnSortList().push(nameColumn);
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> addressColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return object.getIdUser();
	      }
	    };
	    tableCommAcc.addColumn(addressColumn, "User Id");

	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> wrColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_avg_performance());
	      }
	    };
	    tableCommAcc.addColumn(wrColumn, "Win rate");
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> avgXpColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_avg_xp() );
	      }
	    };
	    tableCommAcc.addColumn(avgXpColumn, "Avg Xp");
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> battleWinsColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattle_wins() );
	      }
	    };
	    tableCommAcc.addColumn(battleWinsColumn, "Victories");
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> battlesColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getBattles() );
	      }
	    };
	    tableCommAcc.addColumn(battlesColumn, "Battles");

	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> ctfColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getCtf_points() );
	      }
	    };
	    tableCommAcc.addColumn(ctfColumn, "Capture Points");

	
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> dmgColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getDamage_dealt() );
	      }
	    };
	    tableCommAcc.addColumn(dmgColumn, "Damage");

	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> dropCtfColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getDropped_ctf_points() );
	      }
	    };
	    tableCommAcc.addColumn(dropCtfColumn, "Defense Points");

	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> fragsColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getFrags() );
	      }
	    };
	    tableCommAcc.addColumn(fragsColumn, "Destroyed");

	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> irColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getIntegrated_rating() );
	      }
	    };
	    tableCommAcc.addColumn(irColumn, "Integrated Rating");

	
	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> spottedColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getSpotted() );
	      }
	    };
	    tableCommAcc.addColumn(spottedColumn, "Detected");

	    
	    // Add a text column to show the address.
	    TextColumn<CommunityAccount> xpColumn = new TextColumn<CommunityAccount>() {
	      @Override
	      public String getValue(CommunityAccount object) {
	        return String.valueOf(object.getData().getStats().getXp() );
	      }
	    };
	    tableCommAcc.addColumn(xpColumn, "Experience");

	    
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
	    
	    //update dataprovider with some known list 
	    dataProvider.setList(listCommAcc);
	    
	    //RootPanel.get().add(table);
	    
		return tableCommAcc;

	    // Add it to the root panel.
	    
 }
	
	
//	 public void onModuleLoad5() {
//		    // Create a CellTable.
//		    CellTable<CommunityAccount> table = new CellTable<CommunityAccount>();
//		    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
//
//		    // Add a text column to show the name.
//		    TextColumn<CommunityAccount> nameColumn = new TextColumn<CommunityAccount>() {
//		      @Override
//		      public String getValue(CommunityAccount object) {
//		        return object.getName();
//		      }
//		    };
//		    table.addColumn(nameColumn, "Name");
//
////		    // Add a date column to show the birthday.
////		    DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
////		    DateCell dateCell = new DateCell();
////		    
////		    Column<Contact, Date> dateColumn = new Column<Contact, Date>(dateCell) {
////		      @Override
////		      public Date getValue(Contact object) {
////		        return object.birthday;
////		      }
////		    };
////		
////		    table.addColumn(dateColumn, "Birthday");
//
//		    // Add a text column to show the address.
//		    TextColumn<CommunityAccount> addressColumn = new TextColumn<CommunityAccount>() {
//		      @Override
//		      public String getValue(CommunityAccount object) {
//		        return object.getIdUser();
//		      }
//		    };
//		    table.addColumn(addressColumn, "User Id");
//
//		    
//		    // Add a selection model to handle user selection.
//		    final SingleSelectionModel<CommunityAccount> selectionModel = new SingleSelectionModel<CommunityAccount>();
//		    table.setSelectionModel(selectionModel);
//		    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//		      public void onSelectionChange(SelectionChangeEvent event) {
//		    	  CommunityAccount selected = selectionModel.getSelectedObject();
//		        if (selected != null) {
//		          Window.alert("You selected: " + selected.getName());
//		        }
//		      }
//		    });
//
//		    // Set the total row count. This isn't strictly necessary, but it affects
//		    // paging calculations, so its good habit to keep the row count up to date.
//		    table.setRowCount(CONTACTS.size(), true);
//
//		    // Push the data into the widget.
//		    table.setRowData(0, CONTACTS);
//
//		    // Add it to the root panel.
//		    RootPanel.get().add(table);
//	 }
	
	 
//	 public void onModuleLoad3() {
//		    // Create a CellTable.
//		    CellTable<Contact> table = new CellTable<Contact>();
//		    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
//
//		    // Add a text column to show the name.
//		    TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
//		      @Override
//		      public String getValue(Contact object) {
//		        return object.name;
//		      }
//		    };
//		    table.addColumn(nameColumn, "Name");
//
//		    // Add a date column to show the birthday.
//		    DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
//		    DateCell dateCell = new DateCell();
//		    
//		    Column<Contact, Date> dateColumn = new Column<Contact, Date>(dateCell) {
//		      @Override
//		      public Date getValue(Contact object) {
//		        return object.birthday;
//		      }
//		    };
//		
//		    table.addColumn(dateColumn, "Birthday");
//
//		    // Add a text column to show the address.
//		    TextColumn<Contact> addressColumn = new TextColumn<Contact>() {
//		      @Override
//		      public String getValue(Contact object) {
//		        return object.address;
//		      }
//		    };
//		    table.addColumn(addressColumn, "Address");
//
//		    // Add a selection model to handle user selection.
//		    final SingleSelectionModel<Contact> selectionModel = new SingleSelectionModel<Contact>();
//		    table.setSelectionModel(selectionModel);
//		    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//		      public void onSelectionChange(SelectionChangeEvent event) {
//		        Contact selected = selectionModel.getSelectedObject();
//		        if (selected != null) {
//		          Window.alert("You selected: " + selected.name);
//		        }
//		      }
//		    });
//
//		    // Set the total row count. This isn't strictly necessary, but it affects
//		    // paging calculations, so its good habit to keep the row count up to date.
//		    table.setRowCount(CONTACTS.size(), true);
//
//		    // Push the data into the widget.
//		    table.setRowData(0, CONTACTS);
//
//		    // Add it to the root panel.
//		    RootPanel.get().add(table);
//	 }
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad2() {
		
		
		
		
		final Label errorLabel = new Label();

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel rootPanel = RootPanel.get();
		//RootPanel.get("errorLabelContainer").add(errorLabel);
		
		DockPanel dockPanel = new DockPanel();
		rootPanel.add(dockPanel, 29, 259);
		dockPanel.setSize("1193px", "550px");
		
		
		
		//onInitialize();
		//dockPanel.add(dataGrid,DockPanel.CENTER);
		
		/////////
//		final Grid grid = new Grid(10, 2);
//		dockPanel.add(grid, DockPanel.SOUTH);
//		grid.setSize("1193px", "464px");
//		
//		final TextBox nameClan = new TextBox();
//		
//		
//		rootPanel.add(nameClan, 83, 10);
//		nameClan.setText("NOVA SNAIL");
//		nameClan.setSize("125px", "18px");

		
//		/// for datagrid
//		DataGrid<CommunityAccount> dataGrid;   //tableau des membres du clan
//		SimplePager pager;
//		// Create a DataGrid.
//
//	    /*
//	     * Set a key provider that provides a unique key for each contact. If key is
//	     * used to identify contacts when fields (such as the name and address)
//	     * change.
//	     */
//	    dataGrid = new DataGrid<CommunityAccount>();
//	    dataGrid.setWidth("100%");
//	    /*
//	     * Do not refresh the headers every time the data is updated. The footer
//	     * depends on the current data, so we do not disable auto refresh on the
//	     * footer.
//	     */
//	    dataGrid.setAutoHeaderRefreshDisabled(true);
//	    
//	    // Set the message to display when the table is empty.
//	    dataGrid.setEmptyTableWidget(new Label("Pas de membres dans ce clan"));
//	    
//	    
//	 // Attach a column sort handler to the ListDataProvider to sort the list.
//	    List<CommunityAccount> listCommunityAccount = new ArrayList<CommunityAccount>();
//   
//	    ListHandler<CommunityAccount> sortHandler =
//	        new ListHandler<CommunityAccount>(listCommunityAccount); // A construire apr�s le retour JSON 
//	    dataGrid.addColumnSortHandler(sortHandler);
//	    
//	    // Create a Pager to control the table.
//	    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
//	    pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
//	    pager.setDisplay(dataGrid);
//
//	    
//	    // Add a selection model so we can select cells.
//	    final SelectionModel<CommunityAccount> selectionModel =
//	        new MultiSelectionModel<CommunityAccount>();
//	    dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager
//	        .<CommunityAccount> createCheckboxManager());
//
//
//	    
	    
	    
		///
//		nameClan.addFocusHandler(new FocusHandler() {
//			
//			@Override
//			public void onFocus(FocusEvent event) {
//				// TODO Auto-generated method stub
//				offsetClan = 0;
//				limitClan = 100;
//			}
//		});
//		
		
		

		
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
					
					
					//grid.resizeRows(0);
					
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
									//dataProvider.removeDataDisplay(tableCommAcc);
									
									tableCommAcc = buildACellTableForCommunityAccountWithData(listAccount.getListCommunityAccount());
									
									
									// Connect the table to the data provider.
								    //dataProvider.addDataDisplay(tableCommAcc);
								    
								    
								    
								 // Add a ColumnSortEvent.ListHandler to connect sorting to the
								    // java.util.List.
//								    ListHandler<CommunityAccount> columnSortHandler = new ListHandler<CommunityAccount>(
//								        list);
//								    columnSortHandler.setComparator(nameColumn,
//								        new Comparator<CommunityAccount>() {
//								          public int compare(Contact o1, Contact o2) {
//								            if (o1 == o2) {
//								              return 0;
//								            }
//
//								            // Compare the name columns.
//								            if (o1 != null) {
//								              return (o2 != null) ? o1.name.compareTo(o2.name) : 1;
//								            }
//								            return -1;
//								          }
//								        });
//								    tableCommAcc.addColumnSortHandler(columnSortHandler);
								    
								    
								    //add to dock panel ======
									dockPanel.add(tableCommAcc, DockPanel.SOUTH);
									
									//tableCommAcc.setRowCount(listAccount.getListCommunityAccount().size());
									//tableCommAcc.setRowData(listAccount.getListCommunityAccount());
									tableCommAcc.setVisible(true);
								    /*
								     *  ListHandler<ContactInfo> sortHandler =
							        new ListHandler<ContactInfo>(ContactDatabase.get().getDataProvider().getList());
							    dataGrid.addColumnSortHandler(sortHandler);
								     */
//									 ListHandler<CommunityAccount> sortHandler =
//										        new ListHandler<CommunityAccount>(listAccount.getListCommunityAccount());
//									 tableCommAcc.addColumnSortHandler(sortHandler);
									 
									//dialogBox.center();
									//closeButton.setFocus(true);
								}
								
								public void onSuccessSave(AllCommunityAccount listAccount) {
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
									grid.setText(row, col, String.valueOf("Win rate"));col++; //battleAvgPerf Perf Moy Battle
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
							});
					searchClanButton.setEnabled(true);
					searchClanButton.setFocus(true);
				}
				
				
				
			}
			
			
			///
			///////////
			// Create a handler for the sendButton and nameField
			class HandlerGetMembersClan2 implements ClickHandler, KeyUpHandler {
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
									grid.setText(row, col, String.valueOf("Win rate"));col++; //battleAvgPerf Perf Moy Battle
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
