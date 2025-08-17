package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * TODO Sprint add-controllers.
 */

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(exclude = "email")
public class User {
	Long id;
	String name;
	String email;

	public static final String NOT_FOUND = "User not found!";
	public static final String EMAIL_IN_USE = "The specified email is already in use!";
}
