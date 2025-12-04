package ru.practicum.shareit.user;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.dto.user.UserDto;
import ru.practicum.shareit.common.dto.user.UserFullDto;

@WebMvcTest(controllers = UserController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserControllerTest {

	@Autowired
	ObjectMapper mapper;

	@MockBean
	UserService userService;

	@Autowired
	MockMvc mvc;

	final UserDto dtoCreate = new UserDto("nameCreate", "create@mail.ru");
	final UserDto dtoUpdate = new UserDto("nameUp", "up@mail.ru");

	final UserFullDto ansCreate = new UserFullDto(1L, "nameCreate", "create@mail.ru");
	final UserFullDto ansUpdate = new UserFullDto(1L, "nameUp", "up@mail.ru");

	@Test
	void createUser() throws Exception {
		when(userService.createUser(dtoCreate)).thenReturn(ansCreate);

		mvc.perform(
				post("/users").content(mapper.writeValueAsString(dtoCreate)).characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(ansCreate.getId()))
				.andExpect(jsonPath("$.name").value(ansCreate.getName()))
				.andExpect(jsonPath("$.email").value(ansCreate.getEmail()));
	}

	@Test
	void findAllUsersTest() throws Exception {
		when(userService.findAll()).thenReturn(List.of(ansCreate));

		mvc.perform(get("/users").characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id").value(ansCreate.getId()))
				.andExpect(jsonPath("$[0].name").value(ansCreate.getName()))
				.andExpect(jsonPath("$[0].email").value(ansCreate.getEmail()));
	}

	@Test
	void findUserByIdTest() throws Exception {
		when(userService.findUserById(1L)).thenReturn(ansCreate);

		mvc.perform(get("/users/1").characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(ansCreate.getId()))
				.andExpect(jsonPath("$.name").value(ansCreate.getName()))
				.andExpect(jsonPath("$.email").value(ansCreate.getEmail()));
	}

	@Test
	void updateUserTest() throws Exception {
		when(userService.updateUser(1L, dtoUpdate)).thenReturn(ansUpdate);

		mvc.perform(patch("/users/1").content(mapper.writeValueAsString(dtoUpdate))
				.characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ansUpdate.getId()))
				.andExpect(jsonPath("$.name").value(ansUpdate.getName()))
				.andExpect(jsonPath("$.email").value(ansUpdate.getEmail()));
	}

	@Test
	void deleteUserByIdTest() throws Exception {
		doNothing().when(userService).deleteUserById(1L);
		mvc.perform(delete("/users/1")).andExpect(status().isNoContent());
		verify(userService, times(1)).deleteUserById(1L);
	}
}
