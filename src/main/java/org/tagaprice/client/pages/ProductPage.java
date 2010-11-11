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
 * Filename: ProductWidget.java
 * Date: 19.05.2010
*/
package org.tagaprice.client.pages;

import java.util.ArrayList;
import java.util.HashMap;

import org.tagaprice.client.RPCHandlerManager;
import org.tagaprice.client.ImageBundle;
import org.tagaprice.client.TaPManager;
import org.tagaprice.client.propertyhandler.DefaultPropertyHandler;
import org.tagaprice.client.propertyhandler.IPropertyHandler;
import org.tagaprice.client.propertyhandler.ListPropertyHandler;
import org.tagaprice.client.propertyhandler.PropertyChangeHandler;
import org.tagaprice.client.widgets.InfoBoxWidget;
import org.tagaprice.client.widgets.MorphWidget;
import org.tagaprice.client.widgets.MorphWidgetInfoHandler;
import org.tagaprice.client.widgets.PriceMapWidget;
import org.tagaprice.client.widgets.ProgressWidget;
import org.tagaprice.client.widgets.RatingWidget;
import org.tagaprice.client.widgets.TypeWidget;
import org.tagaprice.client.widgets.TypeWidgetHandler;
import org.tagaprice.client.widgets.InfoBoxWidget.BoxType;
import org.tagaprice.client.widgets.PriceMapWidget.PriceMapType;
import org.tagaprice.shared.PropertyValidator;
import org.tagaprice.shared.SearchResult;
import org.tagaprice.shared.data.Address;
import org.tagaprice.shared.data.ProductData;
import org.tagaprice.shared.data.PropertyData;
import org.tagaprice.shared.data.PropertyGroup;
import org.tagaprice.shared.data.Type;
import org.tagaprice.shared.data.PropertyDefinition.Datatype;
import org.tagaprice.shared.exception.InvalidLoginException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProductPage extends APage {

	private ProductData productData;
	private HashMap<String, ArrayList<PropertyData>> hashProperties = new HashMap<String, ArrayList<PropertyData>>();
	private Type type;
	private VerticalPanel vePa1 = new VerticalPanel();
	private PropertyChangeHandler handler;
	private ArrayList<IPropertyHandler> handlerList = new ArrayList<IPropertyHandler>();
	private InfoBoxWidget bottomInfo = new InfoBoxWidget();
	private PriceMapWidget priceMap;
	private SimplePanel typeWidgetContainer = new SimplePanel();
	private SimplePanel propertyHandlerContainer = new SimplePanel();
	private MorphWidget titleMorph = new MorphWidget("", Datatype.STRING, true);
	
	public ProductPage(ProductData _productData, Type _type) {
		init(vePa1);
		this.productData=_productData;
		
		this.type=_type;
		
		
		
		//Move PropertyData to hashPropertyData
		for(PropertyData pd:this.productData.getProperties()){
			if(hashProperties.get(pd.getName())==null){
				hashProperties.put(pd.getName(), new ArrayList<PropertyData>());
			}			
			hashProperties.get(pd.getName()).add(pd);
		}
		
		//style
		vePa1.setWidth("100%");
		
		//Header
		HorizontalPanel hoPa1 = new HorizontalPanel();
		hoPa1.setWidth("100%");
		vePa1.add(titleMorph);		
		titleMorph.setText(productData.getTitle());
		titleMorph.setWidth("100%");
		vePa1.add(hoPa1);
		ProgressWidget progressWidget = new ProgressWidget(new Image(ImageBundle.INSTANCE.productPriview()), productData.getProgress());
		hoPa1.add(progressWidget);
		
		VerticalPanel vePa2 = new VerticalPanel();
		vePa2.setWidth("100%");
		hoPa1.add(vePa2);
		hoPa1.setCellWidth(vePa2, "100%");
		
		//Type
		vePa2.add(typeWidgetContainer);	
		drawTypeWidget();

		
		//Rating
		vePa2.add(new RatingWidget(this.productData.getRating(), false));
		
		
		//Listener
		handler=new PropertyChangeHandler() {
			
			@Override
			public void onSuccess() {
				showSave();				
			}
			
			@Override
			public void onError() {
				// TODO Auto-generated method stub
				
			}
		};
		
		
		titleMorph.addMorphWidgetErrorHandler(new MorphWidgetInfoHandler() {
			
			@Override
			public void onSuccess(Datatype errorType) {
				
				if(!productData.getTitle().equals(titleMorph.getText())){
					productData.setTitle(titleMorph.getText());
					showSave();				
				}
			}
			
			@Override
			public void onError(Datatype errorType) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onEmpty() {
				// TODO Auto-generated method stub
			}
		});
		
		
		//Add Price
		if(productData.getId()!=null){
			final SimplePanel priceMapContaier = new SimplePanel();
			priceMapContaier.setWidth("100%");
			vePa1.add(priceMapContaier);
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					priceMap = new PriceMapWidget(productData.getId(),PriceMapType.PRODUCT);
					priceMapContaier.setWidget(priceMap);
				}
				
				@Override
				public void onFailure(Throwable reason) {
					showInfo("Download Error at PriceWidget", BoxType.WARNINGBOX);
					
				}
			});
			
			
		}
		
		//Create and Register Handler
		vePa1.add(propertyHandlerContainer);
		propertyHandlerContainer.setWidth("100%");
		registerHandler();
		
		
		vePa1.add(bottomInfo);
	}
	
	private void drawTypeWidget(){
		typeWidgetContainer.setWidget(new TypeWidget(type, new TypeWidgetHandler() {			
			@Override
			public void onChange(Type newType) {	
				
				
				//Get type and set type
				RPCHandlerManager.getTypeHandler().get(newType, new AsyncCallback<Type>() {
					@Override
					public void onFailure(Throwable caught) {
						showInfo("ProductPage getTypeError", BoxType.WARNINGBOX);
					}

					@Override
					public void onSuccess(Type result) {
						type=result;
						drawTypeWidget();	
						registerHandler();
						showSave();	
					}

				});
				
				
				
			}
		}));
	}
	
	
	private void registerHandler(){
		handlerList.clear();
		
		for(String ks:hashProperties.keySet()){
			for(PropertyData pd:hashProperties.get(ks)){
				pd.setRead(false);
			}
		}
		
		
		VerticalPanel hVePa = new VerticalPanel();
		hVePa.setWidth("100%");
		
		
		//Add Properties
		for(PropertyGroup pg:this.type.getPropertyGroups()){
			
			if(pg.getType().equals(PropertyGroup.GroupType.NUTRITIONFACTS)){
				/*
				NutritionFactsPropertyHandler temp = new NutritionFactsPropertyHandler(hashProperties, pg, handler);
				handlerList.add(temp);
				hVePa.add(temp);
				*/
			}else if (pg.getType().equals(PropertyGroup.GroupType.LIST)){				
				ListPropertyHandler temp= new ListPropertyHandler(hashProperties, pg, handler);
				handlerList.add(temp);
				hVePa.add(temp);
				
			}	
			
		}
		
		
		DefaultPropertyHandler defH = new DefaultPropertyHandler(hashProperties, handler);
		handlerList.add(defH);
		hVePa.add(defH);
		
		
		
		propertyHandlerContainer.setWidget(hVePa);
	}
	
	private void showSave(){
		if(TaPManager.getInstance().isLoggedIn()!=null){
			showSaveLogin();

		}else{
			showSaveNotLogin();
		}

	}
	
	private void showSaveLogin(){
		HorizontalPanel bottomInfoHoPa = new HorizontalPanel();
		bottomInfoHoPa.setWidth("100%");
		Button topAbort=new Button("Abort", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(productData.getId()==null){
					History.newItem("home/");
				}else{
					TaPManager.getInstance().showProductPage(productData.getId());
				}
				
				
			}
		});
		final Button topSave=new Button("Save");	
		topSave.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				topSave.setText("Saving...");
				
				
				productData.setProperties(hashToPropertyList(hashProperties));
				productData.setTypeId(new Long(type.getId()));
				
				
				//Validate Data
				if(PropertyValidator.isValid(type, productData.getProperties())){				
					try {
						RPCHandlerManager.getProductHandler().save(
								productData, 
								new AsyncCallback<ProductData>() {
							
							@Override
							public void onSuccess(ProductData result) {
								bottomInfo.setVisible(false);
								topSave.setText("Save");
								if(productData.getId()==null){
									History.newItem("product/get&id="+result.getId());
								}else{
									productData=result;
								}
								
								
							}
							
							@Override
							public void onFailure(Throwable caught) {
								showInfo("SaveFail: "+caught, BoxType.WARNINGBOX);
								
							}
						});
					} catch (IllegalArgumentException e) {
						showInfo("IllegalArgumentException: "+e, BoxType.WARNINGBOX);

					} catch (InvalidLoginException e) {
						showInfo("InvalidLoginException: "+e, BoxType.WARNINGBOX);
					}	
				}else{
					showInfo("SaveFail: Invalide Data", BoxType.WARNINGBOX);
				}
				
			}
		});
		bottomInfoHoPa.add(topAbort);
		bottomInfoHoPa.add(topSave);	
		bottomInfoHoPa.setCellWidth(topAbort, "100%");
		bottomInfoHoPa.setCellHorizontalAlignment(topAbort, HasHorizontalAlignment.ALIGN_RIGHT);
		bottomInfoHoPa.setCellHorizontalAlignment(topSave, HasHorizontalAlignment.ALIGN_RIGHT);				
		bottomInfoHoPa.setCellVerticalAlignment(topAbort, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomInfoHoPa.setCellVerticalAlignment(topSave, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomInfo.showInfo(bottomInfoHoPa, BoxType.WARNINGBOX);
	}
	
	private void showSaveNotLogin(){
		HorizontalPanel bottomInfoHoPa = new HorizontalPanel();
		bottomInfoHoPa.setWidth("100%");
		//bottomInfoHoPa.setHeight("100%");
		Button topAbort=new Button("Abort", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(productData.getId()==null){
					History.newItem("home/");
				}else{
					TaPManager.getInstance().showProductPage(productData.getId());
				}
				
				
			}
		});
		Label pleaseLoginLabel = new Label("Please login for saving!");
		bottomInfoHoPa.add(pleaseLoginLabel);
		bottomInfoHoPa.add(topAbort);
		bottomInfoHoPa.setCellWidth(pleaseLoginLabel, "100%");
		bottomInfoHoPa.setCellHorizontalAlignment(topAbort, HasHorizontalAlignment.ALIGN_RIGHT);
		bottomInfoHoPa.setCellVerticalAlignment(topAbort, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomInfo.showInfo(bottomInfoHoPa, BoxType.WARNINGBOX);
	}
	
	
	private SearchResult<PropertyData> hashToPropertyList(HashMap<String, ArrayList<PropertyData>> hashProperties){
		SearchResult<PropertyData> newList = new SearchResult<PropertyData>();
		
		for(String ks:hashProperties.keySet()){
			for(PropertyData pd:hashProperties.get(ks)){
				newList.add(pd);
				//System.out.println(pd.getName()+": "+pd.getValue());
			}
		}
		return newList;
	}

	@Override
	public void setAddress(Address address) {
		// TODO Auto-generated method stub
		priceMap.setAddress(address);
	}


	
}
