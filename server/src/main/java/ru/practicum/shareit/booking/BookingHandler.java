package ru.practicum.shareit.booking;

import java.util.function.Predicate;

@FunctionalInterface
public interface BookingHandler {
	Predicate<Booking> filterTmpState();
}
