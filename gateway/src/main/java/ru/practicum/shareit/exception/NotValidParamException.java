package ru.practicum.shareit.exception;

public class NotValidParamException extends RuntimeException {

	private final StringBuilder message;

	public NotValidParamException(String... params) {
		message = new StringBuilder("Not valid params: \n");
		for (String param : params) {
			message.append(param + " \n");
		}
	}

	@Override
	public String getMessage() {
		return message.toString();
	}
}
