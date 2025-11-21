package ru.practicum.shareit.booking;

import ru.practicum.shareit.UtilMapper;
import ru.practicum.shareit.common.BookingStatus;
import ru.practicum.shareit.common.dto.booking.BookingDto;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

public class BookingMapper {
	private BookingMapper() {
	}

	public static Booking toModel(final User user, final Item item, final BookingDto dto) {
		if (dto != null) {
			final Booking ans = Booking.builder().id(null).item(item).user(user)
					.start(UtilMapper.toLong(dto.getStart())).end(UtilMapper.toLong(dto.getEnd()))
					.status(BookingStatus.WAITING).build();
			return ans;
		}
		return null;
	}

	public static BookingDto toDto(final Booking booking) {
		if (booking != null) {
			final BookingDto ans = BookingDto.builder().id(booking.getId()).item(ItemMapper.toDto(booking.getItem()))
					.booker(UserMapper.toDto(booking.getUser())).start(UtilMapper.toLocalDateTime(booking.getStart()))
					.end(UtilMapper.toLocalDateTime(booking.getEnd())).status(BookingStatus.WAITING).build();
			return ans;
		}
		return null;
	}

	public static BookingDto toDtoSaveStatus(final Booking booking) {
		if (booking != null) {
			final BookingDto ans = BookingDto.builder().id(booking.getId()).item(ItemMapper.toDto(booking.getItem()))
					.booker(UserMapper.toDto(booking.getUser())).start(UtilMapper.toLocalDateTime(booking.getStart()))
					.end(UtilMapper.toLocalDateTime(booking.getEnd())).status(booking.getStatus()).build();
			return ans;
		}
		return null;
	}
}
