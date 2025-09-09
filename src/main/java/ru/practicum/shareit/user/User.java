package ru.practicum.shareit.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import lombok.Builder;
import lombok.Value;

@Entity
@Table(name = "user_")
@Value
@Builder(toBuilder = true)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	String email;

	public static final String NOT_FOUND = "User not found!";
	public static final String EMAIL_IN_USE = "The specified email is already in use!";
	public static final String NO_ACCESS = "User does not have access!";

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
