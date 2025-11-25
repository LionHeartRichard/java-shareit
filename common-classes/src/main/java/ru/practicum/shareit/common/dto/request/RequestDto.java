package ru.practicum.shareit.common.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.shareit.common.dto.item.ItemDto;
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
public class RequestDto {
	Long id;
	String description;
	String requesterName;
	LocalDateTime created;
	List<ItemDto> items;
}
