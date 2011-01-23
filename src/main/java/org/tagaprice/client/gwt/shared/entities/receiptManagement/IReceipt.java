package org.tagaprice.client.gwt.shared.entities.receiptManagement;

import java.sql.Date;
import java.util.Set;

import org.tagaprice.client.gwt.shared.entities.dump.User;
import org.tagaprice.client.gwt.shared.entities.shopmanagement.Shop;
import org.tagaprice.core.entities.ReceiptEntry;



public interface IReceipt {

	public abstract Long getId();

	public abstract void setId(Long id);

	public abstract Date getDate();

	public abstract void setDate(Date date);

	public abstract Shop getShop();

	public abstract void setShop(Shop shop);

	public abstract User getUser();

	public abstract void setUser(User user);

	public abstract Set<ReceiptEntry> getReceiptEntries();

	public abstract void setReceiptEntries(Set<ReceiptEntry> receiptEntries);

}