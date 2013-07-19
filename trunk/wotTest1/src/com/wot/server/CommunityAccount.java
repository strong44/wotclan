package com.wot.server;

import java.util.List;

import com.google.gson.Gson;

public class CommunityAccount {

	private String status;
	private String status_code;
	
	private DataCommunityAccount data;
	
    public DataCommunityAccount getData() {
		return data;
	}

	public void setData(DataCommunityAccount data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus_code() {
		return status_code;
	}

	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}

	
	
	
	
	public static void main(String[] args) {
		  
		  
		   
		  
		  
	   String json = "";

	      Gson gson = new Gson();
	      CommunityAccount account = gson.fromJson(json,CommunityAccount.class);

//	      if (actFeed.getActivities().isEmpty()) {
//	          System.out.println("No activities found.");
//	        } else {
//	          for (Activity activity : actFeed.getActivities()) {
//	            System.out.println();
//	            System.out.println("-----------------------------------------------");
//	            System.out.println("HTML Content: " + activity.getActivityObject().getContent());
//	            System.out.println("+1's: " + activity.getActivityObject().getPlusOners().getTotalItems());
//	            System.out.println("URL: " + activity.getUrl());
//	            //System.out.println("ID: " + activity.get("id"));
//	          }
//	        }
	      
	      
	  }
	
	
	
	
}

