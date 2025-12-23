package ru.practicum.shareit.booking;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.StateBooking;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookingClient bookingClient;

	private final Long userId = 123L;
	private final Long bookingId = 456L;

	@Test
	void getBooking_shouldReturnOk() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(bookingClient.getBooking(userId, bookingId)).thenReturn(response);

		mockMvc.perform(get("/bookings/456").header("X-Sharer-User-Id", userId)).andExpect(status().isOk());

		verify(bookingClient).getBooking(userId, bookingId);
	}

	@Test
	void getBookings_withDefaultParams_shouldCallClient() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(bookingClient.getBookings(userId, StateBooking.ALL, 0, 10)).thenReturn(response);

		mockMvc.perform(get("/bookings").header("X-Sharer-User-Id", userId)).andExpect(status().isOk());

		verify(bookingClient).getBookings(userId, StateBooking.ALL, 0, 10);
	}

	@Test
	void getBookings_withStateAndPagination_shouldPassParams() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(bookingClient.getBookings(userId, StateBooking.WAITING, 5, 20)).thenReturn(response);

		mockMvc.perform(get("/bookings").header("X-Sharer-User-Id", userId).param("state", "WAITING").param("from", "5")
				.param("size", "20")).andExpect(status().isOk());

		verify(bookingClient).getBookings(userId, StateBooking.WAITING, 5, 20);
	}

	@Test
	void findByOwnerAndState_defaultState_shouldCallClientWithALL() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(bookingClient.findByOwnerAndState("/owner", userId, StateBooking.ALL)).thenReturn(response);

		mockMvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", userId)).andExpect(status().isOk());

		verify(bookingClient).findByOwnerAndState("/owner", userId, StateBooking.ALL);
	}

	@Test
	void findByOwnerAndState_withStateParam_shouldPassState() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
		when(bookingClient.findByOwnerAndState("/owner", userId, StateBooking.REJECTED)).thenReturn(response);

		mockMvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", userId).param("state", "REJECTED"))
				.andExpect(status().isOk());

		verify(bookingClient).findByOwnerAndState("/owner", userId, StateBooking.REJECTED);
	}

	// --- TEST PATCH /bookings/{id} (approvedByUserIdAndBookingId) ---

	@Test
	void approved_shouldCallClientWithTrue() throws Exception {
		ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.ACCEPTED);
		when(bookingClient.approved(bookingId, userId, true)).thenReturn(response);

		mockMvc.perform(patch("/bookings/456?approved=true").header("X-Sharer-User-Id", userId))
				.andExpect(status().isAccepted());

		verify(bookingClient).approved(bookingId, userId, true);
	}
}
