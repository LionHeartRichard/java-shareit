package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.dto.user.UserDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserControllerTest {

	@Autowired
	ObjectMapper objMapper;

	@MockBean
	UserService userService;

	@MockBean
	UserMapper userMapper;

	@Autowired
	MockMvc mvc;

	final User createUser = new User(null, "name", "email@mail.ru");
	final User updateUser = new User(1L, "upName", "up_email@mail.ru");

	final User expectedCreateUser = new User(1L, "name", "email@mail.ru");
	final User expectedUpdateUser = new User(1L, "upName", "up_email@mail.ru");

	final UserDto dto = new UserDto(1L, "upName", "up_email@mail.ru");

	@Test
	void createUserTest() throws Exception {
		when(userService.createUser(createUser)).thenReturn(expectedCreateUser);

		mvc.perform(post("/users").content(objMapper.writeValueAsString(createUser))
				.characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(expectedCreateUser.getId()))
				.andExpect(jsonPath("$.name").value(expectedCreateUser.getName()))
				.andExpect(jsonPath("$.email").value(expectedCreateUser.getEmail()));
	}

	@Test
	void updateUserTest() throws Exception {
		when(userService.updateUser(updateUser)).thenReturn(expectedUpdateUser);
		when(userService.findUserById(1L)).thenReturn(createUser);
		when(userMapper.toModel(dto, createUser)).thenReturn(expectedUpdateUser);

		mvc.perform(patch("/users/1").content(objMapper.writeValueAsString(updateUser))
				.characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(expectedUpdateUser.getId()))
				.andExpect(jsonPath("$.name").value(expectedUpdateUser.getName()))
				.andExpect(jsonPath("$.email").value(expectedUpdateUser.getEmail()));
	}

	@Test
	void findUserByIdTest() throws Exception {
		when(userService.findUserById(1L)).thenReturn(expectedCreateUser);

		mvc.perform(get("/users/1").characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(expectedCreateUser.getId()))
				.andExpect(jsonPath("$.name").value(expectedCreateUser.getName()))
				.andExpect(jsonPath("$.email").value(expectedCreateUser.getEmail()));
	}

	@Test
	void deleteUserByIdTest() throws Exception {
		doNothing().when(userService).deleteUserById(1L);
		mvc.perform(delete("/users/1")).andExpect(status().isOk());
		verify(userService, times(1)).deleteUserById(1L);
	}

	@Test
	void getAllUsersTest() throws Exception {
		when(userService.findAll()).thenReturn(List.of(expectedCreateUser));

		mvc.perform(get("/users").characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id").value(expectedCreateUser.getId()))
				.andExpect(jsonPath("$[0].name").value(expectedCreateUser.getName()))
				.andExpect(jsonPath("$[0].email").value(expectedCreateUser.getEmail()));
	}
}
