package ru.practicum.shareit.common.dto.item;

import java.util.List;

import lombok.Builder;
import ru.practicum.shareit.common.dto.booking.BookingDto;
import ru.practicum.shareit.common.dto.comment.CommentDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
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
