package ru.practicum.shareit.common.dto.booking;

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
	@NotNull(message = "Item id is null!!!")
	@Positive(message = "Item id < 0 !!!")
	Long itemId;
	@FutureOrPresent(message = "Booking the STARTING time must be in the FUTURE or PRESENT!!!")
	@NotNull(message = "Booking start time not null!!!")
	LocalDateTime start;
	@Future(message = "Booking the ENDING time must be in the FUTURE!!!")
	@NotNull(message = "Booking end time not null!!!")
	LocalDateTime end;
}
