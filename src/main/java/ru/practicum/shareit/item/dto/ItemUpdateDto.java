package ru.practicum.shareit.item.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

@Value
@Builder(toBuilder = true)
public class ItemUpdateDto {
	@Size(max = 50)
	String name;
	Boolean available;
	@Size(max = 200)
	String description;

	public boolean hasName() {
		return !(name == null || name.isBlank());
	}

	public boolean hasAvailable() {
		return available != null;
	}

	public boolean hasDescription() {
		return !(description == null || description.isBlank());
	}

}
