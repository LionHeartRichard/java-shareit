package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class UserFullDto {
	Long id;
	String name;
	String email;
}
