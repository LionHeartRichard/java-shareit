package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemUpdateDto {
	@Size(max = 50)
	String name;
	Boolean available;
	@Size(max = 200)
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
