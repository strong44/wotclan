package com.wot.server;

import java.util.List;
import com.google.gson.Gson;

/** Feed of Google+ activities. */
public class ActivityFeed {

  /** List of Google+ activities. */

  private List<Activity> items;

  public List<Activity> getActivities() {
    return items;
  }
  
  
  
  
  public static void main(String[] args) {
	  
	  
	  String toto = "{ \"items\": [  {   \"id\": \"z13lwnljpxjgt5wn222hcvzimtebslkul\",   \"url\": \"url2\",   \"object\": {    \"content\": \"aa\",    \"plusoners\": { ";
	  


	  
	  
	  
      String json = "{\"items\": ["+
                      				"{\"id\": \"z11113lwnljpxjgt5wn222hcvzimtebslkul\","+
                      					"\"url\": \"http\","+
                      					"\"myobject\": {"+
                      										"\"content\": \"blabla\","+
                      										"\"plusoners\": {"+ "\"totalItems\": 1"+
                      													   "}"+
                      								  "}" +
                      				"}"+ 
                      			"]" + 
                      "}" ;
   
//  ,
//  
//  {
//   "id": "z13rtboyqt2sit45o04cdp3jxuf5cz2a3e4",
//   "url": "https://plus.google.com/116899029375914044550/posts/X8W8m9Hk5rE",
//   "object": {
//    "content": "CNN Heroes shines a spotlight on everyday people changing the world. Hear the top ten heroes&#39; inspiring stories by tuning in to the CNN broadcast of &quot;CNN Heroes: An All-Star Tribute&quot; on Sunday, December 11, at 8pm ET/5 pm PT with host \u003cspan class=\"proflinkWrapper\"\u003e\u003cspan class=\"proflinkPrefix\"\u003e+\u003c/span\u003e\u003ca href=\"https://plus.google.com/106168900754103197479\" class=\"proflink\" oid=\"106168900754103197479\"\u003eAnderson Cooper 360\u003c/a\u003e\u003c/span\u003e, and donate to their causes online in a few simple steps with Google Wallet (formerly known as Google Checkout): \u003ca href=\"http://www.google.com/landing/cnnheroes/2011/\" \u003ehttp://www.google.com/landing/cnnheroes/2011/\u003c/a\u003e.",
//    "plusoners": {
//     "totalItems": 21
//    }
//   }
//  },
//  {
//   "id": "z13wtpwpqvihhzeys04cdp3jxuf5cz2a3e4",
//   "url": "https://plus.google.com/116899029375914044550/posts/dBnaybdLgzU",
//   "object": {
//    "content": "Today we hosted one of our Big Tent events in The Hague. \u003cspan class=\"proflinkWrapper\"\u003e\u003cspan class=\"proflinkPrefix\"\u003e+\u003c/span\u003e\u003ca href=\"https://plus.google.com/104233435224873922474\" class=\"proflink\" oid=\"104233435224873922474\"\u003eEric Schmidt\u003c/a\u003e\u003c/span\u003e, Dutch Foreign Minister Uri Rosenthal, U.S. Secretary of State Hillary Clinton and many others came together to discuss free expression and the Internet. The Hague is our third Big Tent, a place where we bring together various viewpoints to discuss essential topics to the future of the Internet. Read more on the Official Google Blog here: \u003ca href=\"http://goo.gl/d9cSe\" \u003ehttp://goo.gl/d9cSe\u003c/a\u003e, and watch the video below for highlights from the day.",
//    "plusoners": {
//     "totalItems": 76
//    }
//   }
//  }
// ]
//}";

      Gson gson = new Gson();
      ActivityFeed actFeed = gson.fromJson(json, ActivityFeed.class);

      if (actFeed.getActivities().isEmpty()) {
          System.out.println("No activities found.");
        } else {
          for (Activity activity : actFeed.getActivities()) {
            System.out.println();
            System.out.println("-----------------------------------------------");
            System.out.println("HTML Content: " + activity.getActivityObject().getContent());
            System.out.println("+1's: " + activity.getActivityObject().getPlusOners().getTotalItems());
            System.out.println("URL: " + activity.getUrl());
            //System.out.println("ID: " + activity.get("id"));
          }
        }
      
  }
  
  
  
}






