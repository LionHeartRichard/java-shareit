package ru.practicum.shareit.user;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@Entity
@Table(name = "user_")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	Long id;

	@Column(nullable = false)
	String name;

	@Column(nullable = false, unique = true)
	String email;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User) o;
		if (user.id != null && id != null) {
			return id.equals(user.id);
		}
		return Objects.equals(name, user.name) && Objects.equals(email, user.email);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : Objects.hash(name, email);
	}

	public static final String NOT_FOUND = "User not found!";
	public static final String EMAIL_IN_USE = "The specified email is already in use!";
	public static final String NO_ACCESS = "User does not have access!";

}
