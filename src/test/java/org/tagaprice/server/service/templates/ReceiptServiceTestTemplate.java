package org.tagaprice.server.service.templates;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.tagaprice.core.api.IReceiptService;
import org.tagaprice.core.api.ServerException;
import org.tagaprice.core.entities.Account;
import org.tagaprice.core.entities.Receipt;
import org.tagaprice.core.entities.ReceiptEntry;
import org.tagaprice.server.dao.helper.HibernateSaveEntityCreator;
import org.tagaprice.server.dao.interfaces.IReceiptDAO;
import org.tagaprice.server.service.helper.EntityCreator;

public abstract class ReceiptServiceTestTemplate extends AbstractJUnit4SpringContextTests {
	protected IReceiptService _receiptService;
	protected IReceiptDAO _receiptDao;

	@Test
	public void getReceiptEntriesByProductIdAndRev_passProductRevisionIdsNotStored_shouldReturnEmptyList()
	throws ServerException {
		Long productId = 1L;
		Integer productRevision = 2;

		getReceiptEntriesByProductIdAndRev_passProductRevisionIdsNotStored_shouldReturnEmptyList_DAO_SETUP(productId,
				productRevision);


		List<ReceiptEntry> receiptEntries = _receiptService.getReceiptEntriesByProductIdAndRev(productId,
				productRevision);

		assertThat(receiptEntries.isEmpty(), is(true));
	}

	protected abstract void getReceiptEntriesByProductIdAndRev_passProductRevisionIdsNotStored_shouldReturnEmptyList_DAO_SETUP(
			Long productId, Integer productRevision);



	@Test
	public void getReceiptEntriesByProductIdAndRev_shouldReturnEntriesAsGivenByDaoButOnlyUnique()
	throws ServerException {
		Long productId = 1L;
		Integer productRevision = 1;

		ArrayList<ReceiptEntry> entries = new ArrayList<ReceiptEntry>();
		ReceiptEntry entry1 = EntityCreator.createReceiptEntry(
				EntityCreator.createBasicReceipt(1L, EntityCreator.createBasicShop(1L, "testShop")),
				EntityCreator.createProductRevision(productId, productRevision, "coke"), 1, 10);
		entries.add(entry1);
		ReceiptEntry entry2 = EntityCreator.createReceiptEntry(
				EntityCreator.createBasicReceipt(2L, EntityCreator.createBasicShop(2L, "otherTestShopX")),
				EntityCreator.createProductRevision(productId, productRevision, "coke"), 3, 8);
		entries.add(entry2);

		getReceiptEntriesByProductIdAndRev_shouldReturnEntriesAsGivenByDaoButOnlyUnique_DAO_SETUP(productId,
				productRevision, entries);

		List<ReceiptEntry> actualReceiptEntries = _receiptService.getReceiptEntriesByProductIdAndRev(productId,
				productRevision);

		List<ReceiptEntry> expectedEntries = new ArrayList<ReceiptEntry>();
		expectedEntries.add(EntityCreator.createReceiptEntry(
				EntityCreator.createBasicReceipt(1L, EntityCreator.createBasicShop(1L, "testShop")),
				EntityCreator.createProductRevision(productId, productRevision, "coke"), 1, 10));
		expectedEntries.add(EntityCreator.createReceiptEntry(
				EntityCreator.createBasicReceipt(2L, EntityCreator.createBasicShop(2L, "otherTestShopX")),
				EntityCreator.createProductRevision(productId, productRevision, "coke"), 3, 8));


		assertThat(actualReceiptEntries, is(expectedEntries));
	}

	protected abstract void getReceiptEntriesByProductIdAndRev_shouldReturnEntriesAsGivenByDaoButOnlyUnique_DAO_SETUP(
			Long productId, Integer productRevision, List<ReceiptEntry> entries);


	//
	// save tests
	//


	@Test
	public void saveEmptyReceipt_shouldSaveReceipt() throws Exception {
		long receiptId = 3L; // TODO this should be set to null when id in receipt is set to generatedValue
		long shopId = 1L;
		Date createdAt = EntityCreator.getDefaultDate();
		Account creator = HibernateSaveEntityCreator.createAccount(1L);

		Receipt receiptToSave = HibernateSaveEntityCreator.createReceipt(receiptId, shopId, createdAt, creator);

		saveEmptyReceipt_shouldSaveReceipt_DAO_SETUP(receiptToSave);

		Receipt actual = _receiptService.save(receiptToSave);

		Receipt expected = HibernateSaveEntityCreator.createReceipt(receiptId, shopId, createdAt, creator);

		assertThat(actual, is(expected));

		saveEmptyReceipt_shouldSaveReceipt_DB_ASSERTS(receiptToSave);
	}

	protected abstract void saveEmptyReceipt_shouldSaveReceipt_DAO_SETUP(Receipt receiptToSave);

	protected abstract void saveEmptyReceipt_shouldSaveReceipt_DB_ASSERTS(Receipt receiptToSave);

	@Test
	public void saveReceiptWithReceiptEntries_shouldSaveReceipt() throws Exception {
		long receiptId = 3L;
		long shopId = 1L;
		Date createdAt = EntityCreator.getDefaultDate();
		Account creator = HibernateSaveEntityCreator.createAccount(1L);


		Set<ReceiptEntry> receiptEntries = new HashSet<ReceiptEntry>();
		long prod1Id = 1;
		int prod1RevNr = 1;
		receiptEntries.add(HibernateSaveEntityCreator
				.createReceiptEntry(receiptId, shopId, prod1Id, prod1RevNr, 1, 200));

		long prod2Id = 2;
		int prod2RevNr = 2;
		receiptEntries.add(HibernateSaveEntityCreator.createReceiptEntry(receiptId, shopId, prod2Id, prod2RevNr, 5,
				1000));


		Receipt receiptToSave = HibernateSaveEntityCreator.createReceipt(receiptId, shopId, createdAt, creator,
				receiptEntries);

		saveReceiptWithReceiptEntries_shouldSaveReceipt_DAO_SETUP(receiptToSave);

		Receipt actual = _receiptService.save(receiptToSave);


		Receipt expected = HibernateSaveEntityCreator.createReceipt(receiptId, shopId, createdAt, creator,
				receiptEntries);

		assertThat(actual, is(expected));
		saveReceiptWithReceiptEntries_shouldSaveReceipt_DB_ASSERTS(receiptToSave, receiptEntries);
	}

	protected abstract void saveReceiptWithReceiptEntries_shouldSaveReceipt_DAO_SETUP(Receipt receipt);

	protected abstract void saveReceiptWithReceiptEntries_shouldSaveReceipt_DB_ASSERTS(Receipt receiptToSave,
			Set<ReceiptEntry> receiptEntries);

}
