package org.jboss.as.quickstarts.greeter.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class UserCache implements Serializable {
	private static final long serialVersionUID = -5680523852258045038L;
	private final Map<String, User> cache = new HashMap<>();

	public UserCache() {
	}
	
	public User getUser(final String username) {
		return cache.get(username);
	}

	private String getNodeName() {
		return System.getenv("HOSTNAME");
	}
	
	public void storeUser(final User user) {
		user.setCachedAtNode(getNodeName());
		cache.put(user.getUsername(), user);
	}
}
