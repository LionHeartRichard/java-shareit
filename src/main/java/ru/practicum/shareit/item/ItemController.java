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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

/**
 * TODO Sprint add-controllers.
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

	ItemService itemService;
	private static final String HEADER = "X-Sharer-User-Id";

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemFullDto createItem(@RequestHeader(HEADER) @Positive final Long userId,
			@RequestBody @Valid ItemCreateDto dto) {
		log.trace("createItem: ", dto.toString());
		Item ans = itemService.createItem(userId, ItemMapper.toModel(dto));
		log.trace(ans.toString());
		return ItemMapper.toDto(ans);
	}

	@PatchMapping("/{itemId}")
	@ResponseStatus(HttpStatus.OK)
	public ItemFullDto updateItem(@RequestHeader(HEADER) @Positive final Long userId,
			@PathVariable @Positive final Long itemId, @RequestBody @Valid ItemUpdateDto dto) {
		log.trace("updateItem: ", dto.toString());
		Item item = itemService.findItemById(itemId);
		log.trace(item.toString());
		Item ans = itemService.updateItem(userId, ItemMapper.toModel(item, dto));
		log.trace(ans.toString());
		return ItemMapper.toDto(ans);
	}

	@GetMapping("/{itemId}")
	@ResponseStatus(HttpStatus.OK)
	public ItemFullDto findItemById(@PathVariable @Positive final Long itemId) {
		log.trace("findItemById: itemId = ", itemId);
		Item item = itemService.findItemById(itemId);
		log.trace(item.toString());
		ItemFullDto ans = ItemMapper.toDto(item);
		log.trace(ans.toString());
		return ans;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ItemFullDto> findItemsByOwner(@RequestHeader(HEADER) @Positive final Long userId) {
		log.trace("findItemsByOwner: userId = ", userId);
		List<Item> items = itemService.findItemsByOwner(userId);
		log.trace("items: {}", items);
		return items.stream().map(v -> ItemMapper.toDto(v)).toList();
	}

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public List<ItemFullDto> searchAvailableItemsByName(@RequestParam @NotBlank final String text) {
		log.trace("searchAvailableItems: ", text);
		List<Item> items = itemService.searchAvailableItemsByName(text);
		log.trace("items: {}", items);
		return items.stream().map(v -> ItemMapper.toDto(v)).toList();
	}

}
