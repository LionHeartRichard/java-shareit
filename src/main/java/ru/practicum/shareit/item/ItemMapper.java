package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

public class ItemMapper {

	private ItemMapper() {
	}

	public static Item toModel(ItemCreateDto dto) {
		Item item = Item.builder()
				.id(null)
				.name(dto.getName())
				.available(dto.getAvailable())
				.description(dto.getDescription())
				.userId(null)
				.build();
		return item;
	}
	
	public static Item toModel(Item item, ItemUpdateDto dto) {
		final String name = dto.hasName() ? dto.getName() : item.getName();
		final Boolean available = dto.hasAvailable() ? dto.getAvailable() : item.getAvailable();
		final String description = dto.hasDescription() ? dto.getDescription() : item.getDescription();
		Item ans = item.toBuilder()
				.name(name)
				.available(available)
				.description(description)
				.build();
		return ans;
	}
	
	public static ItemFullDto toDto(Item item) {
		ItemFullDto dto = ItemFullDto.builder()
				.id(item.getId())
				.name(item.getName())
				.available(item.getAvailable())
				.description(item.getDescription())
				.build();
		return dto;
	}

}
