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
import com.wot.shared.DataPlayerInfos;

public class LineChartExample extends SimplePanel {
	private LineChart chart;
	List<CommunityAccount> listAccount ;
	String stat; 
	
	public LineChartExample(String stat, List<CommunityAccount> listAccount) {
		this.listAccount = listAccount;
		this.stat = stat;
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
		
		
		//new stats WOT
		List<String> listStats = new ArrayList<String>();
		for (CommunityAccount commAcc: listAccount) {
			listStats.add(commAcc.getName());
			
		}
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
		//String  tabDates[] = new String [100] ;
		//init tabDates
		int i = 0;
//		for(String date :commAccount.listDates) {
//			tabDates[i] = date;
//			i++;
//		}
		
		
//		//wn8 directe 
//		Double [] tabWn8= new Double [100];
//		i = 0;
//		for(DataPlayerInfos data :commAccount.listDataPlayerInfos) {
//			tabWn8[i] = data.getStatistics().getAllStatistics().getWn8();
//			i++;
//		}
		//
		//wn8 directe 
//		Double [] tabWr= new Double [100];
//		i = 0;
//		for(DataPlayerInfos data :commAccount.listDataPlayerInfos) {
//			tabWr[i] = data.getStatistics().getAllStatistics().getBattle_avg_performanceCalc()*100;
//			i++;
//		}
		
		
//		Integer [] tabBattlesWins = new Integer [100];
//		i = 0;
//		for(Integer wn :commAccount.listBattlesWins) {
//			tabBattlesWins[i] = wn;
//			i++;
//		}
		
//		Integer [] tabBattles = new Integer [100];
//		i = 0;
//		for(Integer wn :commAccount.listbattles) {
//			tabBattles[i] = wn;
//			i++;
//		}
		
		//
		dataTable.addRows(commAccount.listDates.size());
		
//		for (int i = 0; i < years.length; i++) {
//			dataTable.setValue(i, 0, String.valueOf(years[i]));
//		}
		//
		//on set la légende des abscisses ligne 0 
		i = 0;
		for (String date: commAccount.listDates ) {
			dataTable.setValue(i, 0, String.valueOf(i));
			i++;
		}
		//
//		for (int col = 0; col < values.length; col++) {
//			for (int row = 0; row < values[col].length; row++) {
		
		
//				dataTable.setValue(row, col + 1, values[col][row]);
//			}
//		}
		//
//		for (int col = 0; col < listStats.size() ; col++) {
//			for (int row = 0; row < commAccount.listDates.size(); row++) {
//				int bw = tabBattlesWins[col] ;
//				int b = tabBattles[col] ;
//				double db =  (double)bw/(double)b ;
//				int intdb = (int) (db * 100);
//				db = (double) intdb / (double)100 ;
//				dataTable.setValue(row, col + 1, db);
//			}
//		}
//		
		int col = 0 ;
		for (CommunityAccount commAcc: listAccount) {
			for (int row = 0; row < commAccount.listDates.size(); row++) {
				
				 List<DataPlayerInfos> subList = commAcc.listDataPlayerInfos.subList(row, row+1);
				 if (stat.equalsIgnoreCase("WN8"))
					 dataTable.setValue(row, col + 1, subList.get(0).getStatistics().getAllStatistics().getWn8());
				 
				 if (stat.equalsIgnoreCase("WR"))
					 dataTable.setValue(row, col + 1, subList.get(0).getStatistics().getAllStatistics().getBattle_avg_performanceCalc());
				//dataTable.setValue(row, col + 1, tabWr[col]);
			}
			
			col++;
		}

//		for (int col = 0; col < listStats.size() ; col++) {
//			if (col == 0) //wn8 
//				for (int row = 0; row < commAccount.listDates.size(); row++) {
//	
//					dataTable.setValue(row, col + 1, tabWn8[col]);
//					//dataTable.setValue(row, col + 1, tabWr[col]);
//				}
//			
////			if (col == 1) //Wr
////				for (int row = 0; row < commAccount.listDates.size(); row++) {
////	
////					dataTable.setValue(row, col + 1, tabWr[col]);
////				}
//
//		}

		// Set options
		LineChartOptions options = LineChartOptions.create();
		options.setBackgroundColor("#f0f0f0");
		options.setFontName("Tahoma");
		options.setTitle(stat);
		options.setHAxis(HAxis.create("Il y a xx Jours"));
		options.setVAxis(VAxis.create("wr"));

		// Draw the chart
		chart.draw(dataTable, options);
	}
}