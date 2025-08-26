package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ItemFullDto {
	Long id;
	String name;
	Boolean available;
	String description;
}
