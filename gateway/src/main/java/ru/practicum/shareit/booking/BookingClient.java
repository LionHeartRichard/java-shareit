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
import ru.practicum.shareit.common.dto.booking.BookingRequestDto;

@Component
public class BookingClient extends BaseClient {

	private static final String API = "/bookings";

	@Autowired
	public BookingClient(@Value("${shareit-server.url}") final String serverUrl, RestTemplateBuilder builder) {
		super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API))
				.requestFactory(() -> new HttpComponentsClientHttpRequestFactory()).build());
	}

	public ResponseEntity<Object> getBookings(final Long userId, final BookingState state, final Integer from,
			final Integer size) {
		Map<String, Object> params = Map.of("state", state.name(), "from", from, "size", size);
		return get("?state={state}&from={from}&size={size}", userId, params);
	}

	public ResponseEntity<Object> createBooking(final Long userId, final BookingRequestDto requestDto) {
		return post("", userId, requestDto);
	}

	public ResponseEntity<Object> getBooking(final Long userId, final Long bookingId) {
		return get("/" + bookingId, userId);
	}

	public ResponseEntity<Object> findByOwnerAndState(final String path, final Long userId, final BookingState state) {
		Map<String, Object> params = Map.of("state", state.name());
		return get(path, userId, params);
	}

	public ResponseEntity<Object> approved(final Long bookingId, final Long userId, final Boolean approved) {
		Map<String, Object> params = Map.of("approved", approved);
		return patch("/" + bookingId, userId, params, null);
	}
}
