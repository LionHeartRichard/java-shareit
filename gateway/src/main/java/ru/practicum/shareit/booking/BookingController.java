package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import ru.practicum.shareit.common.StateBooking;
import ru.practicum.shareit.common.dto.booking.BookingCreateDto;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

	BookingClient client;
	private static final String HEADER = "X-Sharer-User-Id";
	private static final String PATH = "/{bookingId}";
	private static final String OWNER = "/owner";

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@RequestBody @Valid final BookingCreateDto dto) {
		log.info("<--GATEWAY--> Creating booking {}, userId={}", dto, userId);
		return client.createBooking(userId, dto);
	}

	@GetMapping(PATH)
	public ResponseEntity<Object> getBooking(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable final Long bookingId) {
		log.info("<--GATEWAY-->  Get booking {}, userId={}", bookingId, userId);
		return client.getBooking(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@RequestParam(name = "state", defaultValue = "all") final String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") final Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") final Integer size) {
		StateBooking state = StateBooking.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("<--GATEWAY-->  Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from,
				size);
		return client.getBookings(userId, state, from, size);
	}

	@GetMapping(OWNER)
	public ResponseEntity<Object> findByOwnerIdAndState(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state) {
		log.info("<--GATEWAY-->  Find by User ID and State: userId = {}, state = {}", userId, state);
		return client.findByOwnerAndState(OWNER, userId, StateBooking.valueOf(state));
	}

	@PatchMapping(PATH)
	public ResponseEntity<Object> approvedByUserIdAndBookingId(
			@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @NotNull @Positive Long bookingId, @RequestParam @NotNull Boolean approved) {
		log.info("<--GATEWAY-->  Approved by User ID and BookingId: userId: {}, bookingId: {}, approved: {}", userId,
				bookingId, approved);
		return client.approved(bookingId, userId, approved);
	}
}