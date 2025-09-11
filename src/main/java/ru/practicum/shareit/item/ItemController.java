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
import ru.practicum.shareit.commentitem.dto.CommentDto;
import ru.practicum.shareit.commentitem.dto.CommentAnsDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.User;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

	ItemService itemService;
	private static final String HEADER = "X-Sharer-User-Id";
	private static final String PATH_ITEM = "/{itemId}";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemDto createItem(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@RequestBody @Valid ItemCreateDto dto) {
		log.trace("createItem: ", dto.toString());
		Item ans = itemService.createItem(userId, ItemMapper.toModel(dto));
		log.trace(ans.toString());
		return ItemMapper.toDto(ans);
	}

	@PatchMapping(PATH_ITEM)
	@ResponseStatus(HttpStatus.OK)
	public ItemDto updateItem(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @Positive final Long itemId, @RequestBody @Valid ItemUpdateDto dto) {
		log.trace("updateItem: ", dto.toString());
		Item item = itemService.findItemById(itemId);
		log.trace(item.toString());
		Item ans = itemService.updateItem(userId, ItemMapper.toModel(item, dto));
		log.trace(ans.toString());
		return ItemMapper.toDto(ans);
	}

	@GetMapping(PATH_ITEM)
	@ResponseStatus(HttpStatus.OK)
	public ItemFullDto findItemById(@PathVariable @NotNull @Positive final Long itemId) {
		log.trace("findItemById: itemId = ", itemId);
		Item item = itemService.findItemById(itemId);
		log.trace(item.toString());
		ItemDto ans = ItemMapper.toDto(item);
		log.trace(ans.toString());
		Booking[] bookings = itemService.findLastBooking(item);
		List<CommentItem> comments = itemService.findCommentsByItem(item);
		return CommentItemMapper.toFullDto(item, comments, bookings);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ItemDto> findItemsByOwner(@RequestHeader(HEADER) @NotNull @Positive final Long userId) {
		log.trace("findItemsByOwner: userId = ", userId);
		List<Item> items = itemService.findItemsByOwner(userId);
		log.trace("items: {}", items);
		return items.stream().map(v -> ItemMapper.toDto(v)).toList();
	}

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public List<ItemDto> searchAvailableItemsByText(@RequestParam final String text) {
		log.trace("searchAvailableItems: ", text);
		List<Item> items = itemService.searchAvailableItemsByText(text);
		log.trace("items: {}", items);
		return items.stream().map(v -> ItemMapper.toDto(v)).toList();
	}

	@PostMapping("/{itemId}/comment")
	public CommentAnsDto addComment(@RequestHeader(HEADER) @NotNull @Positive final Long userId,
			@PathVariable @NotNull @Positive final Long itemId, @RequestBody @Valid CommentDto dto) {
		log.trace("addComment: userId = {}, itemId = {}", userId, itemId, dto.toString());
		User user = itemService.findUserById(userId);
		log.trace(user.toString());
		Item item = itemService.findItemById(itemId);
		log.trace(item.toString());
		CommentItem comment = itemService.addComment(CommentItemMapper.toModel(user, item, dto.getText()));
		return CommentItemMapper.toCommentUserDto(comment);
	}

}
