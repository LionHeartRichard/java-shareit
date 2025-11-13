package ru.practicum.shareit.common.dto.comment;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CommentCreateDto {
	String text;
}
