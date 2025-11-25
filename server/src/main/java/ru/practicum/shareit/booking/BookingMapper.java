package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.practicum.shareit.UtilMapper;
import ru.practicum.shareit.common.BookingStatus;
import ru.practicum.shareit.common.dto.booking.BookingDto;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

@Component
public class BookingMapper {

	@Autowired
	private UserMapper mapper;

	public Booking toModel(final User user, final Item item, final BookingDto dto) {
		if (dto != null) {
			final Booking ans = Booking.builder().id(null).item(item).user(user)
					.start(UtilMapper.toLong(dto.getStart())).end(UtilMapper.toLong(dto.getEnd()))
					.status(BookingStatus.WAITING).build();
			return ans;
		}
		return null;
	}

	public BookingDto toDto(final Booking booking) {
		if (booking != null) {
			final BookingDto ans = BookingDto.builder().id(booking.getId()).item(ItemMapper.toDto(booking.getItem()))
					.booker(mapper.toDto(booking.getUser())).start(UtilMapper.toLocalDateTime(booking.getStart()))
					.end(UtilMapper.toLocalDateTime(booking.getEnd())).status(BookingStatus.WAITING).build();
			return ans;
		}
		return null;
	}

	public BookingDto toDtoSaveStatus(final Booking booking) {
		if (booking != null) {
			final BookingDto ans = BookingDto.builder().id(booking.getId()).item(ItemMapper.toDto(booking.getItem()))
					.booker(mapper.toDto(booking.getUser())).start(UtilMapper.toLocalDateTime(booking.getStart()))
					.end(UtilMapper.toLocalDateTime(booking.getEnd())).status(booking.getStatus()).build();
			return ans;
		}
		return null;
	}
}
