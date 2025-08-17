package ru.practicum.shareit.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private Long id;
	private Map<Long, User> users;

	public UserRepositoryImpl() {
		id = 0L;
		users = new HashMap<>();
	}

	@Override
	public User createUser(User user) {
		++id;
		User ans = user.toBuilder().id(id).build();
		users.put(id, ans);
		return ans;
	}

	@Override
	public User updateUser(User user) {
		users.put(user.getId(), user);
		return user;
	}

	@Override
	public Optional<User> findUserById(Long id) {
		return Optional.ofNullable(users.get(id));
	}

	@Override
	public boolean deleteUserById(Long id) {
		if (users.containsKey(id)) {
			users.remove(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean hasUserById(Long id) {
		return users.containsKey(id);
	}

	@Override
	public boolean hasUserByEmail(User user) {
		return users.containsValue(user);
	}

}
