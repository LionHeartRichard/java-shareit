package ru.practicum.shareit.item;

import java.util.List;

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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.common.dto.comment.CommentTextDto;
import ru.practicum.shareit.common.dto.comment.CommentDto;
import ru.practicum.shareit.common.dto.item.ItemDto;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.common.dto.item.ItemNewDto;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

	ItemService service;
	private static final String HEADER = "X-Sharer-User-Id";
	private static final String ITEM_ID = "/{itemId}";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemDto createItem(@RequestHeader(HEADER) final Long userId, @RequestBody ItemNewDto dto) {
		log.info("<--SERVER-->  CREATE Item, dto: {}", dto.toString());
		return service.createItem(userId, dto);
	}

	@PatchMapping(ITEM_ID)
	@ResponseStatus(HttpStatus.OK)
	public ItemDto updateItem(@RequestHeader(HEADER) final Long userId, @PathVariable final Long itemId,
			@RequestBody ItemDto dto) {
		log.info("<--SERVER-->  UPDATE Item, userId:{}, dto: {}", userId, dto.toString());
		return service.updateItem(userId, dto);
	}

	@GetMapping(ITEM_ID)
	@ResponseStatus(HttpStatus.OK)
	public ItemFullDto findItemById(@RequestHeader(HEADER) final Long userId, @PathVariable final Long itemId) {
		log.info("<--SERVER-->  FIND Item itemId: {}, userId: {}", itemId, userId);
		return service.findItemById(userId, itemId);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ItemDto> findItemsByOwner(@RequestHeader(HEADER) final Long userId) {
		log.info("<--SERVER-->  FIND ItemsByOwner, userId: {}", userId);
		return service.findItemsByOwner(userId);
	}

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public List<ItemDto> searchAvailableItemsByText(@RequestHeader(HEADER) final Long userId,
			@RequestParam final String text) {
		log.info("<--SERVER-->  SEARCH text: {}", text);
		return service.searchAvailableItemsByText(userId, text);
	}

	@PostMapping("/{itemId}/comment")
	public CommentDto addComment(@RequestHeader(HEADER) final Long userId, @PathVariable final Long itemId,
			@RequestBody final CommentTextDto dto) {
		log.info("<--SERVER-->  ADD Comment, userId: {}, itemId: {}, text: {};", userId, itemId, dto.getText());
		return service.addComment(userId, itemId, dto.getText());
	}

}
