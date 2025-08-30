package ru.practicum.shareit.booking;

import ru.practicum.shareit.UtilMapper;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {
	private BookingMapper() {
	}

	public static Booking toModel(User user, Item item, BookingCreateDto dto) {
		Booking ans = Booking.builder().id(null).item(item).user(user).start(UtilMapper.toLong(dto.getStart()))
				.end(UtilMapper.toLong(dto.getEnd())).status(BookingStatus.WAITING).build();
		return ans;
	}

	public static BookingFullDto toDto(Booking booking) {
		BookingFullDto ans = BookingFullDto.builder().id(booking.getId()).item(ItemMapper.toDto(booking.getItem()))
				.booker(UserMapper.toDto(booking.getUser())).start(UtilMapper.toLocalDateTime(booking.getStart()))
				.end(UtilMapper.toLocalDateTime(booking.getEnd())).status(BookingStatus.WAITING).build();
		return ans;
	}

	public static BookingFullDto toDtoNoChangeStatus(Booking booking) {
		BookingFullDto ans = BookingFullDto.builder().id(booking.getId()).item(ItemMapper.toDto(booking.getItem()))
				.booker(UserMapper.toDto(booking.getUser())).start(UtilMapper.toLocalDateTime(booking.getStart()))
				.end(UtilMapper.toLocalDateTime(booking.getEnd())).status(booking.getStatus()).build();
		return ans;
	}
}
