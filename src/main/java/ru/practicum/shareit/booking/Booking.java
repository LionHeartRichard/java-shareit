package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-bookings.
 */

@Value
@Builder(toBuilder = true)
public class Booking {
	Long id;
	Item item;
	User user;
	Long start;
	Long end;
	BookingStatus status;

	public static final String NOT_FOUND = "Booking not found!";
	public static final String NOT_OWNER = "User is not the owner of the item, access denied!";
	public static final String ERROR_STATUS = "The booking status cannot be changed!";
	public static final String ERROR_TIME = "The booking start time cannot be later than the end time!";

	public boolean isOwner(Long userId) {
		return Long.compare(user.getId(), userId) == 0;
	}
}
