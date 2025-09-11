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
public class UserServiceImpl implements UserService {

	UserRepository userRepository;

	@Transactional
	@Override
	public User createUser(User user) {
		if (!userRepository.hasUserByEmail(user.getEmail())) {
			return userRepository.save(user);
		}
		throw new ConflictException(User.EMAIL_IN_USE);
	}

	@Transactional
	@Override
	public User updateUser(User user) {
		final Long userId = user.getId();
		if (userRepository.hasUserById(userId)) {
			if (!userRepository.isUsedEmail(userId, user.getEmail())) {
				return userRepository.save(user);
			}
			throw new ConflictException(User.EMAIL_IN_USE);
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	@Override
	public User findUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	@Transactional
	@Override
	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

}
