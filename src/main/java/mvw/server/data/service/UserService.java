package mvw.server.data.service;

import java.util.Optional;

import mvw.server.data.entity.User;

public interface UserService {
	public Optional<User> findUser(long userId);
}
