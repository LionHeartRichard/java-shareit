package ru.practicum.shareit.common.dto.item;

import lombok.Builder;
import lombok.Data;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
	Long id;
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
