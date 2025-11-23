package ru.practicum.shareit.request;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import ru.practicum.shareit.common.dto.request.ValidRequestDto;

import ru.practicum.shareit.BaseClient;

@Component
public class RequestClient extends BaseClient {

	private static final String API = "/requests";

	@Autowired
	public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API))
				.requestFactory(() -> new HttpComponentsClientHttpRequestFactory()).build());
	}

	public ResponseEntity<Object> getRequests(Long userId, Integer from, Integer size) {
		Map<String, Object> parameters = Map.of("from", from, "size", size);
		return get("/all?from={from}&size={size}", userId, parameters);
	}

	public ResponseEntity<Object> getRequestById(Long userId, Long requestId) {
		return get("/" + requestId, userId);
	}

	public ResponseEntity<Object> createRequest(Long userId, ValidRequestDto dto) {
		return post("", userId, dto);
	}

	public ResponseEntity<Object> getAllRequestsById(Long userId) {
		return get("", userId);
	}

}
