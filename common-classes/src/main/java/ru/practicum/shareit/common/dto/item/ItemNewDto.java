package ru.practicum.shareit.common.dto.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemNewDto {
	String name;
	String description;
	Boolean available;
	Long requestId;

	public boolean hasRequestId() {
		return requestId != null;
	}
}
