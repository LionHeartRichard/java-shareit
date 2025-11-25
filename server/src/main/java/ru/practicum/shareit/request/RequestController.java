package ru.practicum.shareit.request;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.common.dto.request.RequestDto;
import ru.practicum.shareit.common.dto.request.RequestFullDto;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RequestController {

	RequestService service;
	private static final String HEADER = "X-Sharer-User-Id";

	@PostMapping
	public RequestFullDto create(@RequestHeader(HEADER) Long userId, @RequestBody RequestDto dto) {
		log.info("<--SERVER-->  CREATE Request, userId : {}, dto: {}", userId, dto.toString());
		return service.createRequest(userId, dto);
	}

	@GetMapping
	public List<RequestFullDto> findAllRequestsByUserId(@RequestHeader(HEADER) Long userId) {
		log.info("<--SERVER-->  FIND ALL Requests, userId: {}", userId);
		return service.findAllRequestsByUserId(userId);
	}

	@GetMapping("/all")
	public List<RequestFullDto> findAllRequests(@RequestHeader(HEADER) Long userId,
			@RequestParam(name = "from", defaultValue = "0") Integer from,
			@RequestParam(name = "size", defaultValue = "50") Integer size) {
		log.info("<--SERVER-->  FIND All Requests, userId: {}, from: {}, size: {}", userId, from, size);
		return service.findAll(userId, from, size);
	}

	@GetMapping("/{requestId}")
	public RequestFullDto findRequestByUserId(@RequestHeader(HEADER) Long userId,
			@PathVariable("requestId") Long requestId) {
		log.info("<--SERVER-->  FIND RequestByUserId, userId: {}, requestId: {}", userId, requestId);
		return service.findRequestByUserId(userId, requestId);
	}
}
