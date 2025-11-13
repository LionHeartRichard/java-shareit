package ru.practicum.shareit.common.exception;

//Return status code 409

public class ConflictException extends RuntimeException {
	public ConflictException(String message) {
		super(message);
	}
}
