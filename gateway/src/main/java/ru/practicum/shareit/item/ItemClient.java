package ru.practicum.shareit.item;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.common.dto.comment.RequestCommentCreateDto;
import ru.practicum.shareit.common.dto.item.ItemCreateDto;
import ru.practicum.shareit.common.dto.item.ItemUpdateDto;

@Component
public class ItemClient extends BaseClient {

	private static final String API = "/items";

	public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API))
				.requestFactory(() -> new HttpComponentsClientHttpRequestFactory()).build());
	}

	public ResponseEntity<Object> createItem(Long userId, ItemCreateDto dto) {
		return post("", userId, dto);
	}

	public ResponseEntity<Object> updateItem(Long itemId, Long userId, ItemUpdateDto dto) {
		return put("/" + itemId, userId, dto);
	}

	public ResponseEntity<Object> findById(Long itemId, Long userId) {
		return get("/" + itemId, userId);
	}

	public ResponseEntity<Object> findByOwner(final Long userId) {
		return get("", userId);
	}

	public ResponseEntity<Object> searchByText(String search, Long userId, String text) {
		Map<String, Object> params = Map.of("text", text);
		return get(search, userId, params);
	}

	public ResponseEntity<Object> addComment(Long itemId, Long userId, RequestCommentCreateDto dto) {
		return post("/" + itemId + "/comment", userId, dto);
	}

}
