package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.dto.item.ItemNewDto;
import ru.practicum.shareit.common.dto.request.RequestDto;
import ru.practicum.shareit.common.dto.request.RequestFullDto;
import ru.practicum.shareit.common.dto.user.UserDto;
import ru.practicum.shareit.common.dto.user.UserFullDto;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.UserService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestServiceTest {

	@Autowired
	UserService servUser;

	@Autowired
	RequestService servRequest;

	static UserDto userDto;
	static ItemNewDto itemDto;
	static RequestDto requestDto;

	private static final int FROM = 0;
	private static final int SIZE = 10;
	private static final Long USER_ID_NOT_FOUND = 999L;

	@BeforeAll
	static void setUp() {
		userDto = UserDto.builder().name("userName").email("email@mail.com").build();
		itemDto = ItemNewDto.builder().name("itemName").description("item DESCRIPTION").available(true).build();
		requestDto = RequestDto.builder().description("Request - Description").build();
	}

	@Test
	void createUserAndRequestAndFindRequestTest() {
		UserFullDto ans = servUser.createUser(userDto);
		RequestFullDto ansRequest = servRequest.createRequest(ans.getId(), requestDto);
		RequestFullDto findRequest = servRequest.findRequestByUserId(ans.getId(), ansRequest.getId());

		assertThat(findRequest.getId()).isEqualTo(ansRequest.getId());
		assertThat(findRequest.getDescription()).isEqualTo(ansRequest.getDescription());
		assertThat(findRequest.getRequesterName()).isEqualTo(ans.getName());
	}

	@Test
	void throwExceptionWhenUserIsNotFoundTest() {
		assertThatThrownBy(() -> servRequest.createRequest(USER_ID_NOT_FOUND, requestDto))
				.isInstanceOf(NotFoundException.class);
	}

	@Test
	void throwExceptionWhenIdIsNullTest() {
		assertThatThrownBy(() -> servRequest.createRequest(null, requestDto))
				.isInstanceOf(InvalidDataAccessApiUsageException.class);
	}

	@Test
	void findItemRequestByUserIdTest() {
		UserFullDto ans = servUser.createUser(userDto);
		RequestFullDto ansRequest = servRequest.createRequest(ans.getId(), requestDto);

		List<RequestFullDto> requests = servRequest.findAllRequestsByUserId(ans.getId());

		assertThat(requests).hasSize(1);
		assertThat(requests.getFirst().getId()).isEqualTo(ansRequest.getId());
		assertThat(requests.getFirst().getDescription()).isEqualTo(ansRequest.getDescription());
		assertThat(requests.getFirst().getRequesterName()).isEqualTo(ansRequest.getRequesterName());
	}

	@Test
	void findAllRequestsTest() {
		UserFullDto ans = servUser.createUser(userDto);
		RequestFullDto ansRequest = servRequest.createRequest(ans.getId(), requestDto);

		List<RequestFullDto> requests = servRequest.findAll(ans.getId(), FROM, SIZE);

		assertThat(requests).hasSize(1);
		assertThat(requests.getFirst().getId()).isEqualTo(ansRequest.getId());
		assertThat(requests.getFirst().getDescription()).isEqualTo(ansRequest.getDescription());
		assertThat(requests.getFirst().getRequesterName()).isEqualTo(ansRequest.getRequesterName());
	}
}
