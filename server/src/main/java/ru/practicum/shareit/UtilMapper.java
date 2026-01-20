package ru.practicum.shareit;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class UtilMapper {

	private static final int HOUR_DIFFERENCE = 3;
	private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

	private UtilMapper() {
	}

	public static Long toLong(LocalDateTime time) {
		return time.atZone(UTC_ZONE_ID).toInstant().toEpochMilli();
	}

	public static LocalDateTime toLocalDateTime(Long time) {
		return Instant.ofEpochMilli(time).atZone(UTC_ZONE_ID).toLocalDateTime();
	}

	public static Long getCurrentTime() {
		return Instant.now().plus(HOUR_DIFFERENCE, ChronoUnit.HOURS).toEpochMilli();
	}
}
