package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateDto {

	@Size(max = 50, message = "The name cannot be longer than 50 characters!!!")
	String name;
	@Email(message = "Invalid email!!!")
	String email;

	public boolean hasName() {
		return !(name == null || name.isBlank());
	}

	public boolean hasEmail() {
		return !(email == null || email.isBlank());
	}
}
