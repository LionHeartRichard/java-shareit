package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.CommentItemDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

public class ItemMapper {

	private ItemMapper() {
	}

	public static Item toModel(ItemCreateDto dto) {
		Item ans = Item.builder().id(null).name(dto.getName()).available(dto.getAvailable())
				.description(dto.getDescription()).userId(null).build();
		return ans;
	}

	public static Item toModel(Item item, ItemUpdateDto dto) {
		final String name = dto.hasName() ? dto.getName() : item.getName();
		final Boolean available = dto.hasAvailable() ? dto.getAvailable() : item.getAvailable();
		final String description = dto.hasDescription() ? dto.getDescription() : item.getDescription();
		Item ans = item.toBuilder().name(name).available(available).description(description).build();
		return ans;
	}

	public static ItemDto toDto(Item item) {
		ItemDto dto = ItemDto.builder().id(item.getId()).name(item.getName()).available(item.getAvailable())
				.description(item.getDescription()).build();
		return dto;
	}

	public static ItemFullDto toFullDto(Item ans) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Item toModel(CommentItemDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

}
