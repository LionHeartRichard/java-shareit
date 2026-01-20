package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.common.BookingStatus;
import ru.practicum.shareit.common.dto.booking.BookingFullDto;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.common.dto.user.UserFullDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import java.time.LocalDateTime;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingFullDtoTest {

	private final JacksonTester<BookingFullDto> jackson;

	@Test
	void testSerialize() throws Exception {
		UserFullDto userFullDto = UserFullDto.builder().id(1L).name("BookerName").email("booker@email.ru").build();

		ItemFullDto itemFullDto = ItemFullDto.builder().id(2L).name("ItemName").description("ItemDescription")
				.available(true).comments(Collections.emptyList()).requestId(3L).build();

		BookingFullDto bookingFullDto = BookingFullDto.builder().id(1L).start(LocalDateTime.of(2024, 10, 1, 10, 0))
				.end(LocalDateTime.of(2024, 10, 2, 10, 0)).status(BookingStatus.APPROVED).booker(userFullDto)
				.item(itemFullDto).build();

		JsonContent<BookingFullDto> ans = jackson.write(bookingFullDto);

		assertThat(ans).hasJsonPath("$.id");
		assertThat(ans).hasJsonPath("$.start");
		assertThat(ans).hasJsonPath("$.end");
		assertThat(ans).hasJsonPath("$.status");
		assertThat(ans).hasJsonPath("$.booker");
		assertThat(ans).hasJsonPath("$.item");

		assertThat(ans).extractingJsonPathNumberValue("$.id").isEqualTo(bookingFullDto.getId().intValue());
		assertThat(ans).extractingJsonPathStringValue("$.status").isEqualTo(bookingFullDto.getStatus().toString());

		assertThat(ans).extractingJsonPathNumberValue("$.booker.id").isEqualTo(userFullDto.getId().intValue());
		assertThat(ans).extractingJsonPathStringValue("$.booker.name").isEqualTo(userFullDto.getName());
		assertThat(ans).extractingJsonPathStringValue("$.booker.email").isEqualTo(userFullDto.getEmail());

		assertThat(ans).extractingJsonPathNumberValue("$.item.id").isEqualTo(itemFullDto.getId().intValue());
		assertThat(ans).extractingJsonPathStringValue("$.item.name").isEqualTo(itemFullDto.getName());
		assertThat(ans).extractingJsonPathStringValue("$.item.description").isEqualTo(itemFullDto.getDescription());
		assertThat(ans).extractingJsonPathBooleanValue("$.item.available").isEqualTo(itemFullDto.getAvailable());
		assertThat(ans).extractingJsonPathNumberValue("$.item.requestId")
				.isEqualTo(itemFullDto.getRequestId().intValue());
	}

	@Test
	void testDeserialize() throws Exception {
		String json = "{ " + "\"id\": 1, " + "\"start\": \"2023-10-01T10:00:00\", "
				+ "\"end\": \"2023-10-02T10:00:00\", " + "\"status\": \"APPROVED\", "
				+ "\"booker\": { \"id\": 1, \"name\": \"BookerName\", \"email\": \"booker@email.ru\" }, "
				+ "\"item\": { \"id\": 2, \"name\": \"ItemName\", \"description\": \"ItemDescription\", \"available\": true, \"requestId\": 3 } "
				+ "}";

		BookingFullDto dto = jackson.parse(json).getObject();

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2023, 10, 1, 10, 0));
		assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2023, 10, 2, 10, 0));
		assertThat(dto.getStatus()).isEqualTo(BookingStatus.APPROVED);

		assertThat(dto.getBooker()).isNotNull();
		assertThat(dto.getBooker().getId()).isEqualTo(1L);
		assertThat(dto.getBooker().getName()).isEqualTo("BookerName");
		assertThat(dto.getBooker().getEmail()).isEqualTo("booker@email.ru");

		assertThat(dto.getItem()).isNotNull();
		assertThat(dto.getItem().getId()).isEqualTo(2L);
		assertThat(dto.getItem().getName()).isEqualTo("ItemName");
		assertThat(dto.getItem().getDescription()).isEqualTo("ItemDescription");
		assertThat(dto.getItem().getAvailable()).isTrue();
		assertThat(dto.getItem().getRequestId()).isEqualTo(3L);
	}
}
