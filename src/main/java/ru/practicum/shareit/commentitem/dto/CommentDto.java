package ru.practicum.shareit.commentitem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class CommentDto {
	Long id;
	@NotBlank
	String text;
	String authorName;
	LocalDateTime created;
}
