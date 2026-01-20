package ru.practicum.shareit.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.dto.request.RequestValidDto;

@RestController
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RequestController {

	RequestClient client;

	private static final String HEADER = "X-Sharer-User-Id";

	@PostMapping
	public ResponseEntity<Object> createRequest(@RequestHeader(HEADER) Long userId,
			@Valid @RequestBody RequestValidDto dto) {
		return client.createRequest(userId, dto);
	}

	@GetMapping
	public ResponseEntity<Object> getAllRequestsById(@RequestHeader(HEADER) Long userId) {
		return client.getAllRequestsById(userId);
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getRequests(@RequestHeader(HEADER) Long userId,
			@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
			@RequestParam(name = "size", defaultValue = "50") @Positive Integer size) {
		return client.getRequests(userId, from, size);
	}

	@GetMapping("/{requestId}")
	public ResponseEntity<Object> getRequestById(@RequestHeader(HEADER) Long userId,
			@PathVariable("requestId") Long requestId) {
		return client.getRequestById(userId, requestId);
	}
}
