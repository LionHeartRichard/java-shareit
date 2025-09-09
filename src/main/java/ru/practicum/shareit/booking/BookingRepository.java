package ru.practicum.shareit.booking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.practicum.shareit.TmpState;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	Optional<Booking> findByUserIdAndBookingId(final Long bookingId);

	List<Booking> findAllByUserIdAndStateOrderByStartDesc(final Long userId, TmpState state);

	Optional<Booking> findById(final Long bookingId);

	boolean youBooked(final Long userId, final Long itemId);

	Booking[] findLastBooking(final Long id, final Long time);

}
