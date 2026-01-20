package ru.practicum.shareit.user;

import ru.practicum.shareit.common.dto.user.UserDto;
import ru.practicum.shareit.common.dto.user.UserFullDto;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.NotFoundException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {
	@Autowired
	private UserService userService;

	private static UserDto user;
	private static UserDto other;

	@BeforeAll
	static void setUp() {
		user = UserDto.builder().name("CreateName").email("email@mil.ru").build();
		other = UserDto.builder().name("CreateNameOther").email("email_other_@mil.ru").build();
	}

	@Test
	void createAndFindUserTest() {
		UserFullDto dto = userService.createUser(user);
		UserFullDto findDto = userService.findUserById(dto.getId());

		assertTrue(findDto.getId() != null);
		assertThat(dto.getId()).isEqualTo(findDto.getId());
		assertThat(user.getName()).isEqualTo(dto.getName()).isEqualTo(findDto.getName());
		assertThat(user.getEmail()).isEqualTo(dto.getEmail()).isEqualTo(findDto.getEmail());
	}

	@Test
	void updateUserTest() {
		UserFullDto dto = userService.createUser(user);
		UserDto upDto = UserDto.builder().name(other.getName()).email(other.getEmail()).build();
		UserFullDto ans = userService.updateUser(dto.getId(), upDto);

		assertThat(ans.getId()).isEqualTo(dto.getId());
		assertThat(ans.getName()).isEqualTo(upDto.getName());
		assertThat(ans.getEmail()).isEqualTo(upDto.getEmail());
	}

	@Test
	void updateUserNameIsNull() {
		UserFullDto dto = userService.createUser(user);
		UserDto upDto = UserDto.builder().email(other.getEmail()).build();
		UserFullDto ans = userService.updateUser(dto.getId(), upDto);

		assertThat(ans.getId()).isEqualTo(dto.getId());
		assertThat(ans.getName()).isEqualTo(dto.getName());
		assertThat(ans.getEmail()).isEqualTo(upDto.getEmail());
	}

	@Test
	void updateUserEmailIsNullTest() {
		UserFullDto dto = userService.createUser(user);
		UserDto upDto = UserDto.builder().name(other.getName()).build();
		UserFullDto ans = userService.updateUser(dto.getId(), upDto);

		assertThat(ans.getId()).isEqualTo(dto.getId());
		assertThat(ans.getName()).isEqualTo(upDto.getName());
		assertThat(ans.getEmail()).isEqualTo(dto.getEmail());
	}

	@Test
	void findAllUsersTest() {
		UserFullDto dto = userService.createUser(user);
		UserFullDto dtoOther = userService.createUser(other);
		List<UserFullDto> ans = userService.findAll().stream().toList();

		assertThat(ans.get(0).getId()).isEqualTo(dto.getId());
		assertThat(ans.get(0).getName()).isEqualTo(dto.getName());
		assertThat(ans.get(0).getEmail()).isEqualTo(dto.getEmail());

		assertThat(ans.get(1).getId()).isEqualTo(dtoOther.getId());
		assertThat(ans.get(1).getName()).isEqualTo(dtoOther.getName());
		assertThat(ans.get(1).getEmail()).isEqualTo(dtoOther.getEmail());
	}

	@Test
	void deleteUserTest() {
		UserFullDto dto = userService.createUser(user);
		userService.deleteUserById(dto.getId());

		assertThatThrownBy(() -> userService.findUserById(dto.getId())).isInstanceOf(NotFoundException.class);
	}

	@Test
	void throwExceptionWhenEmailIsDuplicateWhenCreateUserTest() {
		UserFullDto dto = userService.createUser(user);
		UserDto duplicate = UserDto.builder().name(other.getName()).email(dto.getEmail()).build();

		assertThatThrownBy(() -> userService.createUser(duplicate)).isInstanceOf(ConflictException.class);
	}

	@Test
	void throwExceptionWhenIdIsNullTest() {
		assertThatThrownBy(() -> userService.findUserById(null)).isInstanceOf(InvalidDataAccessApiUsageException.class);
	}

	@Test
	void throwExceptionWhenEmailIsDuplicateWhenUpdateUserTest() {

		userService.createUser(user);
		UserFullDto otherDto = userService.createUser(other);
		UserDto upUser = other.toBuilder().email(user.getEmail()).build();

		assertThatThrownBy(() -> userService.updateUser(otherDto.getId(), upUser))
				.isInstanceOf(ConflictException.class);
	}

}
