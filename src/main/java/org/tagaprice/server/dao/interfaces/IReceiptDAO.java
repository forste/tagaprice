package org.tagaprice.server.dao.interfaces;

import java.util.List;

import org.tagaprice.core.entities.Receipt;
import org.tagaprice.core.entities.ReceiptEntry;

public interface IReceiptDAO {

	/**
	 * 
	 * @param productId id of the product to get the {@link ReceiptEntry}s for.
	 * @param productRevision revision number of the product.
	 * @return all {@link ReceiptEntry}s for the specified product revision.
	 */
	List<ReceiptEntry> getReceiptEntriesByProductIdAndRev(Long productId, Integer productRevision);

	/**
	 * Save a {@link Receipt} and all its {@link ReceiptEntry}s.
	 */
	Receipt save(Receipt receipt);

}
