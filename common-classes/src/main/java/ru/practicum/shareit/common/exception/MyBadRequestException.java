package ru.practicum.shareit.common.exception;

// Return status code 400

public class MyBadRequestException extends RuntimeException {
	public MyBadRequestException(String message) {
		super(message);
	}
}
