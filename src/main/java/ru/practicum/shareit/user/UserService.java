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

	UserRepository userRepository;

	@Transactional
	public User createUser(User user) {
		if (!userRepository.hasEmail(user.getEmail())) {
			return userRepository.save(user);
		}
		throw new ConflictException(User.EMAIL_IN_USE);
	}

	@Transactional
	public User updateUser(User user) {
		if (!userRepository.emailIsUsed(user.getId(), user.getEmail())) {
			return userRepository.save(user);
		}
		throw new ConflictException(User.EMAIL_IN_USE);

	}

	public User findUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	@Transactional
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

}
