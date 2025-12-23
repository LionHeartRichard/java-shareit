package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.common.dto.user.UserValidDto;
import ru.practicum.shareit.common.dto.user.UserUpValidDto;

@Component
public class UserClient extends BaseClient {

	private static final String API = "/users";

	@Autowired
	public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API))
				.requestFactory(() -> new HttpComponentsClientHttpRequestFactory()).build());
	}

	public ResponseEntity<Object> getAllUsers() {
		return get("");
	}

	public ResponseEntity<Object> getUser(Long userId) {
		return get("/" + userId);
	}

	public ResponseEntity<Object> createUser(UserValidDto userDto) {
		return post("", userDto);
	}

	public ResponseEntity<Object> updateUser(Long userId, UserUpValidDto userDto) {
		return put("/" + userId, userId, userDto);
	}

	public ResponseEntity<Object> deleteUser(Long userId) {
		return delete("/" + userId, userId);
	}

}
