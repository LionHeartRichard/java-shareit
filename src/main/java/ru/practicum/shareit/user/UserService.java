package ru.practicum.shareit.user;

public interface UserService {

	User createUser(User user);

	User updateUser(User user);

	User findUserById(final Long id);

	void deleteUserById(final Long id);
}
