package ru.practicum.shareit.common.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
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
public class RequestFullDto {
	Long id;
	String description;
	String requesterName;
	LocalDateTime created;
	List<ItemFullDto> items;
}
