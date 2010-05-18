/*
 * Copyright 2010 TagAPrice.org
 * 
 * Licensed under the Creative Commons License. You may not
 * use this file except in compliance with the License. 
 *
 * http://creativecommons.org/licenses/by-nc/3.0/
*/

/**
 * Project: TagAPriceUI
 * Filename: ShopPreview.java
 * Date: 15.05.2010
*/
package org.tagaprice.client;


import org.tagaprice.shared.ShopData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Displays the most important properties of a Shop.
 * Properties: title, rating, progress, address
 *
 */
public class ShopPreview extends Composite {
	interface MyUiBinder extends UiBinder<Widget, ShopPreview>{}
	private MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	private ShopData shopData;
	private boolean editable;
	private RatingWidget ratingWidget;
	
	@UiField SimplePanel ratingPanel;
	@UiField HorizontalPanel HoPa1;
	@UiField HorizontalPanel HoPa2;
	@UiField Label name;
	@UiField Label street;
	@UiField Label city;
	@UiField SimplePanel logoPanel;
	@UiField VerticalPanel VePa1;
	@UiField VerticalPanel VePa2;
	
	/**
	 * 
	 * @param productData 
	 * @param editable 
	 */
	public ShopPreview(ShopData shopData, boolean editable) {
		this.shopData=shopData;
		this.editable=editable;
		initWidget(uiBinder.createAndBindUi(this));
		

		//HoPa1
		HoPa1.setWidth("100%");
		HoPa1.setStyleName("ShopPreview");
		
		HoPa1.setCellWidth(logoPanel, "40px");
		//HoPa1.setBorderWidth(1);
		
		//VePa1
		VePa1.setSize("100%", "40px");

		
		
		//HoPa2
		HoPa2.setWidth("100%");		
		ratingWidget=new RatingWidget(shopData.getRating(), this.editable);
		ratingPanel.setWidget(ratingWidget);
		HoPa2.setCellWidth(ratingPanel, "80px");
		
		//VePa2
		HoPa2.setCellHorizontalAlignment(VePa2, HasHorizontalAlignment.ALIGN_RIGHT);
		street.setStyleName("ShopPreview-Street");
		city.setStyleName("ShopPreview-Street");
		
		//Street
		if(this.shopData.getStreet()!=null)
			street.setText(this.shopData.getStreet());
		
		//city
		if(this.shopData.getCity()!=null)
			city.setText(this.shopData.getCity());
		
		
		logoPanel.add(new ProgressWidget(new Image(MyResources.INSTANCE.productPriview()), 50));
		logoPanel.setHeight(MyResources.INSTANCE.productPriview().getHeight()+"px");
		
		
		name.setText(this.shopData.getName());

	}
	

	/**
	 * Return current ProductData
	 * @return 
	 */
	public ShopData getshopData(){
		if(editable){
			this.shopData.setRating(ratingWidget.getRating());
		}
		
		return this.shopData;
	}
	
	/**
	 * 
	 * @return Is ProductPreview editable
	 */
	public boolean isEditable(){
		return editable;
	}

	
}
