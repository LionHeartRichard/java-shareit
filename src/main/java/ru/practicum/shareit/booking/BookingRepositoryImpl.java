package ru.practicum.shareit.booking;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import ru.practicum.shareit.TmpState;

@Repository
public class BookingRepositoryImpl implements BookingRepository {

	private Long id;
	private Map<Long, Booking> bookingData;

	public BookingRepositoryImpl() {
		id = 0L;
		bookingData = new HashMap<>();
	}

	@Override
	public Booking save(Booking booking) {
		if (booking.getId() == null) {
			++id;
			Booking ans = booking.toBuilder().id(id).build();
			bookingData.put(id, ans);
			return ans;
		}
		bookingData.put(booking.getId(), booking);
		return booking;

	}

	@Override
	public Optional<Booking> findByUserIdAndBookingId(final Long bookingId) {
		if (bookingData.containsKey(bookingId)) {
			return Optional.of(bookingData.get(bookingId));
		}
		return Optional.empty();
	}

	@Override
	public List<Booking> findAllByUserIdAndStateOrderByStartDesc(final Long userId, TmpState state) {
		List<Booking> ans = bookingData.values().stream().filter(v -> v.getUser().getId().equals(userId))
				.filter(UtilBooking.filterByState(state)).sorted(Comparator.comparingLong(v -> v.getUser().getId()))
				.toList();
		return ans;
	}

	@Override
	public Optional<Booking> findById(final Long bookingId) {
		if (bookingData.containsKey(bookingId)) {
			return Optional.of(bookingData.get(bookingId));
		}
		return Optional.empty();
	}

	@Override
	public boolean youBooked(final Long userId, final Long itemId) {
		List<Booking> tmp = bookingData.values().stream().filter(v -> v.getItem().getId().equals(itemId)
				&& v.getUser().getId().equals(userId) && v.getStatus().equals(BookingStatus.APPROVED)).toList();
		return !(tmp == null || tmp.isEmpty());
	}

	@Override
	public Booking[] findLastBooking(final Long itemId, final Long time) {
		Booking[] ans = new Booking[2];
		List<Booking> swap = bookingData.values().stream().filter(v -> v.getItem().getId().equals(itemId)).toList();
		if (!(swap == null || swap.isEmpty())) {
			ans[0] = swap.stream().filter(v -> Long.compare(v.getStart(), time) < 0)
					.sorted(Comparator.comparing(Booking::getStart)).findFirst().orElse(null);
			ans[1] = swap.stream().filter(v -> Long.compare(v.getEnd(), time) < 0)
					.sorted(Comparator.comparing(Booking::getEnd)).findFirst().orElse(null);
		}
		return ans;
	}

}
