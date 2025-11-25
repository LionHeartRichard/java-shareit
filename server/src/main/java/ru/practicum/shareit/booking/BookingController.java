package ru.practicum.shareit.booking;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import ru.practicum.shareit.common.dto.booking.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import ru.practicum.shareit.common.StateBooking;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

	BookingService service;
	BookingMapper mapper;
	private static final String HEADER = "X-Sharer-User-Id";
	private static final String PATH_BOOKING = "/{bookingId}";

	@PostMapping
	public BookingDto createBooking(@RequestHeader(HEADER) final Long bookerId, @RequestBody BookingDto dto) {
		log.trace("createBooking: {}", dto.toString());
		final User user = service.findUserById(bookerId);
		log.trace("find user in DB for createBooking: {}", user.toString());
		final Item item = service.findItemById(dto.getItem().getId());
		log.trace("find item in DB for createBooking: {}", item.toString());
		final Booking ans = service.createBooking(mapper.toModel(user, item, dto));
		log.trace("ans booking in DB: {}", ans.toString());
		return mapper.toDto(ans);
	}

	@GetMapping(PATH_BOOKING)
	public BookingDto findByUserIdAndBookingId(@RequestHeader(HEADER) final Long userId,
			@PathVariable final Long bookingId) {
		log.trace("findByUserIdAndBookingId: userId = {}, bookingId = {}", userId, bookingId);
		final Booking ans = service.findByUserIdAndBookingId(userId, bookingId);
		log.trace("ans booking in DB: {}", ans.toString());
		return mapper.toDto(ans);
	}

	@GetMapping
	public List<BookingDto> findByUserIdAndState(@RequestHeader(HEADER) final Long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state) {
		log.trace("findByBookerIdAndState: bookerId = {}, state = {}", userId, state);
		final List<Booking> ans = service.findByUserIdAndState(userId, StateBooking.valueOf(state));
		log.trace("List<Booking> ans: {}", ans.toString());
		return ans.stream().map(v -> mapper.toDto(v)).toList();
	}

	@GetMapping("/owner")
	public List<BookingDto> findByOwnerIdAndState(@RequestHeader(HEADER) final Long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state) {
		log.trace("findByUserIdAndState: userId = {}, state = {}", userId, state);
		List<Booking> ans = service.findByUserIdAndState(userId, StateBooking.valueOf(state));
		log.trace("List<Booking> ans: {}", ans.toString());
		return ans.stream().map(v -> mapper.toDto(v)).toList();
	}

	@PatchMapping(PATH_BOOKING)
	public BookingDto approvedByUserIdAndBookingId(@RequestHeader(HEADER) final Long userId,
			@PathVariable final Long bookingId, @RequestParam Boolean approved) {
		log.trace("approvedByUserIdAndBookingId: userId: {}, bookingId: {}, approved: {}", userId, bookingId, approved);
		Booking ans = service.approvedByUserIdAndBookingId(userId, bookingId, approved);
		log.trace("ans booking in DB: {}", ans.toString());
		return mapper.toDtoSaveStatus(ans);
	}
}