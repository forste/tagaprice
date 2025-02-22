package org.tagaprice.server.dao.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.tagaprice.core.api.ServerException;
import org.tagaprice.core.entities.*;
import org.tagaprice.server.dao.interfaces.IShopDAO;

@SuppressWarnings("unchecked")
public class HibernateShopDAO implements IShopDAO {

	private SessionFactory _sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		_sessionFactory = sessionFactory;
	}

	@Override
	public Shop getById(long id) {
		Shop shop = (Shop) _sessionFactory.getCurrentSession().get(Shop.class, id);
		if(shop == null)
			return null;

		shop.setReceiptEntries(_sessionFactory
				.getCurrentSession()
				.createQuery(
						"select entry from ReceiptEntry entry, Receipt receipt where entry.receiptId = receipt.id and receipt.shop.shopId = "
						+ id).list());
		// TODO only add newest receiptEntry for each product
		return shop;
	}

	@Override
	public List<BasicShop> getByTitle(String title) {
		Criteria crit = _sessionFactory.getCurrentSession().createCriteria(BasicShop.class);
		crit.add(Restrictions.like("title", title, MatchMode.EXACT));
		crit.setMaxResults(10);

		return crit.list();
	}

	@Override
	public List<BasicShop> getByTitleFuzzy(String title) {
		Criteria crit = _sessionFactory.getCurrentSession().createCriteria(BasicShop.class);
		crit.add(Restrictions.ilike("title", title, MatchMode.ANYWHERE));
		crit.setMaxResults(10);

		return crit.list();
	}

	@Override
	public List<BasicShop> getAll() {
		return _sessionFactory.getCurrentSession().createQuery("from BasicShop").list();
	}

	@Override
	public Shop save(Shop shop) throws ServerException {
		if(shop.getTitle() == null)
			throw new ServerException("title is null");

		Session session = _sessionFactory.getCurrentSession();
		session.saveOrUpdate(shop);
		return shop;
	}
}
