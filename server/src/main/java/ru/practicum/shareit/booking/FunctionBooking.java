package ru.practicum.shareit.booking;

import java.util.function.Predicate;

@FunctionalInterface
public interface FunctionBooking {
	Predicate<Booking> filterTmpState();
}
