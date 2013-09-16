package com.wot.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class Helloworld extends Composite implements HasText {

	private static HelloworldUiBinder uiBinder = GWT.create(HelloworldUiBinder.class);

	interface HelloworldUiBinder extends UiBinder<Widget, Helloworld> {
	}

	public Helloworld() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Button button;

	public Helloworld(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);
	}

	@UiHandler("button")
	void onClick(ClickEvent e) {
		Window.alert("Hello!");
	}

	public void setText(String text) {
		button.setText(text);
	}

	public String getText() {
		return button.getText();
	}
	
	/*
	 * 
	 */
	public static void main(String args[]) {
		System.out.println("main");
		Helloworld helloWorld = new Helloworld();
		helloWorld.getElement();
		// Don't forget, this is DOM only; will not work with GWT widgets
		//Document.get().getBody().appendChild(helloWorld.getElement());
		//helloWorld.setName("World");
	}

}
