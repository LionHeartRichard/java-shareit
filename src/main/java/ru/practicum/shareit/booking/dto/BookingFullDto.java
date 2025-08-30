package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserFullDto;

@Value
@Builder(toBuilder = true)
public class BookingFullDto {
	Long id;
	ItemDto item;
	UserFullDto booker;
	LocalDateTime start;
	LocalDateTime end;
	BookingStatus status;
}
