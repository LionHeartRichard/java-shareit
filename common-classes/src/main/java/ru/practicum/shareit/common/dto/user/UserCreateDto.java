package ru.practicum.shareit.common.dto.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class UserCreateDto {
	String name;
	String email;
}
