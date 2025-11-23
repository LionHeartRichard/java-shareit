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
import ru.practicum.shareit.common.dto.request.CreateRequestDto;
import ru.practicum.shareit.common.dto.request.RequestDto;
import ru.practicum.shareit.item.Item;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RequestController {

	RequestService service;

	private static final String HEADER = "X-Sharer-User-Id";

	@PostMapping
	public RequestDto create(@RequestHeader(HEADER) Long userId, @RequestBody CreateRequestDto dto) {
		log.info("CreateRequestDto, userId : {}", userId);
		log.info("CreateRequestDto: {}", dto.toString());
		return RequestMapper.toDto(service.create(userId, RequestMapper.toModel(dto)));
	}

	@GetMapping
	public List<RequestDto> getAllRequestsById(@RequestHeader(HEADER) Long userId) {
		log.info("getAllRequestsById, userId: {}", userId);
		return service.getAllRequestsById(userId).stream().map(v -> RequestMapper.toDto(v)).toList();
	}

	@GetMapping("/all")
	public List<RequestDto> getAllRequests(@RequestHeader(HEADER) Long userId,
			@RequestParam(name = "from", defaultValue = "0") Integer from,
			@RequestParam(name = "size", defaultValue = "50") Integer size) {
		log.info("getAllRequests: userId: {}, from: {}, size: {}", userId, from, size);
		return service.findAll(userId, from, size).stream().map(v -> RequestMapper.toDto(v)).toList();
	}

	@GetMapping("/{requestId}")
	public RequestDto getRequestById(@RequestHeader(HEADER) Long userId, @PathVariable("requestId") Long requestId) {
		log.info("getRequestById, userId: {}, requestId: {}", userId, requestId);
		List<Item> items = service.getRequestItems(requestId);
		RequestDto ans = RequestMapper.toDto(service.findById(userId, requestId), items);
		return ans;
	}
}
