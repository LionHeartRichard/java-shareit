package ru.practicum.shareit.booking;

import java.util.List;
import java.util.Optional;

import ru.practicum.shareit.TmpState;

public interface BookingRepository {

	Booking save(Booking booking);

	Optional<Booking> findByUserIdAndBookingId(Long bookingId);

	List<Booking> findAllByUserIdAndStateOrderByStartDesc(Long userId, TmpState state);

	Optional<Booking> findById(Long bookingId);

}
