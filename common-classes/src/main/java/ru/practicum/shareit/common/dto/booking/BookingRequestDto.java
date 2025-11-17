package ru.practicum.shareit.common.dto.booking;

import lombok.Value;
import lombok.Builder;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

@Value
@Builder(toBuilder = true)
public class BookingRequestDto {
	Long itemId; // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	@FutureOrPresent
	LocalDateTime start;
	@Future
	LocalDateTime end;
}
