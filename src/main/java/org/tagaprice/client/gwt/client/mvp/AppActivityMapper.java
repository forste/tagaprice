package org.tagaprice.client.gwt.client.mvp;

import org.tagaprice.client.gwt.client.ClientFactory;
import org.tagaprice.client.gwt.client.features.productmanagement.createProduct.CreateProductActivity;
import org.tagaprice.client.gwt.client.features.productmanagement.createProduct.CreateProductPlace;
import org.tagaprice.client.gwt.client.features.productmanagement.editProduct.*;
import org.tagaprice.client.gwt.client.features.productmanagement.listProducts.*;
import org.tagaprice.client.gwt.shared.logging.*;

import com.google.gwt.activity.shared.*;
import com.google.gwt.place.shared.Place;

/**
 * Maps Places to Activities.
 * 
 */
public class AppActivityMapper implements ActivityMapper {
	MyLogger logger = LoggerFactory.getLogger(AppActivityMapper.class);

	private ClientFactory clientFactory;

	public AppActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		logger.log("I was asked for an activity...");
		// TODO make this gin
		if (place instanceof ListProductsPlace) {
			logger.log("return new ListProductsActivity");
			return new ListProductsActivity((ListProductsPlace) place, this.clientFactory);
		} else if (place instanceof EditProductPlace) {
			logger.log("return new EditProductsActivity");
			return new EditProductActivity((EditProductPlace) place, this.clientFactory);
		} else if (place instanceof CreateProductPlace) {
			logger.log("return new CreateProductActivity");
			return new CreateProductActivity((CreateProductPlace) place, this.clientFactory);
		} else {
			// THIS ELSE IS IMPORTANT TO AVOID FAILURES
			// IF THE PROGRAMER FORGOTT TO RETURN A VALUE
			return null;
		}
	}

}