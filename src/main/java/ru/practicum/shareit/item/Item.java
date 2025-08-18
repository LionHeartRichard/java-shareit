package ru.practicum.shareit.item;

import java.util.Objects;

import lombok.Builder;
import lombok.Value;

/**
 * TODO Sprint add-controllers.
 */

@Value
@Builder(toBuilder = true)
public class Item {
	Long id;
	String name;
	Boolean available;
	String description;
	Long userId;

	public static final String NOT_FOUND = "Item not found!";
	public static final String NOT_OWNER = "User is not the owner of item";

	public boolean isOwner(final Long userId) {
		return this.userId.equals(userId);
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
		return Objects.equals(id, other.id) && Objects.equals(userId, other.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userId);
	}

}
