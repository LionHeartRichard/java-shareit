package ru.practicum.shareit.common.dto.comment;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CommentDto {
	Long id;
	String text;
	String authorName;
	LocalDateTime created;
}
