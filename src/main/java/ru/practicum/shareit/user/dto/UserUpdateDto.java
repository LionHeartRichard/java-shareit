package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class UserUpdateDto {

	@Size(max = 50)
	String name;
	@Email
	String email;

	public boolean hasName() {
		return !(name == null || name.isBlank());
	}

	public boolean hasEmail() {
		return !(email == null || email.isBlank());
	}
}
