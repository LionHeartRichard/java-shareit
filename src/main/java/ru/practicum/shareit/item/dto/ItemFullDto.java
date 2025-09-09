package ru.practicum.shareit.item.dto;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.commentitem.dto.CommentAnsDto;

@Value
@Builder(toBuilder = true)
public class ItemFullDto {
	Long id;
	String name;
	Boolean available;
	String description;
	Long userId;

	List<CommentAnsDto> comments;

	BookingFullDto lastBooking;
	BookingFullDto nextBooking;
}
