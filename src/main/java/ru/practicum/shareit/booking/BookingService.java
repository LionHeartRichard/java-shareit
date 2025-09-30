package ru.practicum.shareit.booking;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.TmpState;
import ru.practicum.shareit.exception.MyBadRequestException;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class BookingService {

	BookingRepository repBooking;
	UserRepository repUser;
	ItemRepository repItem;

	public User findUserById(Long bookerId) {
		return repUser.findById(bookerId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	public Item findItemById(Long itemId) {
		Item ans = repItem.findById(itemId).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
		if (ans.getAvailable()) {
			return ans;
		}
		throw new MyBadRequestException(Item.NOT_AVAILABLE);
	}

	@Transactional
	public Booking createBooking(Booking booking) {
		if (Long.compare(booking.getStart(), booking.getEnd()) < 0) {
			return repBooking.save(booking);
		}
		throw new ConflictException(Booking.ERROR_TIME);
	}

	public Booking findByUserIdAndBookingId(Long userId, Long bookingId) {
		repUser.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		return repBooking.findByUserIdAndId(userId, bookingId)
				.orElseThrow(() -> new NotFoundException(Booking.NOT_FOUND));
	}

	public List<Booking> findByUserIdAndState(Long userId, TmpState state) {
		repUser.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		List<Booking> ans = repBooking.findByUserId(userId);
		return ans.stream().filter(UtilBooking.filterByState(state)).toList();
	}

	public Booking approvedByUserIdAndBookingId(Long userId, Long bookingId, Boolean approved) {
		final Booking booking = repBooking.findById(bookingId)
				.orElseThrow(() -> new NotFoundException(Booking.NOT_FOUND));
		repUser.findById(userId).orElseThrow(() -> new AccessException(User.NO_ACCESS));
		if (booking.getStatus() != BookingStatus.WAITING) {
			throw new MyBadRequestException(Booking.ERROR_STATUS);
		}
		BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
		return repBooking.save(booking.toBuilder().status(status).build());
	}

}
