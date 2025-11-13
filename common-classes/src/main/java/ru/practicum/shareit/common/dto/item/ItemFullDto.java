package ru.practicum.shareit.common.dto.item;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;

@Value
@Builder(toBuilder = true)
public class ItemFullDto {
	Long id;
	String name;
	Boolean available;
	String description;
	Long userId;

	List<CommentDto> comments;

	BookingDto lastBooking;
	BookingDto nextBooking;
}
