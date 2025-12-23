package ru.practicum.shareit.item;

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
import ru.practicum.shareit.common.dto.comment.CommentValidDto;
import ru.practicum.shareit.common.dto.item.ItemUpValidDto;
import ru.practicum.shareit.common.dto.item.ItemValidDto;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemClient itemClient;

	private final Long userId = 123L;
	private final Long itemId = 456L;

	// --- TEST POST /items (createItem) ---

	@Test
	void createItem_shouldReturnCreated() throws Exception {
		ItemValidDto dto = new ItemValidDto();
		dto.setName("Test Item");
		dto.setDescription("Description");
		dto.setAvailable(true);

		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.CREATED);
		when(itemClient.createItem(userId, dto)).thenReturn(response);

		mockMvc.perform(post("/items").header("X-Sharer-User-Id", userId).contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\": \"Test Item\", \"description\": \"Description\", \"available\": true}"))
				.andExpect(status().isCreated());

		verify(itemClient).createItem(userId, dto);
	}

	// --- TEST PATCH /items/{itemId} (updateItem) ---

	@Test
	void updateItem_shouldReturnOk() throws Exception {
		ItemUpValidDto dto = new ItemUpValidDto();
		dto.setName("Updated Item");

		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(itemClient.updateItem(itemId, userId, dto)).thenReturn(response);

		mockMvc.perform(patch("/items/456").header("X-Sharer-User-Id", userId).contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\": \"Updated Item\"}")).andExpect(status().isOk());

		verify(itemClient).updateItem(itemId, userId, dto);
	}

	// --- TEST GET /items/{itemId} (findItemById) ---

	@Test
	void findItemById_shouldReturnOk() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(itemClient.findById(itemId, userId)).thenReturn(response);

		mockMvc.perform(get("/items/456").header("X-Sharer-User-Id", userId)).andExpect(status().isOk());

		verify(itemClient).findById(itemId, userId);
	}

	// --- TEST GET /items (findItemsByOwner) ---

	@Test
	void findItemsByOwner_shouldReturnOk() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(itemClient.findByOwner(userId)).thenReturn(response);

		mockMvc.perform(get("/items").header("X-Sharer-User-Id", userId)).andExpect(status().isOk());

		verify(itemClient).findByOwner(userId);
	}

	// --- TEST GET /items/search (searchAvailableItemsByText) ---

	@Test
	void searchAvailableItemsByText_shouldReturnOk() throws Exception {
		String text = "laptop";
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(itemClient.searchByText("/search", userId, text)).thenReturn(response);

		mockMvc.perform(get("/items/search").param("text", text).header("X-Sharer-User-Id", userId))
				.andExpect(status().isOk());

		verify(itemClient).searchByText("/search", userId, text);
	}

	// --- TEST POST /items/{itemId}/comment (addComment) ---

	@Test
	void addComment_shouldReturnOk() throws Exception {
		CommentValidDto dto = new CommentValidDto();
		dto.setText("Great item!");

		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(itemClient.addComment(itemId, userId, dto)).thenReturn(response);

		mockMvc.perform(post("/items/456/comment").header("X-Sharer-User-Id", userId)
				.contentType(MediaType.APPLICATION_JSON).content("{\"text\": \"Great item!\"}"))
				.andExpect(status().isOk());

		verify(itemClient).addComment(itemId, userId, dto);
	}
}
