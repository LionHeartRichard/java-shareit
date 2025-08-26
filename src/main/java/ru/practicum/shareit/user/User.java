package ru.practicum.shareit.user;

import java.util.Objects;

import lombok.Builder;
import lombok.Value;

/**
 * TODO Sprint add-controllers.
 */

@Value
@Builder(toBuilder = true)
public class User {
	Long id;
	String name;
	String email;

	public static final String NOT_FOUND = "User not found!";
	public static final String EMAIL_IN_USE = "The specified email is already in use!";

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}
}
