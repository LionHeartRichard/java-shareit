package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.BookingStatus;
import ru.practicum.shareit.common.StateBooking;
import ru.practicum.shareit.common.dto.booking.BookingDto;
import ru.practicum.shareit.common.dto.booking.BookingFullDto;
import ru.practicum.shareit.common.dto.item.ItemDto;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.common.dto.user.UserDto;
import ru.practicum.shareit.common.dto.user.UserFullDto;
import ru.practicum.shareit.common.exception.MyBadRequestException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceTest {

	@Autowired
	ItemService itemService;

	@Autowired
	UserService userService;

	@Autowired
	BookingService bookingService;

	UserDto dtoUser;
	UserDto dtoUserOther;
	ItemDto dtoItem;
	UserFullDto ansUser;
	UserFullDto ansUserOther;
	ItemFullDto ansItem;

	@BeforeEach
	void setUp() {
		dtoUser = UserDto.builder().name("name-User").email("email@mail.com").build();
		dtoUserOther = UserDto.builder().name("name-User-Other").email("other2@mail.com").build();
		dtoItem = ItemDto.builder().name("item").description("item-desc").available(true).build();

		ansUser = userService.createUser(dtoUser);
		ansUserOther = userService.createUser(dtoUserOther);
		ansItem = itemService.createItem(ansUser.getId(), dtoItem);
	}

	@Test
	void createBookingTest() {
		BookingDto dto = BookingDto.builder().itemId(ansItem.getId()).start(LocalDateTime.now().plusHours(1))
				.end(LocalDateTime.now().plusHours(2)).build();

		BookingFullDto ans = bookingService.createBooking(ansUserOther.getId(), dto);

		assertThat(ans.getId()).isNotNull();
		assertThat(ans.getStart()).isEqualTo(dto.getStart().truncatedTo(ChronoUnit.MILLIS));
		assertThat(ans.getEnd()).isEqualTo(dto.getEnd().truncatedTo(ChronoUnit.MILLIS));
		assertThat(ans.getItem().getId()).isEqualTo(dto.getItemId());
		assertThat(ans.getBooker().getId()).isEqualTo(ansUserOther.getId());
		assertThat(ans.getStatus()).isEqualTo(BookingStatus.WAITING);
	}

	@Test
	void findByUserIdAndBookingIdTest() {
		BookingDto dto = BookingDto.builder().itemId(ansItem.getId()).start(LocalDateTime.now().plusHours(1))
				.end(LocalDateTime.now().plusHours(2)).build();

		BookingFullDto expected = bookingService.createBooking(ansUserOther.getId(), dto);
		BookingFullDto actual = bookingService.findByUserIdAndBookingId(ansUserOther.getId(), expected.getId());
		BookingFullDto actualOther = bookingService.findByUserIdAndBookingId(ansUser.getId(), expected.getId());

		assertThat(actual.getId()).isEqualTo(expected.getId());
		assertThat(actual.getStart()).isEqualTo(expected.getStart());
		assertThat(actual.getEnd()).isEqualTo(expected.getEnd());
		assertThat(actual.getItem()).isEqualTo(expected.getItem());
		assertThat(actual.getBooker()).isEqualTo(ansUserOther);
		assertThat(actual.getStatus()).isEqualTo(BookingStatus.WAITING);

		assertThat(actualOther.getId()).isEqualTo(expected.getId());
		assertThat(actualOther.getStart()).isEqualTo(expected.getStart());
		assertThat(actualOther.getEnd()).isEqualTo(expected.getEnd());
		assertThat(actualOther.getItem()).isEqualTo(expected.getItem());
		assertThat(actualOther.getBooker()).isEqualTo(ansUserOther);
		assertThat(actualOther.getStatus()).isEqualTo(BookingStatus.WAITING);
	}

	@Test
	void findByUserIdAndStateTest() {
		BookingDto dto = BookingDto.builder().itemId(ansItem.getId()).start(LocalDateTime.now().plusHours(1))
				.end(LocalDateTime.now().plusHours(2)).build();

		BookingFullDto expexted = bookingService.createBooking(ansUserOther.getId(), dto);
		List<BookingFullDto> actual = bookingService.findByUserIdAndState(ansUserOther.getId(), StateBooking.ALL);

		assertThat(actual.getFirst().getId()).isEqualTo(expexted.getId());
		assertThat(actual.getFirst().getStart()).isEqualTo(expexted.getStart());
		assertThat(actual.getFirst().getEnd()).isEqualTo(expexted.getEnd());
		assertThat(actual.getFirst().getItem()).isEqualTo(expexted.getItem());
		assertThat(actual.getFirst().getBooker()).isEqualTo(ansUserOther);
		assertThat(actual.getFirst().getStatus()).isEqualTo(BookingStatus.WAITING);
	}

	@Test
	void updateStatusBookingTest() {
		BookingDto dto = BookingDto.builder().itemId(ansItem.getId()).start(LocalDateTime.now().plusHours(1))
				.end(LocalDateTime.now().plusHours(2)).build();

		BookingFullDto ans = bookingService.createBooking(ansUserOther.getId(), dto);
		BookingFullDto upStatus = bookingService.approvedByUserIdAndBookingId(ansUser.getId(), ans.getId(), true);

		assertThat(upStatus.getId()).isEqualTo(ans.getId());
		assertThat(upStatus.getStart()).isEqualTo(ans.getStart());
		assertThat(upStatus.getEnd()).isEqualTo(ans.getEnd());
		assertThat(upStatus.getItem()).isEqualTo(ans.getItem());
		assertThat(upStatus.getBooker()).isEqualTo(ansUserOther);
		assertThat(upStatus.getStatus()).isEqualTo(BookingStatus.APPROVED);
	}

	@Test
	void throwExceptionWhenItemAvailableIsFalseTest() {
		ItemDto falseDto = dtoItem.toBuilder().available(false).build();
		ItemFullDto fullDto = itemService.createItem(ansUser.getId(), falseDto);
		BookingDto dto = BookingDto.builder().itemId(fullDto.getId()).start(LocalDateTime.now().plusHours(1))
				.end(LocalDateTime.now().plusHours(2)).build();

		assertThatThrownBy(() -> bookingService.createBooking(ansUserOther.getId(), dto))
				.isInstanceOf(MyBadRequestException.class);
	}

	@Test
	void throwExceptionWhenBookingStartCurrentTimeMoreThanTheEndTimeTest() {
		BookingDto dto = BookingDto.builder().itemId(ansItem.getId()).start(LocalDateTime.now().plusHours(2))
				.end(LocalDateTime.now().plusHours(1)).build();

		assertThatThrownBy(() -> bookingService.createBooking(ansUserOther.getId(), dto))
				.isInstanceOf(MyBadRequestException.class);
	}

	@Test
	void throwExceptionWhenOwnerCreateBooking() {
		BookingDto dto = BookingDto.builder().itemId(ansItem.getId()).start(LocalDateTime.now().plusHours(1))
				.end(LocalDateTime.now().plusHours(2)).build();

		assertThatThrownBy(() -> bookingService.createBooking(ansUser.getId(), dto))
				.isInstanceOf(MyBadRequestException.class);
	}

	@Test
	void throwExceptionWhenUserIsNotOwnerWhenUpdateBooking() {
		BookingDto dto = BookingDto.builder().itemId(ansItem.getId()).start(LocalDateTime.now().plusHours(1))
				.end(LocalDateTime.now().plusHours(2)).build();

		BookingFullDto ans = bookingService.createBooking(ansUserOther.getId(), dto);

		assertThatThrownBy(() -> bookingService.approvedByUserIdAndBookingId(ansUserOther.getId(), ans.getId(), true))
				.isInstanceOf(MyBadRequestException.class);
	}

}
