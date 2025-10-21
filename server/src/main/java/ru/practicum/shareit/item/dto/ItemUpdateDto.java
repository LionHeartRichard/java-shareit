package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ItemUpdateDto {

	String name;
	Boolean available;
	String description;

	public boolean hasName() {
		return !(name == null || name.isBlank());
	}

	public boolean hasAvailable() {
		return available != null;
	}

	public boolean hasDescription() {
		return !(description == null || description.isBlank());
	}
}
