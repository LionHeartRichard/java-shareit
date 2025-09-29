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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserFullDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

	UserMapper mapper;
	UserService userService;
	private static final String PATH_USER = "/{userId}";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserFullDto createUser(@RequestBody @Valid UserCreateDto dto) {
		log.trace("createUser: {}", dto.toString());
		final User user = userService.createUser(mapper.toEntity(dto));
		log.trace("user in DB: {}", user.toString());
		UserFullDto ans = mapper.toDto(user);
		log.trace("ans full dto: {}", ans.toString());
		return ans;
	}

	@PatchMapping(PATH_USER)
	@ResponseStatus(HttpStatus.OK)
	public UserFullDto updateUser(@PathVariable @NotNull @Positive final Long userId,
			@RequestBody @Valid UserUpdateDto dto) {
		log.trace("UserId: {}, updateUser: {}", userId, dto.toString());
		final User user = userService.findUserById(userId);
		log.trace("Old user in DB: {}", user.toString());
		final User ans = userService.updateUser(mapper.toEntity(dto, user));
		log.trace("Update user, ans: {}", ans.toString());
		return mapper.toDto(ans);
	}

	@GetMapping(PATH_USER)
	@ResponseStatus(HttpStatus.OK)
	public UserFullDto findUserById(@PathVariable @NotNull @Positive final Long userId) {
		log.trace("findUserById: userId = {}", userId);
		final User ans = userService.findUserById(userId);
		log.trace("find user in DB: {}", ans.toString());
		return mapper.toDto(ans);
	}

	@DeleteMapping(PATH_USER)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUserById(@PathVariable @NotNull @Positive final Long userId) {
		log.trace("deleteUserById: userId: {}", userId);
		userService.deleteUserById(userId);
	}

}
