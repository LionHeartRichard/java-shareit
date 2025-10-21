package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class UserUpdateDto {

	String name;
	String email;

	public boolean hasName() {
		return !(name == null || name.isBlank());
	}

	public boolean hasEmail() {
		return !(email == null || email.isBlank());
	}
}
