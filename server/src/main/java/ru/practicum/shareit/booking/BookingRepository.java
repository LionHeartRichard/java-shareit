package ru.practicum.shareit.booking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByUserId(final Long userId);

	Optional<Booking> findByUserIdAndItemId(final Long userId, final Long itemId);

	List<Booking> findByItemId(final Long itemId);

	@Query("SELECT b FROM Booking b " + "WHERE b.item.id = :itemId " + "  AND b.user.id = :userId "
			+ "  AND b.status = :status " + "  AND b.end <= :endTime")
	List<Booking> findByItemIdAndUserIdAndStatusIsAndEndTime(@Param("itemId") final Long itemId,
			@Param("userId") final Long userId, @Param("status") final String status,
			@Param("endTime") final Long endTime);

	@Query("SELECT TOP 1 b FROM Booking b " + "WHERE b.item.id = :itemId " + "  AND b.end < :currentTime "
			+ "ORDER BY b.start DESC")
	Optional<Booking> findLastBooking(@Param("itemId") final Long itemId, @Param("currentTime") final Long currentTime);

	@Query("SELECT TOP 1 b FROM Booking b " + "WHERE b.item.id = :itemId " + "  AND b.start > :currentTime "
			+ "ORDER BY b.start ASC")
	Optional<Booking> findNextBooking(@Param("itemId") final Long itemId, @Param("currentTime") final Long currentTime);

	@Query("SELECT COUNT(b) > 0 FROM Booking b " + "WHERE b.item.id = :itemId " + "  AND b.user.id = :userId "
			+ "  AND b.status = 'APPROVED' " + "  AND b.end <= :time")
	boolean hasApprovedBooking(@Param("userId") final Long userId, @Param("itemId") final Long itemId,
			@Param("time") final Long time);
}
