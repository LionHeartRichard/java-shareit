package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;

import ru.practicum.shareit.common.dto.item.ItemDto;

@Component
public class ItemMapper {

	public Item toModel(final ItemDto dto) {
		final Item ans = Item.builder().id(dto.getId()).name(dto.getName()).available(dto.getAvailable())
				.description(dto.getDescription()).user(null).build();
		return ans;
	}

	public Item toModel(final Item item, final ItemDto dto) {
		final String name = dto.hasName() ? dto.getName() : item.getName();
		final Boolean available = dto.hasAvailable() ? dto.getAvailable() : item.getAvailable();
		final String description = dto.hasDescription() ? dto.getDescription() : item.getDescription();
		final Item ans = item.toBuilder().name(name).available(available).description(description).build();
		return ans;
	}

	public ItemDto toDto(final Item item) {
		final ItemDto dto = ItemDto.builder().id(item.getId()).name(item.getName()).available(item.getAvailable())
				.description(item.getDescription()).build();
		return dto;
	}
}
