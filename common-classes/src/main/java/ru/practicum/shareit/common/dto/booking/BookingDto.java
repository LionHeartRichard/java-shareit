package ru.practicum.shareit.common.dto.booking;

import java.time.LocalDateTime;

import ru.practicum.shareit.common.BookingStatus;
import ru.practicum.shareit.common.dto.item.ItemDto;
import ru.practicum.shareit.common.dto.user.UserFullDto;

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
public class BookingDto {
	Long id;
	ItemDto item;
	UserFullDto booker;
	LocalDateTime start;
	LocalDateTime end;
	BookingStatus status;
}
