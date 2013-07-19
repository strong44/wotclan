package com.wot.server;

/** Google+ activity object. */
public class ActivityObject {

  /** HTML-formatted content. */

  private String content;

  public String getContent() {
    return content;
  }

  /** People who +1'd this activity. */

  private PlusOners plusoners;

  public PlusOners getPlusOners() {
    return plusoners;
  }
}
