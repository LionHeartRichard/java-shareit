package ru.practicum.shareit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.MyBadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.NotValidParamException;
import org.junit.jupiter.api.extension.ExtendWith;

@ActiveProfiles("default")
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class ErrorHandlerTest {

	@InjectMocks
	private ErrorHandler errorHandler;

	@Test
	void notFoundExceptionShouldReturn404() {
		NotFoundException e = new NotFoundException("Item not found");

		ErrorResponse response = errorHandler.notFoundException(e);

		assertEquals("Item not found", response.getError());
		// HTTP 404 гарантируется @ResponseStatus(HttpStatus.NOT_FOUND)
	}

	@Test
	void notValidExceptionShouldReturn400() {
		NotValidParamException e = new NotValidParamException("Invalid parameter");

		ErrorResponse response = errorHandler.notValidException(e);
		StringBuilder message = new StringBuilder("Not valid params: \n");
		message.append("Invalid parameter" + " \n");
		assertEquals(message.toString(), response.getError());
		// HTTP 400 гарантируется @ResponseStatus(HttpStatus.BAD_REQUEST)
	}

	@Test
	void conflictExceptionShouldReturn409() {
		ConflictException e = new ConflictException("Conflict occurred");

		ErrorResponse response = errorHandler.conflictExceprion(e);

		assertEquals("Conflict occurred", response.getError());
		// HTTP 409 гарантируется @ResponseStatus(HttpStatus.CONFLICT)
	}

	@Test
	void badRequestExceptionShouldReturn400() {
		MyBadRequestException e = new MyBadRequestException("Bad request");

		ErrorResponse response = errorHandler.badRequestException(e);

		assertEquals("Bad request", response.getError());
		// HTTP 400 гарантируется @ResponseStatus(HttpStatus.BAD_REQUEST)
	}

	@Test
	void handleExceptionWithNullMessageShouldReturnNull() {
		Exception e = new Exception(); // без сообщения

		ErrorResponse response = errorHandler.handleException(e);

		assertNull(response.getError()); // message будет null
	}

	@Test
	void notFoundExceptionWithNullMessageShouldReturnNull() {
		NotFoundException e = new NotFoundException(null);

		ErrorResponse response = errorHandler.notFoundException(e);
		assertNull(response.getError());
	}
}
