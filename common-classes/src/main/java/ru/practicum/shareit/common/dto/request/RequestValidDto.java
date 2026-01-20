package ru.practicum.shareit.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestValidDto {
	@NotBlank(message = "Description is blank!!!")
	@Size(max = 1000, message = "The description for item cannot be longer than 1000 characters!!!")
	String description;
}
