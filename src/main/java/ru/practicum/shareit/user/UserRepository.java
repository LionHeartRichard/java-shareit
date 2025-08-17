package ru.practicum.shareit.user;

import java.util.Optional;

public interface UserRepository {
	User createUser(User user);

	User updateUser(User user);

	Optional<User> findUserById(Long id);

	boolean deleteUserById(Long id);
	
	boolean hasUserById(Long id);
	
	boolean hasUserByEmail(User user);
}
