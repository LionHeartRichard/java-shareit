package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserFullDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Mapper
public interface UserMapper {

	UserFullDto toDto(User user);

	User toEntity(UserCreateDto dto);

	User toEntity(UserUpdateDto userUpdateDto, @MappingTarget User user);
}
