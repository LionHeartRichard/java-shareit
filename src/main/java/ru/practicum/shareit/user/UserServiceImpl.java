package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

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

	@Override
	public User createUser(User user) {
		if (!userRepository.hasUserByEmail(user))
			return userRepository.save(user);
		throw new ConflictException(User.EMAIL_IN_USE);
	}

	@Override
	public User updateUser(User user) {
		if (!userRepository.isUsedEmail(user))
			return userRepository.save(user);
		throw new ConflictException(User.EMAIL_IN_USE);
	}

	@Override
	public User findUserById(Long id) {
		return userRepository.findUserById(id).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	@Override
	public void deleteUserById(Long id) {
		userRepository.deleteUserById(id);
	}

}
