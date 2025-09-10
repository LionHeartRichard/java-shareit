package ru.practicum.shareit.item;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.user.User;

@Entity
@Table(name = "item")
@Value
@Builder(toBuilder = true)
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	Boolean available;
	String description;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	User user;

	public static final String NOT_FOUND = "Item not found!";
	public static final String NOT_OWNER = "User is not the owner of item!";
	public static final String NOT_AVAILABLE = "Item is not available!";

	public boolean isOwner(final Long userId) {
		return this.user.getId().equals(userId);
	}

	public boolean isAvailable(String nameItem) {
		return this.available && this.name.equals(nameItem);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		return Objects.equals(id, other.id) && Objects.equals(user.getId(), other.user.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, user.getId());
	}

}
