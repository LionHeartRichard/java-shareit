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
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

	UserClient client;
	private static final String PATH = "/{userId}";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> createUser(@RequestBody @Valid UserRequestDto dto) {
		log.info("Create User: {}", dto.toString());
		return client.createUser(dto);
	}

	@PatchMapping(PATH)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> updateUser(@PathVariable @NotNull @Positive final Long userId,
			@RequestBody @Valid UserRequestDto dto) {
		log.info("Update User userId: {}, updateUser: {}", userId, dto.toString());
		return client.updateUser(userId, dto);
	}

	@GetMapping(PATH)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> findUserById(@PathVariable @NotNull @Positive final Long userId) {
		log.info("Find User By Id: userId = {}", userId);
		return client.getUser(userId);
	}

	@DeleteMapping(PATH)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Object> deleteUserById(@PathVariable @NotNull @Positive final Long userId) {
		log.info("Delete User By Id: userId: {}", userId);
		return client.deleteUser(userId);
	}

}
