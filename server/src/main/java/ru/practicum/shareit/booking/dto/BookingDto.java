package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

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
