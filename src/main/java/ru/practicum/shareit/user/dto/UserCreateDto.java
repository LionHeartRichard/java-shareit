package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class UserCreateDto {
	@NotBlank
	@Size(max = 50)
	String name;
	@NotNull
	@Email
	String email;
}
