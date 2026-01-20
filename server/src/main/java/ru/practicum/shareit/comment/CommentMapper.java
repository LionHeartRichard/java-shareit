package ru.practicum.shareit.comment;

import java.util.List;
import ru.practicum.shareit.UtilMapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.common.dto.comment.CommentDto;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

public class CommentMapper {

	private CommentMapper() {
	}

	public static Comment toModel(final User user, final Item item, final String text) {
		final Comment ans = Comment.builder().id(null).text(text).item(item).user(user)
				.created(UtilMapper.getCurrentTime()).build();
		return ans;

	}

	public static ItemFullDto toFullDto(final Item item, final List<Comment> comments, final Booking[] bookings) {
		final List<CommentDto> commentsUsers = comments.stream().map(v -> toDto(v)).toList();

		final ItemFullDto ans = ItemFullDto.builder().id(item.getId()).name(item.getName())
				.available(item.getAvailable()).description(item.getDescription()).userId(item.getOwner().getId())
				.lastBooking(BookingMapper.toDtoSaveStatus(bookings[0]))
				.nextBooking(BookingMapper.toDtoSaveStatus(bookings[1])).comments(commentsUsers).build();
		return ans;
	}

	public static CommentDto toDto(final Comment comment) {
		final CommentDto ans = CommentDto.builder().id(comment.getId()).authorName(comment.getUser().getName())
				.text(comment.getText()).created(UtilMapper.toLocalDateTime(comment.getCreated())).build();
		return ans;
	}

}