package ru.practicum.shareit.booking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	Optional<Booking> findByUserIdAndId(final Long userId, final Long bookingId);

	List<Booking> findByUserId(final Long userId);

	Optional<Booking> findByUserIdAndItemId(final Long userId, final Long itemId);

	List<Booking> findByItemId(final Long itemId);

	@Query(value = "SELECT * FROM booking WHERE item_id = :item_id AND user_id = :user_id AND status = :status AND end_ < :end_", nativeQuery = true)
	List<Booking> findByItemIdAndUserIdAndStatusIsAndEndTime(@Param("item_id") final Long itemId,
			@Param("user_id") final Long userId, @Param("status") final String status, @Param("end_") final Long end);

	@Query(value = "SELECT * FROM booking WHERE item_id = :item_id AND end_ < :end_ ORDER BY start_ DESC LIMIT 1", nativeQuery = true)
	Optional<Booking> findLastBooking(@Param("item_id") final Long itemId, @Param("end_") final Long currentTime);

	@Query(value = "SELECT * FROM booking WHERE item_id = :item_id AND start_ > :start_ ORDER BY start_ ASC LIMIT 1", nativeQuery = true)
	Optional<Booking> findNextBooking(@Param("item_id") final Long itemId, @Param("start_") final Long currentTime);

	@Query(value = "select COUNT(*)>0 from booking where item_id = :item_id and user_id = :user_id and status = 'APPROVED' and end_ < :end_", nativeQuery = true)
	boolean hasApprovedBooking(@Param("user_id") final Long userId, @Param("item_id") final Long itemId,
			@Param("end_") final Long time);

}
