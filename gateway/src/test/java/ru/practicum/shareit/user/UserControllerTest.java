package ru.practicum.shareit.user;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.dto.user.UserUpValidDto;
import ru.practicum.shareit.common.dto.user.UserValidDto;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserClient userClient;

	private final Long userId = 123L;

	@Test
	void createUserShouldReturnCreated() throws Exception {
		UserValidDto dto = new UserValidDto();
		dto.setName("Test User");
		dto.setEmail("test@example.com");

		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.CREATED);
		when(userClient.createUser(dto)).thenReturn(response);

		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\": \"Test User\", \"email\": \"test@example.com\"}")).andExpect(status().isCreated());

		verify(userClient).createUser(dto);
	}

	@Test
	void updateUserPatchShouldReturnOk() throws Exception {
		UserUpValidDto dto = new UserUpValidDto();
		dto.setName("Updated User");

		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(userClient.updateUser(userId, dto)).thenReturn(response);

		mockMvc.perform(
				patch("/users/123").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"Updated User\"}"))
				.andExpect(status().isOk());

		verify(userClient).updateUser(userId, dto);
	}

	@Test
	void updateUserPutShouldReturnOk() throws Exception {
		UserUpValidDto dto = new UserUpValidDto();
		dto.setName("Replaced User");

		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(userClient.updateUser(userId, dto)).thenReturn(response);

		mockMvc.perform(
				put("/users/123").contentType(MediaType.APPLICATION_JSON).content("{\"name\": \"Replaced User\"}"))
				.andExpect(status().isOk());

		verify(userClient).updateUser(userId, dto);
	}

	@Test
	void findUserByIdShouldReturnOk() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(userClient.getUser(userId)).thenReturn(response);

		mockMvc.perform(get("/users/123")).andExpect(status().isOk());
		verify(userClient).getUser(userId);
	}

	@Test
	void deleteUserByIdShouldReturnNoContent() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		when(userClient.deleteUser(userId)).thenReturn(response);
		mockMvc.perform(delete("/users/123")).andExpect(status().isNoContent());
		verify(userClient).deleteUser(userId);
	}

	@Test
	void getAllUsersShouldReturnOk() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(userClient.getAllUsers()).thenReturn(response);
		mockMvc.perform(get("/users")).andExpect(status().isOk());
		verify(userClient).getAllUsers();
	}
}
