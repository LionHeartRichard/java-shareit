package ru.practicum.shareit.request;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.dto.request.RequestValidDto;

@ActiveProfiles("default")
@SpringBootTest
@AutoConfigureMockMvc
class RequestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RequestClient requestClient;

	private final Long userId = 123L;
	private final Long requestId = 456L;

	@Test
	void createRequestShouldReturnCreated() throws Exception {
		RequestValidDto dto = new RequestValidDto();
		dto.setDescription("Need item for repair");

		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.CREATED);
		when(requestClient.createRequest(userId, dto)).thenReturn(response);

		mockMvc.perform(post("/requests").header("X-Sharer-User-Id", userId).contentType(MediaType.APPLICATION_JSON)
				.content("{\"description\": \"Need item for repair\"}")).andExpect(status().isCreated());

		verify(requestClient).createRequest(userId, dto);
	}

	@Test
	void getAllRequestsByIdShouldReturnOk() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(requestClient.getAllRequestsById(userId)).thenReturn(response);

		mockMvc.perform(get("/requests").header("X-Sharer-User-Id", userId)).andExpect(status().isOk());

		verify(requestClient).getAllRequestsById(userId);
	}

	@Test
	void getRequestsWithDefaultParamsShouldCallClient() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(requestClient.getRequests(userId, 0, 50)).thenReturn(response);

		mockMvc.perform(get("/requests/all").header("X-Sharer-User-Id", userId)).andExpect(status().isOk());

		verify(requestClient).getRequests(userId, 0, 50);
	}

	@Test
	void getRequestsWithCustomParamsShouldPassValues() throws Exception {
		Integer from = 10;
		Integer size = 20;

		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(requestClient.getRequests(userId, from, size)).thenReturn(response);

		mockMvc.perform(get("/requests/all").param("from", "10").param("size", "20").header("X-Sharer-User-Id", userId))
				.andExpect(status().isOk());

		verify(requestClient).getRequests(userId, from, size);
	}

	@Test
	void getRequestByIdShouldReturnOk() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(requestClient.getRequestById(userId, requestId)).thenReturn(response);

		mockMvc.perform(get("/requests/456").header("X-Sharer-User-Id", userId)).andExpect(status().isOk());

		verify(requestClient).getRequestById(userId, requestId);
	}
}
