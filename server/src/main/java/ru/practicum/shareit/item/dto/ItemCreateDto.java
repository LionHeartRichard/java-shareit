package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ItemCreateDto {
	String name;
	Boolean available;
	String description;
}
