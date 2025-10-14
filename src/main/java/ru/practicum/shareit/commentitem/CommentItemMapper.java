package ru.practicum.shareit.commentitem;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.UtilMapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.commentitem.dto.CommentDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.user.User;

@Slf4j
public class CommentItemMapper {

	private CommentItemMapper() {
	}

	public static CommentItem toModel(final User user, final Item item, final String text) {
		final CommentItem ans = CommentItem.builder().id(null).text(text).item(item).user(user)
				.created(UtilMapper.getCurrentTime()).build();
		return ans;

	}

	public static ItemFullDto toFullDto(final Item item, final List<CommentItem> comments, final Booking[] bookings) {
		final List<CommentDto> commentsUsers = comments.stream().map(v -> toDto(v)).toList();

		commentsUsers.forEach(v -> log.error("   ----   " + v));

		final ItemFullDto ans = ItemFullDto.builder().id(item.getId()).name(item.getName())
				.available(item.getAvailable()).description(item.getDescription()).userId(item.getUser().getId())
				.lastBooking(BookingMapper.toDtoSaveStatus(bookings[0]))
				.nextBooking(BookingMapper.toDtoSaveStatus(bookings[1])).comments(commentsUsers).build();
		return ans;
	}

	public static CommentDto toDto(final CommentItem comment) {
		final CommentDto ans = CommentDto.builder().id(comment.getId()).authorName(comment.getUser().getName())
				.text(comment.getText()).created(UtilMapper.toLocalDateTime(comment.getCreated())).build();
		return ans;
	}

}
