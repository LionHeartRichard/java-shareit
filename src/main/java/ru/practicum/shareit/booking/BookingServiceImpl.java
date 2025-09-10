package ru.practicum.shareit.booking;

import java.util.List;

import org.springframework.stereotype.Service;

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
public class BookingServiceImpl implements BookingService {

	BookingRepository bookingRepository;
	UserRepository userRepository;
	ItemRepository itemRepository;

	@Override
	public User findUserById(Long bookerId) {
		return userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	@Override
	public Item findItemById(Long itemId) {
		Item ans = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
		if (ans.getAvailable()) {
			return ans;
		}
		throw new MyBadRequestException(Item.NOT_AVAILABLE);
	}

	@Override
	public Booking createBooking(Booking booking) {
		if (Long.compare(booking.getStart(), booking.getEnd()) < 0) {
			return bookingRepository.save(booking);
		}
		throw new ConflictException(Booking.ERROR_TIME);
	}

	@Override
	public Booking findByUserIdAndBookingId(Long userId, Long bookingId) {
		userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		return bookingRepository.findByUserIdAndBookingId(bookingId)
				.orElseThrow(() -> new NotFoundException(Booking.NOT_FOUND));
	}

	@Override
	public List<Booking> findByUserIdAndState(Long userId, TmpState state) {
		userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		return bookingRepository.findAllByUserIdAndStateOrderByStartDesc(userId, state);
	}

	@Override
	public Booking approvedByUserIdAndBookingId(Long userId, Long bookingId, Boolean approved) {
		final Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new NotFoundException(Booking.NOT_FOUND));
		userRepository.findById(userId).orElseThrow(() -> new AccessException(User.NO_ACCESS));
		if (booking.getStatus() != BookingStatus.WAITING) {
			throw new MyBadRequestException(Booking.ERROR_STATUS);
		}
		BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
		return bookingRepository.save(booking.toBuilder().status(status).build());
	}

}
