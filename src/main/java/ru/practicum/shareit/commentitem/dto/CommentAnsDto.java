package ru.practicum.shareit.commentitem.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CommentAnsDto {
	Long id;
	String text;
	String authorName;
	LocalDateTime created;
}
