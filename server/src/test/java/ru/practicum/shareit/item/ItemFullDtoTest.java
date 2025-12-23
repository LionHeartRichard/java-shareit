package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemFullDtoTest {

	private final JacksonTester<ItemFullDto> jackson;

	@Test
	void testSerialize() throws Exception {
		ItemFullDto dto = ItemFullDto.builder().id(1L).name("ItemName").description("ItemDescription").available(true)
				.lastBooking(null).nextBooking(null).comments(Collections.emptyList()).requestId(2L).build();

		JsonContent<ItemFullDto> ans = jackson.write(dto);

		assertThat(ans).hasJsonPath("$.id");
		assertThat(ans).hasJsonPath("$.name");
		assertThat(ans).hasJsonPath("$.description");
		assertThat(ans).hasJsonPath("$.available");
		assertThat(ans).hasJsonPath("$.comments");
		assertThat(ans).hasJsonPath("$.requestId");
		assertThat(ans).hasJsonPath("$.lastBooking");
		assertThat(ans).hasJsonPath("$.nextBooking");

		assertThat(ans).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
		assertThat(ans).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
		assertThat(ans).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
		assertThat(ans).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
		assertThat(ans).extractingJsonPathNumberValue("$.requestId").isEqualTo(dto.getRequestId().intValue());
		assertThat(ans).extractingJsonPathArrayValue("$.comments").isEmpty();
		assertThat(ans).extractingJsonPathValue("$.lastBooking").isNull();
		assertThat(ans).extractingJsonPathValue("$.nextBooking").isNull();
	}

	@Test
	void testDeserialize() throws Exception {
		String json = "{ " + "\"id\": 1, " + "\"name\": \"ItemName\", " + "\"description\": \"ItemDescription\", "
				+ "\"available\": true, " + "\"lastBooking\": null, " + "\"nextBooking\": null, " + "\"comments\": [], "
				+ "\"requestId\": 2 " + "}";

		ItemFullDto dto = jackson.parse(json).getObject();

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getName()).isEqualTo("ItemName");
		assertThat(dto.getDescription()).isEqualTo("ItemDescription");
		assertThat(dto.getAvailable()).isTrue();
		assertThat(dto.getLastBooking()).isNull();
		assertThat(dto.getNextBooking()).isNull();
		assertThat(dto.getComments()).isEmpty();
		assertThat(dto.getRequestId()).isEqualTo(2L);
	}
}
