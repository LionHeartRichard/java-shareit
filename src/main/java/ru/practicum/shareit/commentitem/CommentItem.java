package ru.practicum.shareit.commentitem;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CommentItem {
	Long id;
	String text;
	Long itemId;
	Long userId;
	String userName;
	Long created;
}
