package ru.practicum.shareit.commentitem;

import java.util.List;

import ru.practicum.shareit.UtilMapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.commentitem.dto.CommentAnsDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.user.User;

public class CommentItemMapper {
	private CommentItemMapper() {
	}

	public static CommentItem toModel(final User user, final Long itemId, final String text) {
		CommentItem ans = CommentItem.builder().id(null).text(text).itemId(itemId).userId(user.getId())
				.userName(user.getName()).created(UtilMapper.getCurrentTime()).build();
		return ans;
	}

	public static ItemFullDto toFullDto(Item item, List<CommentItem> comments, Booking[] bookings) {
		List<CommentAnsDto> commentsUsers = comments.stream().map(v -> toCommentUserDto(v)).toList();
		ItemFullDto ans = ItemFullDto.builder().id(item.getId()).name(item.getName()).available(item.getAvailable())
				.description(item.getDescription()).userId(item.getUserId())
				.lastBooking(BookingMapper.toDtoSaveStatus(bookings[0]))
				.nextBooking(BookingMapper.toDtoSaveStatus(bookings[1])).comments(commentsUsers).build();
		return ans;
	}

	public static CommentAnsDto toCommentUserDto(CommentItem comment) {
		CommentAnsDto ans = CommentAnsDto.builder().id(comment.getId()).authorName(comment.getUserName())
				.text(comment.getText()).created(UtilMapper.toLocalDateTime(comment.getCreated())).build();
		return ans;
	}

}
