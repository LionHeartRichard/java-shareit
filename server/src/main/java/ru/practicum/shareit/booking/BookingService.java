package ru.practicum.shareit.booking;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.UtilMapper;
import ru.practicum.shareit.common.BookingStatus;
import ru.practicum.shareit.common.StateBooking;
import ru.practicum.shareit.common.dto.booking.BookingDto;
import ru.practicum.shareit.common.dto.booking.BookingFullDto;
import ru.practicum.shareit.common.exception.AccessException;
import ru.practicum.shareit.common.exception.MyBadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class BookingService {

	BookingRepository repoBooking;
	UserRepository repoUser;
	ItemRepository repoItem;

	@Transactional
	public BookingFullDto createBooking(final Long userId, final BookingDto dto) {
		long start = UtilMapper.toLong(dto.getStart());
		long end = UtilMapper.toLong(dto.getEnd());

		if (Long.compare(start, end) < 0) {
			User user = repoUser.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
			Item item = repoItem.findById(dto.getItemId()).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
			if (item.isOwner(userId)) {
				throw new MyBadRequestException(Item.IS_OWNER);
			}
			if (!item.getAvailable()) {
				throw new MyBadRequestException(Item.NOT_AVAILABLE);
			}
			Booking booking = BookingMapper.toModel(user, item, dto);
			return BookingMapper.toDtoSaveStatus(repoBooking.save(booking));
		}
		throw new MyBadRequestException(Booking.ERROR_TIME);
	}

	public BookingFullDto findByUserIdAndBookingId(Long userId, Long bookingId) {
		if (repoUser.hasId(userId)) {
			Booking ans = repoBooking.findById(bookingId).orElseThrow(() -> new NotFoundException(Booking.NOT_FOUND));
			return BookingMapper.toDto(ans);
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	public List<BookingFullDto> findByUserIdAndState(Long userId, StateBooking state) {
		if (repoUser.hasId(userId)) {
			List<Booking> ans = repoBooking.findByUserId(userId);
			return ans.stream().filter(UtilBooking.filterByState(state)).map(BookingMapper::toDto).toList();
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	public BookingFullDto approvedByUserIdAndBookingId(Long userId, Long bookingId, Boolean approved) {
		final Booking booking = repoBooking.findById(bookingId)
				.orElseThrow(() -> new NotFoundException(Booking.NOT_FOUND));
		if (repoUser.hasId(userId)) {
			if (!booking.getItem().isOwner(userId)) {
				throw new MyBadRequestException(Booking.NOT_OWNER);
			}
			if (booking.getStatus() != BookingStatus.WAITING) {
				throw new MyBadRequestException(Booking.ERROR_STATUS);
			}
			BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
			Booking ans = repoBooking.save(booking.toBuilder().status(status).build());
			return BookingMapper.toDtoSaveStatus(ans);
		}
		throw new AccessException(User.NO_ACCESS);
	}

}
