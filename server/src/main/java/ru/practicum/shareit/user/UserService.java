package ru.practicum.shareit.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.dto.user.UserDto;
import ru.practicum.shareit.common.dto.user.UserFullDto;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.NotFoundException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class UserService {

	UserRepository repo;

	@Transactional
	public UserFullDto createUser(final UserDto dto) {
		if (!repo.hasEmail(dto.getEmail())) {
			return UserMapper.toDto(repo.save(UserMapper.toModel(dto)));
		}
		throw new ConflictException(User.EMAIL_IN_USE);
	}

	@Transactional
	public UserFullDto updateUser(final Long userId, final UserDto dto) {
		User user = repo.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		if (!repo.emailIsUsed(userId, dto.getEmail())) {
			User ans = UserMapper.toModel(dto, user);
			return UserMapper.toDto(repo.save(ans));
		}
		throw new ConflictException(User.EMAIL_IN_USE);

	}

	public UserFullDto findUserById(final Long id) {
		User ans = repo.findById(id).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		return UserMapper.toDto(ans);
	}

	@Transactional
	public void deleteUserById(final Long id) {
		repo.deleteById(id);
	}

	public User findByEmail(final String email) {
		return repo.findByEmail(email).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	public List<UserFullDto> findAll() {
		return repo.findAll().stream().map(v -> UserMapper.toDto(v)).toList();
	}

}
