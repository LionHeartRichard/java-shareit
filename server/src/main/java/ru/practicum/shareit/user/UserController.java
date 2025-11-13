package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.common.dto.user.UserCreateDto;
import ru.practicum.shareit.common.dto.user.UserDto;
import ru.practicum.shareit.common.dto.user.UserUpdateDto;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
	UserService service;
	private static final String PATH_USER = "/{userId}";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto createUser(@RequestBody UserCreateDto dto) {
		log.trace("createUser: {}", dto.toString());
		final User user = service.createUser(UserMapper.toModel(dto));
		log.trace("user in DB: {}", user.toString());
		UserDto ans = UserMapper.toDto(user);
		log.trace("ans full dto: {}", ans.toString());
		return ans;
	}

	@PatchMapping(PATH_USER)
	@ResponseStatus(HttpStatus.OK)
	public UserDto updateUser(@PathVariable final Long userId, @RequestBody UserUpdateDto dto) {
		log.trace("UserId: {}, updateUser: {}", userId, dto.toString());
		final User user = service.findUserById(userId);
		log.trace("Old user in DB: {}", user.toString());
		final User ans = service.updateUser(UserMapper.toModel(dto, user));
		log.trace("Update user, ans: {}", ans.toString());
		return UserMapper.toDto(ans);
	}

	@GetMapping(PATH_USER)
	@ResponseStatus(HttpStatus.OK)
	public UserDto findUserById(@PathVariable final Long userId) {
		log.trace("findUserById: userId = {}", userId);
		final User ans = service.findUserById(userId);
		log.trace("find user in DB: {}", ans.toString());
		return UserMapper.toDto(ans);
	}

	@DeleteMapping(PATH_USER)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUserById(@PathVariable final Long userId) {
		log.trace("deleteUserById: userId: {}", userId);
		service.deleteUserById(userId);
	}
}
