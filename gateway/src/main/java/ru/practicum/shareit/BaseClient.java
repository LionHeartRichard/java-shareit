package ru.practicum.shareit;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class BaseClient {

	protected final RestTemplate rest;

	public BaseClient(final RestTemplate rest) {
		this.rest = rest;
	}

	protected ResponseEntity<Object> get(final String path) {
		return get(path, null, null);
	}

	protected ResponseEntity<Object> get(final String path, final Long userId) {
		return get(path, userId, null);
	}

	protected ResponseEntity<Object> get(final String path, final Long userId, final Map<String, Object> parameters) {
		return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
	}

	protected <T> ResponseEntity<Object> post(final String path, final T body) {
		return post(path, null, null, body);
	}

	protected <T> ResponseEntity<Object> post(final String path, final Long userId, final T body) {
		return post(path, userId, null, body);
	}

	protected <T> ResponseEntity<Object> post(final String path, final Long userId,
			final Map<String, Object> parameters, final T body) {
		return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
	}

	protected <T> ResponseEntity<Object> put(final String path, final Long userId, final T body) {
		return put(path, userId, null, body);
	}

	protected <T> ResponseEntity<Object> put(final String path, final Long userId, final Map<String, Object> parameters,
			final T body) {
		return makeAndSendRequest(HttpMethod.PUT, path, userId, parameters, body);
	}

	protected <T> ResponseEntity<Object> patch(final String path, final T body) {
		return patch(path, null, null, body);
	}

	protected <T> ResponseEntity<Object> patch(final String path, final Long userId) {
		return patch(path, userId, null, null);
	}

	protected <T> ResponseEntity<Object> patch(final String path, final Long userId, final T body) {
		return patch(path, userId, null, body);
	}

	protected <T> ResponseEntity<Object> patch(final String path, final Long userId,
			final Map<String, Object> parameters, final T body) {
		return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
	}

	protected ResponseEntity<Object> delete(final String path) {
		return delete(path, null, null);
	}

	protected ResponseEntity<Object> delete(final String path, final Long userId) {
		return delete(path, userId, null);
	}

	protected ResponseEntity<Object> delete(final String path, final Long userId,
			final Map<String, Object> parameters) {
		return makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
	}

	private <T> ResponseEntity<Object> makeAndSendRequest(final HttpMethod method, final String path, final Long userId,
			final Map<String, Object> parameters, final T body) {
		HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

		ResponseEntity<Object> shareitServerResponse;
		try {
			if (parameters != null) {
				shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
				return prepareGatewayResponse(shareitServerResponse);
			}
			shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
			return prepareGatewayResponse(shareitServerResponse);
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
		}

	}

	private HttpHeaders defaultHeaders(final Long userId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		if (userId != null) {
			headers.set("X-Sharer-User-Id", String.valueOf(userId));
		}
		return headers;
	}

	private static ResponseEntity<Object> prepareGatewayResponse(final ResponseEntity<Object> response) {
		if (response.getStatusCode().is2xxSuccessful()) {
			return response;
		}

		ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

		if (response.hasBody()) {
			return responseBuilder.body(response.getBody());
		}

		return responseBuilder.build();
	}
}