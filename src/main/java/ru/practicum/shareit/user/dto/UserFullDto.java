package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class UserFullDto {
	Long id;
	String name;
	String email;
}
