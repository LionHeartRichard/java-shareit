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
import ru.practicum.shareit.common.dto.booking.BookingFullDto;
import ru.practicum.shareit.common.StateBooking;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

	BookingService service;
	private static final String HEADER = "X-Sharer-User-Id";
	private static final String BOOKING_ID = "/{bookingId}";

	@PostMapping
	public BookingFullDto createBooking(@RequestHeader(HEADER) final Long bookerId, @RequestBody BookingDto dto) {
		log.info("<--SERVER-->  CREATE Booking, dto: {}", dto.toString());
		return service.createBooking(bookerId, dto);
	}

	@GetMapping(BOOKING_ID)
	public BookingFullDto findByUserIdAndBookingId(@RequestHeader(HEADER) final Long userId,
			@PathVariable final Long bookingId) {
		log.info("<--SERVER-->  FIND userId: {}, bookingId: {}", userId, bookingId);
		return service.findByUserIdAndBookingId(userId, bookingId);
	}

	@GetMapping
	public List<BookingFullDto> findByUserIdAndState(@RequestHeader(HEADER) final Long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state) {
		log.info("<--SERVER-->  FIND bookerId: {}, state: {}", userId, state);
		return service.findByUserIdAndState(userId, StateBooking.valueOf(state));
	}

	@GetMapping("/owner")
	public List<BookingFullDto> findByOwnerIdAndState(@RequestHeader(HEADER) final Long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state) {
		log.info("<--SERVER-->  FIND <--OWNER--> userId: {}, state: {}", userId, state);
		return service.findByUserIdAndState(userId, StateBooking.valueOf(state));
	}

	@PatchMapping(BOOKING_ID)
	public BookingFullDto approvedByUserIdAndBookingId(@RequestHeader(HEADER) final Long userId,
			@PathVariable final Long bookingId, @RequestParam(name = "approved") Boolean approved) {
		log.info("<--SERVER-->  APPROVED, userId: {}, bookingId: {}, approved: {}", userId, bookingId, approved);
		return service.approvedByUserIdAndBookingId(userId, bookingId, approved);
	}
}