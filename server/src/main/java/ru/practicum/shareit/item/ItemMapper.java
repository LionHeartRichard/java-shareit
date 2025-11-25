package ru.practicum.shareit.item;

import java.util.List;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.common.dto.booking.BookingFullDto;
import ru.practicum.shareit.common.dto.comment.CommentDto;
import ru.practicum.shareit.common.dto.item.ItemDto;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.common.dto.item.ItemNewDto;

public class ItemMapper {

	private ItemMapper() {
	}

	public static Item toModel(final ItemDto dto) {
		final Item ans = Item.builder().id(dto.getId()).name(dto.getName()).available(dto.getAvailable())
				.description(dto.getDescription()).owner(null).build();
		return ans;
	}

	public static Item toModel(final ItemNewDto dto) {
		final Item ans = Item.builder().id(null).name(dto.getName()).available(dto.getAvailable())
				.description(dto.getDescription()).owner(null).build();
		return ans;
	}

	public static Item toModel(final Item item, final ItemDto dto) {
		final String name = dto.hasName() ? dto.getName() : item.getName();
		final Boolean available = dto.hasAvailable() ? dto.getAvailable() : item.getAvailable();
		final String description = dto.hasDescription() ? dto.getDescription() : item.getDescription();
		final Item ans = item.toBuilder().name(name).available(available).description(description).build();
		return ans;
	}

	public static ItemDto toDto(final Item item) {
		final ItemDto dto = ItemDto.builder().id(item.getId()).name(item.getName()).available(item.getAvailable())
				.description(item.getDescription()).build();
		return dto;
	}

	public static ItemFullDto toDto(Item item, List<CommentDto> comments, Booking[] bookings) {
		BookingFullDto lastBooking = BookingMapper.toDto(bookings[0]);
		BookingFullDto nextBooking = BookingMapper.toDto(bookings[1]);
		final Long requestId = item.getRequest() == null ? null : item.getRequest().getId();
		ItemFullDto ans = ItemFullDto.builder().id(item.getId()).name(item.getName()).available(item.getAvailable())
				.description(item.getDescription()).comments(comments).lastBooking(lastBooking).nextBooking(nextBooking)
				.requestId(requestId).build();
		return ans;
	}
}
