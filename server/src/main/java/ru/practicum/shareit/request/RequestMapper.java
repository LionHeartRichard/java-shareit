package ru.practicum.shareit.request;

import java.time.LocalDateTime;
import ru.practicum.shareit.common.dto.request.RequestDto;
import ru.practicum.shareit.common.dto.request.RequestFullDto;

public class RequestMapper {

	private RequestMapper() {
	}

	public static Request toModel(RequestDto dto) {
		return Request.builder().description(dto.getDescription()).created(LocalDateTime.now()).build();
	}

	public static RequestFullDto toDto(Request request) {
		return RequestFullDto.builder().id(request.getId()).description(request.getDescription())
				.requesterName(request.getRequester().getName()).created(request.getCreated()).build();
	}
}
