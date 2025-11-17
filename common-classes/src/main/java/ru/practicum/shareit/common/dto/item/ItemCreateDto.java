package ru.practicum.shareit.common.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ItemCreateDto {
	@NotBlank(message = "Name item is blank!!!")
	@Size(max = 50, message = "The name for item cannot be longer than 50 characters!!!")
	String name;
	@NotNull(message = "Available is blank!!!")
	Boolean available;
	@NotBlank(message = "Description is blank!!!")
	@Size(max = 200, message = "The description for item cannot be longer than 200 characters!!!")
	String description;
}
