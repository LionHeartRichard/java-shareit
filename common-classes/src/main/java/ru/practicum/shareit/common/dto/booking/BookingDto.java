package ru.practicum.shareit.common.dto.booking;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.common.BookingStatus;
import ru.practicum.shareit.common.dto.item.ItemDto;
import ru.practicum.shareit.common.dto.user.UserDto;

@Value
@Builder(toBuilder = true)
public class BookingDto {
	Long id;
	ItemDto item;
	UserDto booker;
	LocalDateTime start;
	LocalDateTime end;
	BookingStatus status;
}
