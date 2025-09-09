package ru.practicum.shareit.booking;

import java.util.List;
import java.util.Optional;

import ru.practicum.shareit.TmpState;

public interface BookingRepository {

	Booking save(Booking booking);

	Optional<Booking> findByUserIdAndBookingId(final Long bookingId);

	List<Booking> findAllByUserIdAndStateOrderByStartDesc(final Long userId, TmpState state);

	Optional<Booking> findById(final Long bookingId);

	boolean youBooked(final Long userId, final Long itemId);

	Booking[] findLastBooking(final Long id, final Long time);

}
