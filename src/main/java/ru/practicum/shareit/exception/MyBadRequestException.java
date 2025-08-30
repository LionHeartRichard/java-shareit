package ru.practicum.shareit.exception;

// 400 

public class MyBadRequestException extends RuntimeException {
	public MyBadRequestException(String message) {
		super(message);
	}
}
