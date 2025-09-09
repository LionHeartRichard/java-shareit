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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.TmpState;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

	BookingService service;
	private static final String HEADER = "X-Sharer-User-Id";
	private static final String PATH_BOOKING = "/{bookingId}";

	@PostMapping
	public BookingFullDto createBooking(@RequestHeader(HEADER) @NotNull @Positive final Long bookerId,
			@RequestBody @Valid BookingCreateDto dto) {
		log.trace("createBooking: ", dto.toString());
		User user = service.findUserById(bookerId);
		log.trace(user.toString());
		Item item = service.findItemById(dto.getItemId());
		log.trace(item.toString());
		Booking ans = service.createBooking(BookingMapper.toModel(user, item, dto));
		log.trace(ans.toString());
		return BookingMapper.toDto(ans);
	}

	@GetMapping(PATH_BOOKING)
	public BookingFullDto findByUserIdAndBookingId(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @Positive Long bookingId) {
		log.trace("BookingFullDto: userId = {}, bookingId = {}", userId, bookingId);
		Booking ans = service.findByUserIdAndBookingId(userId, bookingId);
		log.trace(ans.toString());
		return BookingMapper.toDto(ans);
	}

	@GetMapping
	public List<BookingFullDto> findByUserIdAndState(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state) {
		log.trace("findByBookerIdAndState: bookerId = {}, state = {}", userId, state);
		List<Booking> ans = service.findByUserIdAndState(userId, TmpState.valueOf(state));
		log.trace("List<Booking> ans: ", ans.toString());
		return ans.stream().map(v -> BookingMapper.toDto(v)).toList();
	}

	@GetMapping("/owner")
	public List<BookingFullDto> findByOwnerIdAndState(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@RequestParam(required = false, defaultValue = "ALL") String state) {
		log.trace("findByUserIdAndState: userId = {}, state = {}", userId, state);
		List<Booking> ans = service.findByUserIdAndState(userId, TmpState.valueOf(state));
		log.trace("List<Booking> ans: ", ans.toString());
		return ans.stream().map(v -> BookingMapper.toDto(v)).toList();
	}

	@PatchMapping(PATH_BOOKING)
	public BookingFullDto approvedByUserIdAndBookingId(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @Positive Long bookingId, @RequestParam Boolean approved) {
		log.trace("approvedByUserIdAndBookingId: ", userId, bookingId, approved);
		Booking ans = service.approvedByUserIdAndBookingId(userId, bookingId, approved);
		log.trace(ans.toString());
		return BookingMapper.toDtoSaveStatus(ans);
	}
}
