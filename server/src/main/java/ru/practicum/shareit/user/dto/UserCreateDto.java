package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class UserCreateDto {
	String name;
	String email;
}
