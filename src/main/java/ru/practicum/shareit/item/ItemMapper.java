package ru.practicum.shareit.item;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.commentitem.CommentItem;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Mapper
public interface ItemMapper {

	Item toEntity(ItemCreateDto dto);

	ItemDto toDto(Item item);

	Item toEntity(Item item, @MappingTarget ItemUpdateDto dto);

	ItemFullDto toFullDto(Item item, List<CommentItem> comments, Booking[] bookings);

}
