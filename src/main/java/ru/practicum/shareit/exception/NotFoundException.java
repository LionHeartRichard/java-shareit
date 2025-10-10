package ru.practicum.shareit.exception;

// Return status code 404

public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}
