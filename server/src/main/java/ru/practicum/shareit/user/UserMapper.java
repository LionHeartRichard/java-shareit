package ru.practicum.shareit.user;

import ru.practicum.shareit.common.dto.user.UserCreateDto;
import ru.practicum.shareit.common.dto.user.UserDto;
import ru.practicum.shareit.common.dto.user.UserUpdateDto;

public class UserMapper {

	private UserMapper() {
	}

	public static User toModel(final UserCreateDto dto) {
		final User ans = User.builder().id(null).name(dto.getName()).email(dto.getEmail()).build();
		return ans;
	}

	public static UserDto toDto(final User user) {
		final UserDto ans = UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
		return ans;
	}

	public static User toModel(final UserUpdateDto dto, final User user) {
		final String name = dto.hasName() ? dto.getName() : user.getName();
		final String email = dto.hasEmail() ? dto.getEmail() : user.getEmail();
		final User ans = User.builder().id(user.getId()).name(name).email(email).build();
		return ans;
	}

}
