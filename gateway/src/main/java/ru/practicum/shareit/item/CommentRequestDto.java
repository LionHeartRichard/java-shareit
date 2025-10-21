package ru.practicum.shareit.item;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CommentRequestDto {
	@NotBlank(message = "Comment is blank!!!")
	String text;
}
