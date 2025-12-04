package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.dto.comment.CommentDto;
import ru.practicum.shareit.common.dto.comment.CommentTextDto;
import ru.practicum.shareit.common.dto.item.ItemDto;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.common.exception.NotFoundException;
import org.springframework.http.MediaType;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ItemController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemControllerTest {

	private static final String HEADER = "X-Sharer-User-Id";
	private static final Long USER_ID = 1L;
	private static final Long ITEM_ID = 1L;

	@Autowired
	ObjectMapper mapper;

	@MockBean
	ItemService service;

	@Autowired
	MockMvc mvc;

	final ItemDto dto = ItemDto.builder().name("name-item").available(false).description("description-false-available")
			.requestId(2L).build();

	final ItemDto dtoUp = ItemDto.builder().available(true).description("UPDATE - Description - item").requestId(2L)
			.build();

	final CommentDto commentDto = CommentDto.builder().id(1L).text("comment text by item is greate!!!")
			.authorName("User-Name").created(LocalDateTime.of(2020, 1, 10, 10, 10, 10)).build();

	final ItemFullDto ans = ItemFullDto.builder().id(1L).name("name-item").available(false)
			.description("description-false-available").comments(List.of(commentDto)).requestId(2L).build();

	final ItemFullDto ansOther = ItemFullDto.builder().id(2L).name("name-item-other").available(false)
			.description("description-not-available-other-ANS").build();

	final ItemFullDto ansUp = ItemFullDto.builder().id(1L).name("name-item").available(true)
			.description("UPDATE - Description - item").comments(List.of(commentDto)).requestId(2L).build();

	final CommentTextDto textDto = CommentTextDto.builder().text("text-commmmmmmmments").build();

	@Test
	void createItemTest() throws Exception {
		when(service.createItem(USER_ID, dto)).thenReturn(ans);

		mvc.perform(post("/items").header(HEADER, USER_ID).content(mapper.writeValueAsString(dto))
				.characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(ans.getId())).andExpect(jsonPath("$.name").value(ans.getName()))
				.andExpect(jsonPath("$.available").value(ans.getAvailable()))
				.andExpect(jsonPath("$.description").value(ans.getDescription()))
				.andExpect(jsonPath("$.requestId").value(ans.getRequestId()));

	}

	@Test
	void findItemsByOwnerTest() throws Exception {
		when(service.findItemsByOwner(USER_ID)).thenReturn(List.of(ans));

		mvc.perform(get("/items").header(HEADER, USER_ID).characterEncoding(StandardCharsets.UTF_8)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id").value(ans.getId())).andExpect(jsonPath("$[0].name").value(ans.getName()))
				.andExpect(jsonPath("$[0].available").value(ans.getAvailable()))
				.andExpect(jsonPath("$[0].description").value(ans.getDescription()))
				.andExpect(jsonPath("$[0].comments", hasSize(1)))
				.andExpect(jsonPath("$[0].comments[0].id").value(commentDto.getId()))
				.andExpect(jsonPath("$[0].comments[0].text").value(commentDto.getText()))
				.andExpect(jsonPath("$[0].comments[0].authorName").value(commentDto.getAuthorName()))
				.andExpect(jsonPath("$[0].requestId").value(ans.getRequestId()));
	}

	@Test
	void findItemByIdTest() throws Exception {
		when(service.findItemById(USER_ID, ITEM_ID)).thenReturn(ans);

		mvc.perform(get("/items/1").header(HEADER, USER_ID).characterEncoding(StandardCharsets.UTF_8)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ans.getId())).andExpect(jsonPath("$.name").value(ans.getName()))
				.andExpect(jsonPath("$.available").value(ans.getAvailable()))
				.andExpect(jsonPath("$.description").value(ans.getDescription()))
				.andExpect(jsonPath("$.comments", hasSize(1)))
				.andExpect(jsonPath("$.comments[0].id").value(commentDto.getId()))
				.andExpect(jsonPath("$.comments[0].text").value(commentDto.getText()))
				.andExpect(jsonPath("$.comments[0].authorName").value(commentDto.getAuthorName()))
				.andExpect(jsonPath("$.requestId").value(ans.getRequestId()));
	}

	@Test
	void updateItemTest() throws Exception {
		when(service.updateItem(USER_ID, ITEM_ID, dtoUp)).thenReturn(ansUp);

		mvc.perform(patch("/items/1").header(HEADER, USER_ID).content(mapper.writeValueAsString(dtoUp))
				.characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ansUp.getId())).andExpect(jsonPath("$.name").value(ansUp.getName()))
				.andExpect(jsonPath("$.available").value(ansUp.getAvailable()))
				.andExpect(jsonPath("$.description").value(ansUp.getDescription()))
				.andExpect(jsonPath("$.comments", hasSize(1)))
				.andExpect(jsonPath("$.comments[0].id").value(commentDto.getId()))
				.andExpect(jsonPath("$.comments[0].text").value(commentDto.getText()))
				.andExpect(jsonPath("$.comments[0].authorName").value(commentDto.getAuthorName()))
				.andExpect(jsonPath("$.requestId").value(ansUp.getRequestId()));
	}

	@Test
	void searchAvailableItemsByTextTest() throws Exception {
		String searchText = "other";
		when(service.searchAvailableItemsByText(2L, searchText)).thenReturn(List.of(ansOther));

		mvc.perform(get("/items/search").header(HEADER, 2L).param("text", searchText)
				.characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id").value(ansOther.getId()))
				.andExpect(jsonPath("$[0].name").value(ansOther.getName()))
				.andExpect(jsonPath("$[0].available").value(ansOther.getAvailable()))
				.andExpect(jsonPath("$[0].description").value(ansOther.getDescription()))
				.andExpect(jsonPath("$[0].requestId").value(ansOther.getRequestId()));
	}

	@Test
	void searchAvailableItemsByTextNotFoundTest() throws Exception {
		String searchText = "null";
		when(service.searchAvailableItemsByText(USER_ID, searchText)).thenReturn(List.of());

		mvc.perform(get("/items/search").header(HEADER, USER_ID).param("text", searchText)
				.characterEncoding(StandardCharsets.UTF_8).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void addCommentTest() throws Exception {
		when(service.addComment(USER_ID, ITEM_ID, textDto.getText())).thenReturn(commentDto);

		mvc.perform(post("/items/1/comment").header(HEADER, USER_ID).content(mapper.writeValueAsString(textDto))
				.characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(commentDto.getId()))
				.andExpect(jsonPath("$.text").value(commentDto.getText()))
				.andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
				.andExpect(jsonPath("$.created").value("2020-01-10T10:10:10"));
	}

	@Test
	void findItemByIdNotFoundTest() throws Exception {
		when(service.findItemById(USER_ID, ITEM_ID)).thenThrow(new NotFoundException(Item.NOT_FOUND));

		mvc.perform(get("/items/1").header(HEADER, USER_ID).characterEncoding(StandardCharsets.UTF_8)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value(Item.NOT_FOUND));
	}
}
