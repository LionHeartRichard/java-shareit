package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.common.dto.booking.BookingDto;
import ru.practicum.shareit.common.dto.booking.BookingFullDto;
import ru.practicum.shareit.common.dto.comment.CommentDto;
import ru.practicum.shareit.common.dto.comment.CommentTextDto;
import ru.practicum.shareit.common.dto.item.ItemDto;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.common.dto.request.RequestDto;
import ru.practicum.shareit.common.dto.request.RequestFullDto;
import ru.practicum.shareit.common.dto.user.UserDto;
import ru.practicum.shareit.common.dto.user.UserFullDto;
import ru.practicum.shareit.common.exception.MyBadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.request.RequestService;
import ru.practicum.shareit.user.UserService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceTest {

	@Autowired
	ItemService itemService;

	@Autowired
	UserService userService;

	@Autowired
	BookingService bookingService;

	@Autowired
	RequestService requestService;

	UserDto user;
	UserDto otherUser;
	ItemDto hammerDrill;
	ItemDto electricScooter;

	@BeforeEach
	void setUp() {
		user = UserDto.builder().name("Name-User").email("email@mail.com").build();
		otherUser = UserDto.builder().name("Name-OTHER").email("other@mail.com").build();
		hammerDrill = ItemDto.builder().name("hammer drill").description("description hammer drill").available(true)
				.build();
		electricScooter = ItemDto.builder().name("electric scooter").description("description electric scooter")
				.available(true).build();
	}

	@Test
	void createAndFindItemTest() {
		UserFullDto ans = userService.createUser(user);
		ItemFullDto expected = itemService.createItem(ans.getId(), hammerDrill);
		ItemFullDto actual = itemService.findItemById(ans.getId(), expected.getId());

		assertThat(expected.getId()).isEqualTo(actual.getId());
		assertThat(expected.getName()).isEqualTo(actual.getName());
		assertThat(expected.getDescription()).isEqualTo(actual.getDescription());
		assertThat(expected.getAvailable()).isEqualTo(actual.getAvailable());
		assertThat(expected.getRequestId()).isNull();
	}

	@Test
	void createItemWithRequestTest() {
		UserFullDto ans = userService.createUser(user);
		UserFullDto ansOther = userService.createUser(otherUser);
		RequestDto dto = RequestDto.builder().description("desc-request").build();
		RequestFullDto itemRequest = requestService.createRequest(ansOther.getId(), dto);
		ItemDto carabiner = ItemDto.builder().name("carabiner").description("carabiner - for - Climbing")
				.available(true).requestId(itemRequest.getId()).build();
		ItemFullDto expected = itemService.createItem(ans.getId(), carabiner);
		ItemFullDto actual = itemService.findItemById(ans.getId(), expected.getId());
		assertThat(expected.getId()).isEqualTo(actual.getId());
		assertThat(expected.getName()).isEqualTo(actual.getName());
		assertThat(expected.getDescription()).isEqualTo(actual.getDescription());
		assertThat(expected.getAvailable()).isEqualTo(actual.getAvailable());
		assertThat(expected.getRequestId()).isEqualTo(actual.getRequestId());
	}

	@Test
	void updateItemTest() {
		ItemDto upItem = ItemDto.builder().name(electricScooter.getName()).description(electricScooter.getDescription())
				.available(true).build();
		UserFullDto ans = userService.createUser(user);
		ItemFullDto item = itemService.createItem(ans.getId(), hammerDrill);
		ItemFullDto actual = itemService.updateItem(ans.getId(), item.getId(), upItem);

		assertThat(actual.getId()).isEqualTo(item.getId());
		assertThat(actual.getName()).isEqualTo(electricScooter.getName());
		assertThat(actual.getDescription()).isEqualTo(electricScooter.getDescription());
		assertThat(actual.getAvailable()).isEqualTo(electricScooter.getAvailable());
	}

	@Test
	void updateItemNameIsNullTest() {
		UserFullDto ans = userService.createUser(user);
		ItemDto expected = ItemDto.builder().description("Up").available(false).build();
		ItemFullDto beforeItem = itemService.createItem(ans.getId(), hammerDrill);
		ItemFullDto actual = itemService.updateItem(ans.getId(), beforeItem.getId(), expected);

		assertThat(actual.getId()).isEqualTo(beforeItem.getId());
		assertThat(actual.getName()).isEqualTo(hammerDrill.getName());
		assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
		assertThat(actual.getAvailable()).isEqualTo(expected.getAvailable());
	}

	@Test
	void updateItemDescriptionIsNullTest() {
		UserFullDto ans = userService.createUser(user);
		ItemDto expected = ItemDto.builder().name("up_name").available(false).build();
		ItemFullDto beforeItem = itemService.createItem(ans.getId(), hammerDrill);
		ItemFullDto actual = itemService.updateItem(ans.getId(), beforeItem.getId(), expected);

		assertThat(actual.getId()).isEqualTo(beforeItem.getId());
		assertThat(actual.getName()).isEqualTo(expected.getName());
		assertThat(actual.getDescription()).isEqualTo(hammerDrill.getDescription());
		assertThat(actual.getAvailable()).isEqualTo(expected.getAvailable());
	}

	@Test
	void updateItemAvaliableIsNullTest() {
		UserFullDto ans = userService.createUser(user);
		ItemDto expected = ItemDto.builder().name("up-item-NAME").description("up-item-DESCRIPTION").build();
		ItemFullDto beforeItem = itemService.createItem(ans.getId(), hammerDrill);
		ItemFullDto actual = itemService.updateItem(ans.getId(), beforeItem.getId(), expected);

		assertThat(actual.getId()).isEqualTo(beforeItem.getId());
		assertThat(actual.getName()).isEqualTo(expected.getName());
		assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
		assertThat(actual.getAvailable()).isEqualTo(hammerDrill.getAvailable());
	}

	@Test
	void findItemsByOwnerTest() {
		UserFullDto ans = userService.createUser(user);
		ItemFullDto expected = itemService.createItem(ans.getId(), hammerDrill);
		ItemFullDto expectedOther = itemService.createItem(ans.getId(), electricScooter);

		List<ItemFullDto> actual = itemService.findItemsByOwner(ans.getId()).stream().toList();

		assertThat(actual).hasSize(2);
		assertThat(actual.get(0).getId()).isEqualTo(expected.getId());
		assertThat(actual.get(0).getName()).isEqualTo(expected.getName());
		assertThat(actual.get(0).getDescription()).isEqualTo(expected.getDescription());
		assertThat(actual.get(0).getAvailable()).isEqualTo(expected.getAvailable());

		assertThat(actual.get(1).getId()).isEqualTo(expectedOther.getId());
		assertThat(actual.get(1).getName()).isEqualTo(expectedOther.getName());
		assertThat(actual.get(1).getDescription()).isEqualTo(expectedOther.getDescription());
		assertThat(actual.get(1).getAvailable()).isEqualTo(expectedOther.getAvailable());
	}

	@Test
	void addCommentToItemTest() {
		UserFullDto owner = userService.createUser(user);
		UserFullDto ans = userService.createUser(otherUser);
		ItemFullDto fullDtoItem = itemService.createItem(owner.getId(), hammerDrill);

		BookingDto dtoBoking = BookingDto.builder().itemId(fullDtoItem.getId())
				.start(LocalDateTime.of(2025, 7, 11, 3, 5)).end(LocalDateTime.of(2025, 7, 11, 3, 5).plusSeconds(1))
				.build();
		BookingFullDto ansBoking = bookingService.createBooking(ans.getId(), dtoBoking);
		bookingService.approvedByUserIdAndBookingId(owner.getId(), ansBoking.getId(), true);

		String comment = "comment - text - for add item";
		CommentDto actual = itemService.addComment(ans.getId(), fullDtoItem.getId(), comment);

		assertThat(actual.getText()).isEqualTo(comment);
		assertThat(actual.getAuthorName()).isEqualTo(ans.getName());
	}

	@Test
	void throwExceptionWhenOwnerTryToCommentTest() {
		UserFullDto owner = userService.createUser(user);
		ItemFullDto fullDtoItem = itemService.createItem(owner.getId(), hammerDrill);
		BookingDto booking = BookingDto.builder().itemId(fullDtoItem.getId()).start(LocalDateTime.now())
				.end(LocalDateTime.now().plusSeconds(1)).build();
		UserFullDto notOwner = userService.createUser(otherUser);
		bookingService.createBooking(notOwner.getId(), booking);
		String comment = "Text";

		assertThatThrownBy(() -> itemService.addComment(owner.getId(), fullDtoItem.getId(), comment))
				.isInstanceOf(MyBadRequestException.class);
	}

	@Test
	void throwExceptionWhenUserIsNotOwnerWhenUpdateItemTest() {
		ItemDto dto = ItemDto.builder().name("Yandex").description("YandexPracticum").available(true).build();
		UserFullDto owner = userService.createUser(user);
		UserFullDto notOwner = userService.createUser(otherUser);
		ItemFullDto actual = itemService.createItem(owner.getId(), hammerDrill);

		assertThatThrownBy(() -> itemService.updateItem(notOwner.getId(), actual.getId(), dto))
				.isInstanceOf(MyBadRequestException.class);
	}

	@Test
	void throwExceptionWhenUserIsNotFoundTest() {
		assertThatThrownBy(() -> itemService.createItem(777L, hammerDrill)).isInstanceOf(NotFoundException.class);
	}

	@Test
	void throwExceptionWhenIdIsNullTest() {
		assertThatThrownBy(() -> itemService.createItem(null, hammerDrill))
				.isInstanceOf(InvalidDataAccessApiUsageException.class);
	}

}
