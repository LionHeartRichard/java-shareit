package ru.practicum.shareit.booking;

import jakarta.persistence.Column;
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
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Entity
@Table(name = "booking")
@Value
@Builder(toBuilder = true)
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@JoinColumn(name = "item_id")
	@ManyToOne(fetch = FetchType.LAZY)
	Item item;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	User user;

	@Column(name = "start_")
	Long start;
	@Column(name = "end_")
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
