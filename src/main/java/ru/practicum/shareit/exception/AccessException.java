package ru.practicum.shareit.exception;

// Return status code 403

public class AccessException extends RuntimeException {
	public AccessException(String message) {
		super(message);
	}
}
