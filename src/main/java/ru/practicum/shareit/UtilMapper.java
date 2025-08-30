package ru.practicum.shareit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class UtilMapper {

	private UtilMapper() {
	}

	public static Long toLong(LocalDateTime time) {
		return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public static LocalDateTime toLocalDateTime(Long time) {
		return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static Long getCurrentTime() {
		return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
}
