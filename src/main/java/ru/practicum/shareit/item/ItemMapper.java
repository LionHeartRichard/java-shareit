package ru.practicum.shareit.item;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import jakarta.validation.Valid;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.commentitem.CommentItem;
import ru.practicum.shareit.commentitem.dto.CommentAnsDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserFullDto;

@Mapper
public interface ItemMapper {

	Item toEntity(ItemCreateDto dto);

	ItemDto toDto(Item item);

	Item toEntity(Item item, @MappingTarget ItemUpdateDto dto);

	@Mapping(target = "owner", source = "item.owner", qualifiedByName = "mapOwner")
	@Mapping(target = "comments", source = "comments")
	@Mapping(target = "bookings", source = "bookings")
	ItemFullDto toFullDto(Item item, List<CommentItem> comments, Booking[] bookings);

	@Named("mapOwner")
	default UserFullDto mapOwner(User owner) {
		return UserFullDto.builder().id(owner.getId()).name(owner.getName()).email(owner.getEmail()).build();
	}

	@Mapping(target = "author", source = "author", qualifiedByName = "mapOwner")
	CommentAnsDto toCommentDto(CommentItem commentItem);

	@Mapping(target = "booker", source = "booker", qualifiedByName = "mapOwner")
	BookingFullDto toBookingDto(Booking booking);

	Booking toEntity(User user, Item item, BookingCreateDto dto);

}
