package ru.practicum.shareit.common.dto.item;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.common.dto.booking.BookingDto;
import ru.practicum.shareit.common.dto.comment.CommentDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
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

	Long requestId;
}
