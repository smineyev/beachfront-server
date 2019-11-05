package mvw.server.data.service.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import mvw.server.data.entity.Cookie;
import mvw.server.data.service.CookieService;

@Configuration
public class CookieServiceMockImpl implements CookieService {
	
	@Value(value = "${spring.maxSleepTime}")
	private int maxSleepTime;

	public CookieService cookieService() {
		return this;
	}

	@Override
	public Collection<Cookie> findCookies(long userId) {
		Random rnd = new Random();
		try {
			Thread.sleep(rnd.nextInt(maxSleepTime));
			return Arrays.asList(new Cookie[] { 
					new Cookie("cookie1", "v1"), 
					new Cookie("cookie2", "v2") 
			});
		} catch (InterruptedException e) {
			System.out.println("interrupted");
			return new ArrayList<Cookie>(0);
		}
	}

}
