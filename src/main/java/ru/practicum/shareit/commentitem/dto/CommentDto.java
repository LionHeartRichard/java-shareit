package ru.practicum.shareit.commentitem.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CommentDto {
	Long id;
	@NotBlank
	String text;
	String authorName;
	LocalDateTime created;
}
