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
import ru.practicum.shareit.common.dto.user.UserFullDto;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

	UserService service;
	private static final String USER_ID = "/{userId}";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserFullDto createUser(@RequestBody UserDto dto) {
		log.info("<--SERVER-->  CREATE User, dto: {}", dto.toString());
		return service.createUser(dto);
	}

	@PatchMapping(USER_ID)
	@ResponseStatus(HttpStatus.OK)
	public UserFullDto updateUser(@PathVariable final Long userId, @RequestBody UserFullDto dto) {
		log.info("<--SERVER-->  UPDATE User id: {}, dto: {}", userId, dto.toString());
		return service.updateUser(userId, dto);
	}

	@GetMapping(USER_ID)
	@ResponseStatus(HttpStatus.OK)
	public UserFullDto findUserById(@PathVariable final Long userId) {
		log.info("<--SERVER-->  FIND User, id: {}", userId);
		return service.findUserById(userId);
	}

	@DeleteMapping(USER_ID)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUserById(@PathVariable final Long userId) {
		log.info("<--SERVER-->  DELETE User, id: {}", userId);
		service.deleteUserById(userId);
	}

	@GetMapping
	public List<UserFullDto> getAllUsers() {
		log.info("getAllUsers");
		return service.findAll();
	}

}
