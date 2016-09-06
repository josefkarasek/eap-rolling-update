package org.jboss.as.quickstarts.greeter.domain;

import java.util.Optional;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

@Decorator
public abstract class CacheDecorator implements UserDao {

	@Inject
	@Delegate
	@Any
	private UserDao userDao;

	@Inject
    private UserCache cache;

	public CacheDecorator() {
	}

	@Override
	public User getForUsername(final String username) {
		return Optional.ofNullable(cache.getUser(username)).orElseGet(() -> {
			User u = userDao.getForUsername(username);
			if (u != null) {
				cache.storeUser(u);
			}
			return u;
		});
	}

	@Override
	public void createUser(final User user) {
		userDao.createUser(user);
		cache.storeUser(user);
	}

}
