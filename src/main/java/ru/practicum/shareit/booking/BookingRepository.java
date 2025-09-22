package ru.practicum.shareit.booking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	Optional<Booking> findByUserIdAndId(final Long userId, final Long bookingId);

	List<Booking> findByUserId(final Long userId);

	Optional<Booking> findByUserIdAndItemId(final Long userId, final Long itemId);

	List<Booking> findByItemId(final Long itemId);

	// Booking[] findLastBooking(final Long id, final Long time);

}
