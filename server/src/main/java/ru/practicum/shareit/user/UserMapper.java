package ru.practicum.shareit.user;

import ru.practicum.shareit.common.dto.user.UserDto;

public class UserMapper {

	private UserMapper() {
	}

	public static User toModel(final UserDto dto) {
		final User ans = User.builder().id(dto.getId()).name(dto.getName()).email(dto.getEmail()).build();
		return ans;
	}

	public static UserDto toDto(final User user) {
		final UserDto ans = UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
		return ans;
	}

	public static User toModel(final UserDto dto, final User user) {
		final String name = dto.hasName() ? dto.getName() : user.getName();
		final String email = dto.hasEmail() ? dto.getEmail() : user.getEmail();
		final User ans = User.builder().id(user.getId()).name(name).email(email).build();
		return ans;
	}

}
