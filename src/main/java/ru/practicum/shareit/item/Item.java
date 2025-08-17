package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * TODO Sprint add-controllers.
 */

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(exclude = {"id", "userId"})
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
}
