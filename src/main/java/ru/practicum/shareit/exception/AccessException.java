package ru.practicum.shareit.exception;

// 403

public class AccessException extends RuntimeException {
	public AccessException(String message) {
		super(message);
	}
}
