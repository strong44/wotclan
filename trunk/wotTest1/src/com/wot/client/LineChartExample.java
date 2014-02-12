package com.wot.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.LineChart;
import com.googlecode.gwt.charts.client.corechart.LineChartOptions;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.VAxis;
import com.wot.shared.CommunityAccount;

public class LineChartExample extends SimplePanel {
	private LineChart chart;
	List<CommunityAccount> listAccount ;
	
	public LineChartExample(List<CommunityAccount> listAccount) {
		this.listAccount = listAccount;
		//super(Unit.PX);
		initialize();
	}

	private void initialize() {
		ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
		chartLoader.loadApi(new Runnable() {

			@Override
			public void run() {
				// Create and attach the chart
				chart = new LineChart();
				add(chart);
				draw();
			}
		});
	}

	private void draw() {
		//String[] countries = new String[] { "Austria", "Bulgaria", "Denmark", "Greece" };
		int[] years = new int[] { 2003, 2004, 2005, 2006, 2007, 2008 };
		
		int[][] values = new int[][] { 
				{ 1336060, 1538156, 1576579, 1600652, 1968113, 1901067 },	// "Austria"
				{ 400361, 366849, 440514, 434552, 393032, 517206 }, 		//  "Bulgaria"
				{ 1001582, 1119450, 993360, 1004163, 979198, 916965 },
				{ 997974, 941795, 930593, 897127, 1080887, 1056036 } };

		//new stats WOT
		List<String> listStats = new ArrayList<String>();
		
		///comme countries
		listStats.add("Wr");
//		listStats.add("CtfPoints");
//		listStats.add("DamagePoints");
//		listStats.add("DestroyedPoints");
//		listStats.add("DroppedCtfPoints");
		
//	    for (CommunityAccount commAcc :  listAccount) {
//	    	commAcc.getData().getRatioCtfPoints();
//	    	commAcc.getData().getRatioDamagePoints();
//	    	commAcc.getData().getRatioDestroyedPoints();
//	    	commAcc.getData().getRatioDetectedPoints();
//	    	commAcc.getData().getRatioDroppedCtfPoints();
//	    	
//	    }
		///
		// Prepare the data
		DataTable dataTable = DataTable.create();
		dataTable.addColumn(ColumnType.STRING, "dates");
		
//		for (int i = 0; i < countries.length; i++) {
//			dataTable.addColumn(ColumnType.NUMBER, countries[i]);
//		}
		//
		for (String nameStat: listStats) {
			dataTable.addColumn(ColumnType.NUMBER, nameStat);
		}
		
		//dataTable.addRows(years.length);
		//
		CommunityAccount commAccount = listAccount.get(0);
		String  tabDates[] = new String [100] ;
		//init tabDates
		int i = 0;
		for(String date :commAccount.listDates) {
			tabDates[i] = date;
			i++;
		}
		
		Integer [] tabBattlesWins = new Integer [100];
		i = 0;
		for(Integer wn :commAccount.listBattlesWins) {
			tabBattlesWins[i] = wn;
			i++;
		}
		
		Integer [] tabBattles = new Integer [100];
		i = 0;
		for(Integer wn :commAccount.listbattles) {
			tabBattles[i] = wn;
			i++;
		}
		
		//
		dataTable.addRows(commAccount.listDates.size());
		
//		for (int i = 0; i < years.length; i++) {
//			dataTable.setValue(i, 0, String.valueOf(years[i]));
//		}
		//
		//on set la légende des abscisses ligne 0 
		i = 0;
		for (String date: commAccount.listDates ) {
			dataTable.setValue(i, 0, String.valueOf(date));
			i++;
		}
		//
//		for (int col = 0; col < values.length; col++) {
//			for (int row = 0; row < values[col].length; row++) {
		
		
//				dataTable.setValue(row, col + 1, values[col][row]);
//			}
//		}
		//
		for (int col = 0; col < commAccount.listDates.size(); col++) {
			for (int row = 0; row < listStats.size(); row++) {
				int bw = tabBattlesWins[col] ;
				int b = tabBattles[col] ;
				double db =  (double)bw/(double)b ;
				int intdb = (int) (db * 100);
				db = (double) intdb / (double)100 ;
				dataTable.setValue(row, col + 1, db);
			}
		}
		
		// Set options
		LineChartOptions options = LineChartOptions.create();
		options.setBackgroundColor("#f0f0f0");
		options.setFontName("Tahoma");
		options.setTitle("Wr");
		options.setHAxis(HAxis.create("dates"));
		options.setVAxis(VAxis.create("wr"));

		// Draw the chart
		chart.draw(dataTable, options);
	}
}