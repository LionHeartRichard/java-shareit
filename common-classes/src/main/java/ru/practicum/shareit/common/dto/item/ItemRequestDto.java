package ru.practicum.shareit.common.dto.item;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ItemRequestDto {
	Long itemId;
	@Size(max = 50, message = "The name for item cannot be longer than 50 characters!!!")
	String name;
	Boolean available;
	@Size(max = 200, message = "The description for item cannot be longer than 200 characters!!!")
	String description;
}
