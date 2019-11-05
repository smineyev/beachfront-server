package mvw.server.data.service.mock;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import mvw.server.data.entity.User;
import mvw.server.data.service.UserService;

@Configuration
public class UserServiceMockImpl implements UserService {

	@Value(value = "${spring.maxSleepTime}")
	private int maxSleepTime;

	public UserService userRepo() {
		return this;
	}
	
	@Override
	public Optional<User> findUser(long userId) {
		Random rnd = new Random();
		try {
			Thread.sleep(rnd.nextInt(maxSleepTime));
			return Optional.of(new User(rnd.nextLong()));
		} catch (InterruptedException e) {
			System.out.println("interrupted");
			return Optional.empty();
		}
	}

}
