package ru.practicum.shareit.booking;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.common.dto.booking.BookingValidDto;
import ru.practicum.shareit.common.StateBooking;

@Component
public class BookingClient extends BaseClient {

	private static final String API = "/bookings";

	@Autowired
	public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
		super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API))
				.requestFactory(() -> new HttpComponentsClientHttpRequestFactory()).build());
	}

	public ResponseEntity<Object> getBookings(Long userId, StateBooking state, Integer from, Integer size) {
		Map<String, Object> params = Map.of("state", state.name(), "from", from, "size", size);
		return get("?state={state}&from={from}&size={size}", userId, params);
	}

	public ResponseEntity<Object> createBooking(Long userId, BookingValidDto dto) {
		return post("", userId, dto);
	}

	public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
		return get("/" + bookingId, userId);
	}

	public ResponseEntity<Object> findByOwnerAndState(String path, Long userId, StateBooking state) {
		Map<String, Object> params = Map.of("state", state.name());
		return get(path, userId, params);
	}

	public ResponseEntity<Object> approved(Long bookingId, Long userId, Boolean approved) {
		return patch(("/" + bookingId + "?approved=" + approved), userId, null);
	}
}
