package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserFullDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public class UserMapper {

	private UserMapper() {
	}

	public static User toModel(UserCreateDto dto) {
		User user = User.builder().id(null).name(dto.getName()).email(dto.getEmail()).build();
		return user;
	}

	public static User toModel(User user, UserUpdateDto dto) {
		final String name = dto.hasName() ? dto.getName() : user.getName();
		final String email = dto.hasEmail() ? dto.getEmail() : user.getEmail();
		User ans = user.toBuilder().name(name).email(email).build();
		return ans;
	}

	public static UserFullDto toDto(User user) {
		UserFullDto dto = UserFullDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
		return dto;
	}

}
