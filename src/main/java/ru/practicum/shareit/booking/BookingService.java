package ru.practicum.shareit.booking;

import java.util.List;

import ru.practicum.shareit.TmpState;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

public interface BookingService {

	User findUserById(Long bookerId);

	Item findItemById(Long itemId);

	Booking createBooking(Booking booking);

	Booking findByUserIdAndBookingId(Long userId, Long bookingId);

	List<Booking> findByUserIdAndState(Long userId, TmpState state);

	Booking approvedByUserIdAndBookingId(Long userId, Long bookingId, Boolean approved);

}
