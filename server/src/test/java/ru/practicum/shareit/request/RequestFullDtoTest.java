package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.common.dto.request.RequestFullDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestFullDtoTest {
	
	private final JacksonTester<RequestFullDto> jackson;

	@Test
	void testSerialize() throws Exception {
		RequestFullDto requestDto = RequestFullDto.builder().id(1L).description("description")
				.requesterName("requesterName").created(LocalDateTime.now()).items(new ArrayList<>()).build();

		JsonContent<RequestFullDto> result = jackson.write(requestDto);

		assertThat(result).hasJsonPath("$.id");
		assertThat(result).hasJsonPath("$.description");
		assertThat(result).hasJsonPath("$.requesterName");
		assertThat(result).hasJsonPath("$.created");
		assertThat(result).hasJsonPathValue("$.created");
		assertThat(result).hasJsonPath("$.items");
		assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(requestDto.getId().intValue());
		assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(requestDto.getDescription());
		assertThat(result).extractingJsonPathStringValue("$.requesterName").isEqualTo(requestDto.getRequesterName());
		assertThat(result).extractingJsonPathArrayValue("$.items");
	}

	@Test
	void testDeserialize() throws Exception {
		String json = "{ \"id\": 1, \"description\": \"description\", \"requesterName\": \"requesterName\", "
				+ "\"created\": \"2022-02-27T20:35:00\", \"items\": [] }";

		RequestFullDto dto = this.jackson.parse(json).getObject();

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getDescription()).isEqualTo("description");
		assertThat(dto.getRequesterName()).isEqualTo("requesterName");
		assertThat(dto.getCreated()).isEqualTo(LocalDateTime.parse("2022-02-27T20:35:00"));
		assertThat(dto.getItems()).isEmpty();
	}
}