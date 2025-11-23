package ru.practicum.shareit.request;

import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.shareit.common.dto.item.ItemDto;
import ru.practicum.shareit.common.dto.request.CreateRequestDto;
import ru.practicum.shareit.common.dto.request.RequestDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;

public class RequestMapper {

	private RequestMapper() {
	}

	public static Request toModel(CreateRequestDto dto) {
		return Request.builder().description(dto.getDescription()).created(LocalDateTime.now()).build();
	}

	public static RequestDto toDto(Request request) {
		return RequestDto.builder().id(request.getId()).description(request.getDescription())
				.requesterName(request.getRequester().getName()).created(request.getCreated()).build();
	}

	public static RequestDto toDto(Request request, List<Item> items) {
		List<ItemDto> dtoItems = items == null ? null : items.stream().map(v -> ItemMapper.toDto(v)).toList();
		return RequestDto.builder().id(request.getId()).description(request.getDescription())
				.requesterName(request.getRequester().getName()).created(request.getCreated()).items(dtoItems).build();
	}
}
