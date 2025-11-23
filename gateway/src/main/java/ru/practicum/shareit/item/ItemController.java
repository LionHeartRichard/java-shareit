package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.common.dto.comment.ValidCommentDto;
import ru.practicum.shareit.common.dto.item.ItemCreateDto;
import ru.practicum.shareit.common.dto.item.ItemUpdateDto;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

	ItemClient client;
	private static final String HEADER = "X-Sharer-User-Id";
	private static final String PATH = "/{itemId}";
	private static final String SEARCH = "/search";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> createItem(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@RequestBody @Valid ItemCreateDto dto) {
		log.info("<--GATEWAY-->  Create Item: {}", dto.toString());
		return client.createItem(userId, dto);
	}

	@PatchMapping(PATH)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> updateItem(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @Positive final Long itemId, @RequestBody @Valid ItemUpdateDto dto) {
		log.info("<--GATEWAY-->  Update Item, userId:{}, dto: {}", userId, dto.toString());
		return client.updateItem(itemId, userId, dto);
	}

	@GetMapping(PATH)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> findItemById(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @NotNull @Positive final Long itemId) {
		log.info("<--GATEWAY--> method findItemById: userId: " + userId);
		log.info("<--GATEWAY-->  Find item by ID: itemId: {}, userId: {}", itemId, userId);
		return client.findById(itemId, userId);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> findItemsByOwner(@RequestHeader(HEADER) @NotNull @Positive final Long userId) {
		log.info("<--GATEWAY-->  Find Items By Owner: userId: {}", userId);
		return client.findByOwner(userId);
	}

	@GetMapping(SEARCH)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> searchAvailableItemsByText(
			@RequestHeader(HEADER) @NotNull @Positive final Long userId, @RequestParam final String text) {
		log.info("<--GATEWAY-->  Search by TEXT: text: {}", text);
		return client.searchByText(SEARCH, userId, text);
	}

	@PostMapping("/{itemId}/comment")
	public ResponseEntity<Object> addComment(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @NotNull @Positive final Long itemId, @RequestBody @Valid final ValidCommentDto dto) {
		log.info("<--GATEWAY-->  Add Comment: text: {}, userId: {}, itemId: {};", dto.getText(), userId, itemId);
		return client.addComment(itemId, userId, dto);
	}

}
