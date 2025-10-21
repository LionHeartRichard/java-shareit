package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class UserService {

	UserRepository repo;

	@Transactional
	public User createUser(final User user) {
		if (!repo.hasEmail(user.getEmail())) {
			return repo.save(user);
		}
		throw new ConflictException(User.EMAIL_IN_USE);
	}

	@Transactional
	public User updateUser(final User user) {
		if (!repo.emailIsUsed(user.getId(), user.getEmail())) {
			return repo.save(user);
		}
		throw new ConflictException(User.EMAIL_IN_USE);

	}

	public User findUserById(final Long id) {
		return repo.findById(id).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	@Transactional
	public void deleteUserById(final Long id) {
		repo.deleteById(id);
	}

}
