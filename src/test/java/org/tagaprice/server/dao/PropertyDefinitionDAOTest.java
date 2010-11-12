package org.tagaprice.server.dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tagaprice.server.DBConnection;
import org.tagaprice.shared.data.Account;
import org.tagaprice.shared.data.PropertyDefinition;
import org.tagaprice.shared.data.PropertyDefinition.Datatype;

public class PropertyDefinitionDAOTest {
	private PropertyDefinitionDAO dao;
	private DBConnection db;
	private int localeId;
	private long uid;

	@Before
	public void setUp() throws Exception {
		db = new EntityDAOTest.TestDBConnection();
		dao = new PropertyDefinitionDAO(db);
		localeId = new LocaleDAO(db).get("English").getId();
		Account a = new Account("testAccount", localeId, "foo@test.invalid", null);
		new AccountDAO(db).save(a);
		uid = a.getId();
	}

	@After
	public void tearDown() throws Exception {
		db.forceRollback();
	}

	@Test
	public void testCreate() throws Exception {
		PropertyDefinition pdef = new PropertyDefinition("testWeight", "Test property named 'weight'", localeId, uid, Datatype.STRING, 5, 27, null, true);
		dao.save(pdef);
		PropertyDefinition pdef2 = new PropertyDefinition(pdef.getId());
		dao.get(pdef2);
		assertEquals(pdef, pdef2);
	}

}
