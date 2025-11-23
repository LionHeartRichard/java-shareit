package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	@Mock
	private UserService service;

	@InjectMocks
	private UserController controller;

	private final ObjectMapper objMapper = new ObjectMapper();
	private MockMvc mvc;
	private User user;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
		user = User.builder().id(1L).name("testName").email("testEmail@mail.com").build();

		when(service.createUser(any(User.class))).thenReturn(user);
	}

	@Test
	void createUserTest() throws Exception {
		mvc.perform(post("/users").content(objMapper.writeValueAsString(user)).characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(user.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(user.getName()))).andExpect(jsonPath("$.email", is(user.getEmail())));
	}

//	@Test
//	void updateUserTest() throws Exception {
//		User upUser = User.builder().id(1L).name("updatedName").email("updatedEmail@mail.com").build();
//
//		when(service.updateUser(any(User.class))).thenReturn(upUser);
//
//		mvc.perform(patch("/users/{id}", 1L).content(objMapper.writeValueAsString(upUser))
//				.characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//				.andExpect(jsonPath("$.id", is(upUser.getId()), Long.class))
//				.andExpect(jsonPath("$.name", is(upUser.getName())))
//				.andExpect(jsonPath("$.email", is(upUser.getEmail())));
//	}
//
//	@Test
//	void findUserByIdTest() throws Exception {
//		when(service.findUserById(1L)).thenReturn(user);
//
//		mvc.perform(get("/users/{id}", 1L).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//				.andExpect(jsonPath("$.id", is(user.getId()), Long.class))
//				.andExpect(jsonPath("$.name", is(user.getName()))).andExpect(jsonPath("$.email", is(user.getEmail())));
//	}
//
//	@Test
//	void deleteUserByIdTest() throws Exception {
//		doNothing().when(service).deleteUserById(1L);
//
//		mvc.perform(delete("/users/{id}", 1L)).andExpect(status().isNoContent());
//	}
}
