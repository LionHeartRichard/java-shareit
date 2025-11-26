package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.common.dto.user.UserFullDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserFullDtoTest {

	private final JacksonTester<UserFullDto> json;

	@Test
	void testSerialize() throws Exception {
		UserFullDto dto = new UserFullDto();
		dto.setId(1L);
		dto.setName("name");
		dto.setEmail("email@email.ru");

		JsonContent<UserFullDto> ans = json.write(dto);
		assertThat(ans).hasJsonPath("$.id");
		assertThat(ans).hasJsonPath("$.name");
		assertThat(ans).hasJsonPath("$.email");
		assertThat(ans).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
		assertThat(ans).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
		assertThat(ans).extractingJsonPathStringValue("$.email").isEqualTo(dto.getEmail());
	}

	@Test
	void testDeserialize() throws Exception {
		String jsonString = "{ \"id\": 1, \"name\": \"name\", \"email\": \"email@email.ru\" }";

		UserFullDto dto = json.parse(jsonString).getObject();

		assertThat(dto).isNotNull();
		assertThat(dto.getId()).isEqualTo(1L);
		assertThat(dto.getName()).isEqualTo("name");
		assertThat(dto.getEmail()).isEqualTo("email@email.ru");
	}
}
