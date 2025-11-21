package ru.practicum.shareit.common.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserCreateDto {
	@NotBlank(message = "Name is blank!!!")
	@Size(max = 50, message = "The name cannot be longer than 50 characters!!!")
	String name;
	@NotNull(message = "Email is blank!!!")
	@Email(message = "Invalid email!!!")
	String email;
}
