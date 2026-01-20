package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.dto.booking.BookingDto;
import ru.practicum.shareit.common.dto.booking.BookingFullDto;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.common.dto.user.UserFullDto;
import ru.practicum.shareit.common.BookingStatus;
import ru.practicum.shareit.common.StateBooking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingControllerTest {

	private static final String HEADER = "X-Sharer-User-Id";
	private static final Long USER_ID = 10L;
	private static final Long BOOKING_ID = 1L;
	private static final Long BOOKING_ID_OTHER = 2L;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	BookingService service;

	static BookingDto dto;
	static BookingFullDto ans;
	static BookingFullDto ansOther;

	@BeforeAll
	static void setUp() {
		dto = BookingDto.builder().itemId(1L).start(LocalDateTime.now().plusHours(1))
				.end(LocalDateTime.now().plusHours(2)).build();
		UserFullDto dtoUser = UserFullDto.builder().id(USER_ID).name("nameUser").email("email@mail.com").build();
		ItemFullDto dtoItem = ItemFullDto.builder().id(1L).name("nameItem").description("item-description")
				.available(true).build();
		ans = BookingFullDto.builder().id(1L).start(dto.getStart()).end(dto.getEnd()).item(dtoItem).booker(dtoUser)
				.status(BookingStatus.WAITING).build();
		ansOther = BookingFullDto.builder().id(BOOKING_ID_OTHER).start(dto.getStart()).end(dto.getEnd()).item(dtoItem)
				.booker(dtoUser).status(BookingStatus.APPROVED).build();
	}

	@Test
	void createBookingTest() throws Exception {
		when(service.createBooking(USER_ID, dto)).thenReturn(ans);

		mockMvc.perform(post("/bookings").header(HEADER, USER_ID).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.item.id").value(ans.getId()))
				.andExpect(jsonPath("$.item.name").value(ans.getItem().getName()))
				.andExpect(jsonPath("$.item.description").value(ans.getItem().getDescription()))
				.andExpect(jsonPath("$.booker.id").value(ans.getBooker().getId()))
				.andExpect(jsonPath("$.booker.name").value(ans.getBooker().getName()))
				.andExpect(jsonPath("$.booker.email").value(ans.getBooker().getEmail()))
				.andExpect(jsonPath("$.status").value(BookingStatus.WAITING.toString()));
	}

	@Test
	void changeStatusBookingTest() throws Exception {
		when(service.approvedByUserIdAndBookingId(USER_ID, BOOKING_ID_OTHER, true)).thenReturn(ansOther);

		mockMvc.perform(patch("/bookings/2").param("approved", "true").header(HEADER, USER_ID)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ansOther.getId()))
				.andExpect(jsonPath("$.item.id").value(ansOther.getItem().getId()))
				.andExpect(jsonPath("$.item.name").value(ansOther.getItem().getName()))
				.andExpect(jsonPath("$.item.description").value(ansOther.getItem().getDescription()))
				.andExpect(jsonPath("$.booker.id").value(ansOther.getBooker().getId()))
				.andExpect(jsonPath("$.booker.name").value(ansOther.getBooker().getName()))
				.andExpect(jsonPath("$.booker.email").value(ansOther.getBooker().getEmail()))
				.andExpect(jsonPath("$.status").value(BookingStatus.APPROVED.toString()));

	}

	@Test
	void findBookingByIdTest() throws Exception {
		when(service.findByUserIdAndBookingId(USER_ID, BOOKING_ID)).thenReturn(ans);

		mockMvc.perform(get("/bookings/1").header(HEADER, USER_ID).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.item.id").value(ans.getId()))
				.andExpect(jsonPath("$.item.name").value(ans.getItem().getName()))
				.andExpect(jsonPath("$.item.description").value(ans.getItem().getDescription()))
				.andExpect(jsonPath("$.booker.id").value(ans.getBooker().getId()))
				.andExpect(jsonPath("$.booker.name").value(ans.getBooker().getName()))
				.andExpect(jsonPath("$.booker.email").value(ans.getBooker().getEmail()))
				.andExpect(jsonPath("$.status").value(BookingStatus.WAITING.toString()));
	}

	@Test
	void findByUserIdAndStateTes() throws Exception {
		List<BookingFullDto> bookings = List.of(ans);

		when(service.findByUserIdAndState(USER_ID, StateBooking.ALL)).thenReturn(bookings);

		mockMvc.perform(
				get("/bookings").param("state", "ALL").header(HEADER, USER_ID).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(ans.getId()))
				.andExpect(jsonPath("$[0].item.id").value(ans.getItem().getId()))
				.andExpect(jsonPath("$[0].item.name").value(ans.getItem().getName()))
				.andExpect(jsonPath("$[0].item.description").value(ans.getItem().getDescription()))
				.andExpect(jsonPath("$[0].booker.id").value(ans.getBooker().getId()))
				.andExpect(jsonPath("$[0].booker.name").value(ans.getBooker().getName()))
				.andExpect(jsonPath("$[0].booker.email").value(ans.getBooker().getEmail()))
				.andExpect(jsonPath("$[0].status").value(BookingStatus.WAITING.toString()));
	}

	@Test
	void findByOwnerIdAndStateTest() throws Exception {
		List<BookingFullDto> bookings = List.of(ans);

		when(service.findByUserIdAndState(USER_ID, StateBooking.ALL)).thenReturn(bookings);

		mockMvc.perform(get("/bookings/owner").param("state", "ALL").header(HEADER, USER_ID)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(ans.getId()))
				.andExpect(jsonPath("$[0].item.id").value(ans.getItem().getId()))
				.andExpect(jsonPath("$[0].item.name").value(ans.getItem().getName()))
				.andExpect(jsonPath("$[0].item.description").value(ans.getItem().getDescription()))
				.andExpect(jsonPath("$[0].booker.id").value(ans.getBooker().getId()))
				.andExpect(jsonPath("$[0].booker.name").value(ans.getBooker().getName()))
				.andExpect(jsonPath("$[0].booker.email").value(ans.getBooker().getEmail()))
				.andExpect(jsonPath("$[0].status").value(BookingStatus.WAITING.toString()));
	}
}
