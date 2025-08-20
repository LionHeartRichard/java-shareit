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
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserFullDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

/**
 * TODO Sprint add-controllers.
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

	UserService userService;
	private static final String PATH_USER = "/{userId}";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserFullDto createUser(@RequestBody @Valid UserCreateDto dto) {
		log.trace("createUser: ", dto.toString());
		User user = userService.createUser(UserMapper.toModel(dto));
		log.trace(user.toString());
		UserFullDto ans = UserMapper.toDto(user);
		log.trace(ans.toString());
		return ans;
	}

	@PatchMapping(PATH_USER)
	@ResponseStatus(HttpStatus.OK)
	public UserFullDto updateUser(@PathVariable @Positive final Long userId, @RequestBody @Valid UserUpdateDto dto) {
		log.trace("updateUser: ", dto.toString());
		User user = userService.findUserById(userId);
		log.trace(user.toString());
		User ans = userService.updateUser(UserMapper.toModel(user, dto));
		log.trace(ans.toString());
		return UserMapper.toDto(ans);
	}

	@GetMapping(PATH_USER)
	@ResponseStatus(HttpStatus.OK)
	public UserFullDto findUserById(@PathVariable @Positive final Long userId) {
		log.trace("findUserById: userId = ", userId);
		User ans = userService.findUserById(userId);
		log.trace(ans.toString());
		return UserMapper.toDto(ans);
	}

	@DeleteMapping(PATH_USER)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUserById(@PathVariable @Positive final Long userId) {
		log.trace("deleteUserById: userId = ", userId);
		userService.deleteUserById(userId);
	}

}
