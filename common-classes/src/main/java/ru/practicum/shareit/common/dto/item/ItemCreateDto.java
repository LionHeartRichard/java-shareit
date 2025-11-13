package ru.practicum.shareit.common.dto.item;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ItemCreateDto {
	String name;
	Boolean available;
	String description;
}
