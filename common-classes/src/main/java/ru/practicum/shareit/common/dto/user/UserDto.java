package ru.practicum.shareit.common.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

	Long id;
	String name;
	String email;

	public boolean hasName() {
		return !(name == null || name.isBlank());
	}

	public boolean hasEmail() {
		return !(email == null || email.isBlank());
	}
}
