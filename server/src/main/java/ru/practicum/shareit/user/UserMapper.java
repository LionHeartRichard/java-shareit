package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import ru.practicum.shareit.common.dto.user.UserDto;

@Component
public class UserMapper {

	public User toModel(final UserDto dto) {
		final User ans = User.builder().id(dto.getId()).name(dto.getName()).email(dto.getEmail()).build();
		return ans;
	}

	public UserDto toDto(final User user) {
		final UserDto ans = UserDto.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
		return ans;
	}

	public User toModel(final UserDto dto, final User user) {
		final String name = dto.hasName() ? dto.getName() : user.getName();
		final String email = dto.hasEmail() ? dto.getEmail() : user.getEmail();
		final User ans = User.builder().id(user.getId()).name(name).email(email).build();
		return ans;
	}

}
