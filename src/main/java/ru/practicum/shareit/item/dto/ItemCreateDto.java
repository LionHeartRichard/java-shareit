package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ItemCreateDto {
	@NotBlank
	@Size(max = 50)
	String name;
	@NotNull
	Boolean available;
	@NotBlank
	@Size(max = 200)
	String description;
}
