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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.commentitem.CommentItem;
import ru.practicum.shareit.commentitem.CommentItemMapper;
import ru.practicum.shareit.commentitem.dto.CommentCreateDto;
import ru.practicum.shareit.commentitem.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

	ItemService service;
	private static final String HEADER = "X-Sharer-User-Id";
	private static final String PATH_ITEM = "/{itemId}";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemDto createItem(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@RequestBody @Valid ItemCreateDto dto) {
		log.trace("createItem: {}", dto.toString());
		final Item ans = service.createItem(userId, ItemMapper.toModel(dto));
		log.trace("ans item in DB: {}", ans.toString());
		return ItemMapper.toDto(ans);
	}

	@PatchMapping(PATH_ITEM)
	@ResponseStatus(HttpStatus.OK)
	public ItemDto updateItem(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @Positive final Long itemId, @RequestBody @Valid ItemUpdateDto dto) {
		log.trace("updateItem, userId:{}, ItemUpdateDto: {}", userId, dto.toString());
		final Item item = service.findItemById(itemId);
		log.trace("old item in DB: {}", item.toString());
		final Item ans = service.updateItem(userId, ItemMapper.toModel(item, dto));
		log.trace("ans item update: {}", ans.toString());
		return ItemMapper.toDto(ans);
	}

	@GetMapping(PATH_ITEM)
	@ResponseStatus(HttpStatus.OK)
	public ItemFullDto findItemById(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @NotNull @Positive final Long itemId) {
		log.trace("findItemById itemId: {}, userId: {}", itemId, userId);
		final Item item = service.findItemById(itemId);
		log.trace("find item in DB: {}", item.toString());
		final Booking[] bookings = service.findLastBooking(itemId, userId);
		log.trace("bookings: [0]: {}, [1]: {}", bookings[0], bookings[1]);
		final List<CommentItem> comments = service.findCommentsByItemId(itemId);
		return CommentItemMapper.toFullDto(item, comments, bookings);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ItemDto> findItemsByOwner(@RequestHeader(HEADER) @NotNull @Positive final Long userId) {
		log.trace("findItemsByOwner: userId = {}", userId);
		final List<Item> items = service.findItemsByOwner(userId);
		log.trace("items: {}", items);
		return items.stream().map(v -> ItemMapper.toDto(v)).toList();
	}

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public List<ItemDto> searchAvailableItemsByText(@RequestParam final String text) {
		log.trace("searchAvailableItems: {}", text);
		final List<Item> items = service.searchAvailableItemsByText(text);
		log.trace("items: {}", items);
		return items.stream().map(v -> ItemMapper.toDto(v)).toList();
	}

	@PostMapping("/{itemId}/comment")
	public CommentDto addComment(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @NotNull @Positive final Long itemId, @RequestBody @Valid final CommentCreateDto dto) {
		log.trace("addComment: userId: {}, itemId: {};", userId, itemId);
		log.trace("CommentDTO: {}", dto.toString());
		final CommentItem comment = service.addComment(userId, itemId, dto.getText());
		log.trace("comment: {}", comment.toString());
		return CommentItemMapper.toDto(comment);
	}

}
