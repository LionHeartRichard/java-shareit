package ru.practicum.shareit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class BaseClientTest {

	@Mock
	private RestTemplate restTemplate;

	private BaseClient baseClient;

	@BeforeEach
	void setUp() {
		baseClient = new BaseClient(restTemplate);
	}

	@Test
	void getWithoutUserIdShouldCallRestWithNoHeader() {
		ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.OK);
		when(restTemplate.exchange(eq("/path"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
				.thenReturn(mockResponse);

		ResponseEntity<Object> result = baseClient.get("/path");

		assertEquals(HttpStatus.OK, result.getStatusCode());
		verify(restTemplate).exchange(eq("/path"), eq(HttpMethod.GET), argThat(entity -> {
			HttpHeaders headers = entity.getHeaders();
			return headers.getContentType().equals(MediaType.APPLICATION_JSON)
					&& headers.getAccept().contains(MediaType.APPLICATION_JSON)
					&& !headers.containsKey("X-Sharer-User-Id");
		}), eq(Object.class));
	}

	@Test
	void postWithBodyShouldSendJsonBodyAndHeaders() {
		Map<String, String> body = Map.of("key", "value");
		ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.CREATED);
		when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
				.thenReturn(mockResponse);

		ResponseEntity<Object> result = baseClient.post("/path", body);

		assertEquals(HttpStatus.CREATED, result.getStatusCode());
		verify(restTemplate).exchange(anyString(), eq(HttpMethod.POST), argThat(entity -> {
			HttpHeaders headers = entity.getHeaders();
			return headers.getContentType().equals(MediaType.APPLICATION_JSON) && entity.getBody().equals(body);
		}), eq(Object.class));
	}

	@Test
	void putWithUserIdAndBodyShouldIncludeHeaderAndBody() {
		String body = "updated data";
		ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		when(restTemplate.exchange(eq("/path"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class)))
				.thenReturn(mockResponse);

		baseClient.put("/path", 456L, body);

		verify(restTemplate).exchange(eq("/path"), eq(HttpMethod.PUT), argThat(entity -> {
			HttpHeaders headers = entity.getHeaders();
			return headers.getFirst("X-Sharer-User-Id").equals("456") && entity.getBody().equals(body);
		}), eq(Object.class));
	}

	@Test
	void patchWithUserIdAndBodyShouldWork() {
		Map<String, Object> body = new HashMap<>();
		body.put("status", "approved");
		ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.ACCEPTED);
		when(restTemplate.exchange(eq("/path"), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class)))
				.thenReturn(mockResponse);

		baseClient.patch("/path", 789L, body);

		verify(restTemplate).exchange(eq("/path"), eq(HttpMethod.PATCH), argThat(entity -> {
			HttpHeaders headers = entity.getHeaders();
			return headers.getFirst("X-Sharer-User-Id").equals("789") && entity.getBody().equals(body);
		}), eq(Object.class));
	}

	@Test
	void deleteWithUserIdShouldIncludeHeader() {
		ResponseEntity<Object> mockResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		when(restTemplate.exchange(eq("/path"), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Object.class)))
				.thenReturn(mockResponse);

		baseClient.delete("/path", 111L);

		verify(restTemplate).exchange(eq("/path"), eq(HttpMethod.DELETE), argThat(entity -> {
			HttpHeaders headers = entity.getHeaders();
			return headers.getFirst("X-Sharer-User-Id").equals("111");
		}), eq(Object.class));
	}

	// --- TEST FOR ERROR HANDLING ---

	@Test
	void makeAndSendRequestWhenHttpStatusCodeExceptionShouldReturnErrorResponse() {
		HttpStatusCodeException mockException = mock(HttpStatusCodeException.class);
		when(mockException.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
		when(mockException.getResponseBodyAsByteArray()).thenReturn("Error body".getBytes());

		when(restTemplate.exchange(anyString(), any(), any(), eq(Object.class))).thenThrow(mockException);

		ResponseEntity<Object> result = baseClient.get("/error-path", 1L);

		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertEquals("Error body", new String((byte[]) result.getBody()));
	}
}