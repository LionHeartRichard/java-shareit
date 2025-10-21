package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CommentCreateDto {
	String text;
}
