package ru.practicum.shareit.common.dto.item;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemUpdateDto {
	@Size(max = 50, message = "The name for item cannot be longer than 50 characters!!!")
	String name;
	Boolean available;
	@Size(max = 200, message = "The description for item cannot be longer than 200 characters!!!")
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
