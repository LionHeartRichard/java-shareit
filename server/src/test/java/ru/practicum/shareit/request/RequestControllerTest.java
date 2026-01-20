package ru.practicum.shareit.request;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.common.dto.request.RequestDto;
import ru.practicum.shareit.common.dto.request.RequestFullDto;

@WebMvcTest(controllers = RequestController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestControllerTest {

	private static final String HEADER = "X-Sharer-User-Id";

	@Autowired
	ObjectMapper mapper;

	@MockBean
	RequestService service;

	@Autowired
	MockMvc mvc;

	final RequestDto dto = RequestDto.builder().description("description").build();

	final ItemFullDto itemDto = ItemFullDto.builder().id(1L).name("name").available(true).description("description")
			.build();

	final ItemFullDto itemDtoOther = ItemFullDto.builder().id(2L).name("nameOther").available(true)
			.description("otherDescription").build();

	final RequestFullDto ans = RequestFullDto.builder().id(1L).description("request description")
			.created(LocalDateTime.of(2022, 2, 27, 20, 35, 0)).requesterName("requesterName")
			.items(List.of(itemDto, itemDtoOther)).build();

	final RequestFullDto ansOther = RequestFullDto.builder().id(2L).description("description OTHER")
			.created(LocalDateTime.of(2022, 5, 28, 21, 36, 5)).requesterName("requesterName OTHER")
			.items(List.of(itemDto)).build();

	private static final Long USER_ID = 3L;
	private static final Long REQUEST_ID = 1L;

	@Test
	void createRequestTest() throws Exception {
		when(service.createRequest(USER_ID, dto)).thenReturn(ans);

		mvc.perform(post("/requests").header(HEADER, USER_ID).content(mapper.writeValueAsString(dto))
				.characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ans.getId()))
				.andExpect(jsonPath("$.description").value(ans.getDescription()))
				.andExpect(jsonPath("$.requesterName").value(ans.getRequesterName()))
				.andExpect(jsonPath("$.created").value("2022-02-27T20:35:00"))
				.andExpect(jsonPath("$.items", hasSize(2))).andExpect(jsonPath("$.items[0].id").value(itemDto.getId()))
				.andExpect(jsonPath("$.items[1].id").value(itemDtoOther.getId()));
	}

	@Test
	void getAllRequestsByIdTest() throws Exception {
		when(service.findAllRequestsByUserId(USER_ID)).thenReturn(List.of(ans));

		mvc.perform(get("/requests").header(HEADER, USER_ID).characterEncoding(StandardCharsets.UTF_8)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].description").value(ans.getDescription()))
				.andExpect(jsonPath("$[0].requesterName").value(ans.getRequesterName()))
				.andExpect(jsonPath("$[0].created").value("2022-02-27T20:35:00"))
				.andExpect(jsonPath("$[0].items", hasSize(2)))
				.andExpect(jsonPath("$[0].items[0].id").value(itemDto.getId()))
				.andExpect(jsonPath("$[0].items[1].id").value(itemDtoOther.getId()));
	}

	@Test
	void getAllRequestsTest() throws Exception {
		when(service.findAll(USER_ID, 0, 50)).thenReturn(List.of(ans, ansOther));
		mvc.perform(get("/requests/all").header(HEADER, USER_ID).characterEncoding(StandardCharsets.UTF_8)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(ans.getId()))
				.andExpect(jsonPath("$[0].description").value(ans.getDescription()))
				.andExpect(jsonPath("$[0].requesterName").value(ans.getRequesterName()))
				.andExpect(jsonPath("$[0].created").value("2022-02-27T20:35:00"))
				.andExpect(jsonPath("$[0].items", hasSize(2)))
				.andExpect(jsonPath("$[0].items[0].id").value(itemDto.getId()))
				.andExpect(jsonPath("$[0].items[1].id").value(itemDtoOther.getId()))
				.andExpect(jsonPath("$[1].id").value(ansOther.getId()))
				.andExpect(jsonPath("$[1].description").value(ansOther.getDescription()))
				.andExpect(jsonPath("$[1].requesterName").value(ansOther.getRequesterName()))
				.andExpect(jsonPath("$[1].created").value("2022-05-28T21:36:05"))
				.andExpect(jsonPath("$[1].items", hasSize(1)))
				.andExpect(jsonPath("$[1].items[0].id").value(itemDto.getId()));
	}

	@Test
	void getRequestByIdTest() throws Exception {
		when(service.findRequestByUserId(USER_ID, REQUEST_ID)).thenReturn(ans);

		mvc.perform(get("/requests/1").header(HEADER, USER_ID).characterEncoding(StandardCharsets.UTF_8)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ans.getId()))
				.andExpect(jsonPath("$.description").value(ans.getDescription()))
				.andExpect(jsonPath("$.requesterName").value(ans.getRequesterName()))
				.andExpect(jsonPath("$.created").value("2022-02-27T20:35:00"))
				.andExpect(jsonPath("$.items", hasSize(2))).andExpect(jsonPath("$.items[0].id").value(itemDto.getId()))
				.andExpect(jsonPath("$.items[1].id").value(itemDtoOther.getId()));
	}
}
