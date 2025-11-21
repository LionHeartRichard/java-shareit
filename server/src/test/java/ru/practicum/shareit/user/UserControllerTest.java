package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.practicum.shareit.common.dto.user.UserDto;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
	@Mock
	private UserService service;

	@InjectMocks
	private UserController controller;

	private final ObjectMapper objMapper = new ObjectMapper();

	private MockMvc mvc;
	private UserDto dto;
	private User user;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
		dto = UserDto.builder().id(1L).name("testName").email("testEmail@mail.com").build();
		user = User.builder().id(1L).name("testName").email("testEmail@mail.com").build();
	}

	@Test
	void createUserTest() throws Exception {
		when(service.createUser(any())).thenReturn(user);

		mvc.perform(post("/users").content(objMapper.writeValueAsString(dto)).characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id", is(dto.getId()), Long.class))
				.andExpect(jsonPath("$.name", is(dto.getName()))).andExpect(jsonPath("$.email", is(dto.getEmail())));
	}

	@Test
	void updateUserTest() throws Exception {

	}

	@Test
	void findUserByIdTest() throws Exception {

	}

	@Test
	void deleteUserByIdTest() throws Exception {

	}
}
