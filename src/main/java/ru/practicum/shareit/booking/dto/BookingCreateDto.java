package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BookingCreateDto {
	@NotNull
	@Positive
	Long itemId;
	@FutureOrPresent
	@NotNull
	LocalDateTime start;
	@Future
	@NotNull
	LocalDateTime end;
}
