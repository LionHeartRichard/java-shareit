package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BookingCreateDto {
	Long itemId;
	LocalDateTime start;
	LocalDateTime end;
}
