package mvw.server.data.service;

import java.util.Collection;

import mvw.server.data.entity.Cookie;

public interface CookieService {
	public Collection<Cookie> findCookies(long userId);
}
