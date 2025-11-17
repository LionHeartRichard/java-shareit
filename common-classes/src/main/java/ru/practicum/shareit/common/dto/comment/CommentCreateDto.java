package ru.practicum.shareit.common.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CommentCreateDto {
	@NotBlank(message = "Comment is blank!!!")
	String text;
}
