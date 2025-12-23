package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.MyBadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.NotValidParamException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleException(final Exception e) {
		log.error("Error: ", e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse notFoundException(final Exception e) {
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler(NotValidParamException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse notValidException(final Exception e) {
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler(ConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse conflictExceprion(final Exception e) {
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler(MyBadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse badRequestException(final Exception e) {
		return new ErrorResponse(e.getMessage());
	}
}
