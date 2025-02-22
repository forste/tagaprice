package org.tagaprice.client.gwt.client.generics.widgets;

import org.tagaprice.client.gwt.shared.entities.dump.IQuantity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;

/**
 * This is an QuantitySelecter wrapper class. This class implements the right screen design.
 * Is is also possible to use this class in UIBuilder
 * 
 */
public class QuantitySelecter extends Composite implements IQuantitySelecter {

	IQuantitySelecter quantitySelecter = GWT.create(IQuantitySelecter.class);

	public QuantitySelecter() {
		initWidget(quantitySelecter.asWidget());
	}

	@Override
	public void setQuantity(IQuantity quantity) {
		quantitySelecter.setQuantity(quantity);
	}

	@Override
	public IQuantity getQuantity() {
		return quantitySelecter.getQuantity();
	}

}
