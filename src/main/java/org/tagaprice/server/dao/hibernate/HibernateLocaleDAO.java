package org.tagaprice.server.dao.hibernate;

import org.hibernate.SessionFactory;
import org.tagaprice.core.entities.Locale;
import org.tagaprice.server.dao.interfaces.ILocaleDAO;

/** TODO proper setup through spring beans config */
public class HibernateLocaleDAO implements ILocaleDAO {

	/** TODO @Autowired */
	private SessionFactory _sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		_sessionFactory = sessionFactory;
	}

	@Override
	public Locale save(Locale locale) {
		_sessionFactory.getCurrentSession().save(locale);
		return locale;
	}

	@Override
	public Locale getById(int id) {
		return (Locale) _sessionFactory.getCurrentSession().load(Locale.class, id);
	}

}
