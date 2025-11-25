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

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.common.dto.user.UserDto;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

	UserService service;
	UserMapper mapper;
	private static final String USER_ID = "/{userId}";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto createUser(@RequestBody UserDto dto) {
		log.info("createUser: {}", dto.toString());
		final User user = service.createUser(mapper.toModel(dto));
		log.info("user in DB: {}", user);
		UserDto ans = mapper.toDto(user);
		log.info("ans full dto: {}", ans);
		return ans;
	}

	@PatchMapping(USER_ID)
	@ResponseStatus(HttpStatus.OK)
	public UserDto updateUser(@PathVariable final Long userId, @RequestBody UserDto dto) {
		log.info("UserId: {}, updateUser(Dto): {}", userId, dto.toString());
		final User user = service.findUserById(userId);
		log.info("Old user in DB: {}", user);
		final User ans = service.updateUser(mapper.toModel(dto, user));
		log.info("Update user, ans: {}", ans);
		return mapper.toDto(ans);
	}

	@GetMapping(USER_ID)
	@ResponseStatus(HttpStatus.OK)
	public UserDto findUserById(@PathVariable final Long userId) {
		log.info("findUserById: userId = {}", userId);
		final User ans = service.findUserById(userId);
		log.info("find user in DB: {}", ans);
		return mapper.toDto(ans);
	}

	@DeleteMapping(USER_ID)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUserById(@PathVariable final Long userId) {
		log.info("deleteUserById: userId: {}", userId);
		service.deleteUserById(userId);
	}

	@GetMapping
	public List<UserDto> getAllUsers() {
		log.info("getAllUsers");
		return service.findAll().stream().map(v -> mapper.toDto(v)).toList();
	}

}
