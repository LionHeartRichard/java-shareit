package ru.practicum.shareit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidParamException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleException(final Exception e) {
		log.error("Error: ", e);
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(out));
		return new ErrorResponse(out.toString());
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

	@ExceptionHandler(EmailException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse emailInUse(final Exception e) {
		return new ErrorResponse(e.getMessage());
	}

}
