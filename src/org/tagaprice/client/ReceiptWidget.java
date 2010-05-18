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
 * Filename: ReceiptWidget.java
 * Date: 15.05.2010
*/
package org.tagaprice.client;


import java.util.Iterator;

import org.tagaprice.shared.ProductData;
import org.tagaprice.shared.ReceiptData;
import org.tagaprice.shared.ShopData;
import org.tagaprice.shared.TaPManagerImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Displays editable receipt including shop and product search.
 *
 */
public class ReceiptWidget extends Composite {
	interface MyUiBinder extends UiBinder<Widget, ReceiptWidget>{}
	private MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	
	boolean isEditable=true;
	MorphWidget title = new MorphWidget("Default title", isEditable);
	int bill=0;
	ChangeHandler priceChangeHandler; 
	ReceiptData receiptData;
	
	
	@UiField HorizontalPanel HoPa1;
	@UiField HorizontalPanel HoPa2;
	@UiField VerticalPanel VePa1;
	@UiField DateWidget date;
	@UiField SimplePanel titleContainer;
	@UiField SimplePanel shopChooser;
	@UiField SelectiveVerticalPanel productContainer;
	@UiField Label price;
	@UiField Button save;
	
	/**
	 * 
	 * @param receiptData
	 */
	public ReceiptWidget(ReceiptData receiptData, boolean editable){
		this();
		this.receiptData=receiptData;
		isEditable=editable;
		title.setText(receiptData.getName());
		date.setDate(receiptData.getDate());
		
		if(receiptData.getShopData()!=null){
			setShop(receiptData.getShopData());
		}
		
		Iterator<ProductData> myIter = receiptData.getProductData().iterator();
		
		while(myIter.hasNext()){
			addProduct(myIter.next());
		}
		

		refreshPrice();	
	}
	
	/**
	 * 
	 */
	public ReceiptWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		
		//Style
		//VePa1
		VePa1.setWidth("100%");
		
		//Hopa1
		HoPa1.setWidth("100%");
		titleContainer.setWidget(title);
		
		HoPa1.setCellWidth(date, "50px");
		
		//shopChoolser
		
		//HoPa2
		VePa1.setCellHorizontalAlignment(HoPa2, HasHorizontalAlignment.ALIGN_RIGHT);

		
		//ProductsHandler
		priceChangeHandler = new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				refreshPrice();		
			}
		};
		
		//Products		
		productContainer.addSelectiveVerticalPanelHandler(new SelectiveVerticalPanelHandler() {
			
			@Override
			public void onClick(Widget widget, int index) {
				productContainer.removeWidget(index);
				refreshPrice();
				
			}
		});
		
		
		//Refresh the price
		refreshPrice();		
		
		//Save
		save.setStyleName("Awesome");
		save.setWidth("100%");
		save.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				receiptData.setId(0);//Now a new receipt will be created.
				TaPManagerImpl.getInstance().saveReceipt(getReceiptData());
				
			}
		});
		
	}
	
	/**
	 * 
	 */
	private void refreshPrice(){
		bill=0;
		for(int i=0;i<productContainer.getWidgetCount();i++){
			bill+=((ProductPreview)productContainer.getWidget(i)).getProductData().getPrice();
		}
		
		price.setText((bill/100.00)+"");
	}
	
	/**
	 * 
	 * @param shop
	 */
	public void setShop(ShopData shop){
		shopChooser.setWidget(new ShopPreview(shop,isEditable));
	}
	
	/**
	 * 
	 * @param product
	 */
	public void addProduct(ProductData product){
		productContainer.add(new ProductPreview(product, isEditable,priceChangeHandler));
	}
	
	
	public ReceiptData getReceiptData(){
		//TODO in ReceiptData die akutalisierten anderen Data geben!
		
		return this.receiptData;
	}
	
}
