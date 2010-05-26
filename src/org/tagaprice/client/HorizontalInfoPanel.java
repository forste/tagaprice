/*
 * Copyright 2010 TagAPrice.org
 * 
 * Licensed under the Creative Commons License. You may not
 * use this file except in compliance with the License. 
 *
 * http://creativecommons.org/licenses/by-nc/3.0/
*/

/**
 * Project: TagAPrice
 * Filename: VerticalInfoPanel.java
 * Date: 26.05.2010
*/
package org.tagaprice.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HorizontalInfoPanel extends Composite {
	VerticalPanel vePa1 = new VerticalPanel();
	HorizontalPanel hoPa1 = new HorizontalPanel();
	Label info = new Label();
	
	public HorizontalInfoPanel() {
		vePa1.setWidth("100%");
		hoPa1.setWidth("100%");
		info.setWidth("100%");
		info.setStyleName("Warning");
		
		vePa1.add(hoPa1);
		vePa1.add(info);
		info.setVisible(false);
		
		
		initWidget(vePa1);
	}
	
	public void add(Widget w){
		hoPa1.add(w);
	}
	
	public void showInfo(String text){
		info.setText(text);
		info.setVisible(true);
	}
	
	public void showInfo(boolean visible){
		info.setVisible(visible);
	}
	
	public HorizontalPanel getPanel(){
		return hoPa1;
	}
}
