package ru.practicum.shareit.booking;

import java.util.Map;
import java.util.function.Predicate;

import ru.practicum.shareit.UtilMapper;
import ru.practicum.shareit.common.BookingStatus;
import ru.practicum.shareit.common.StateBooking;

public class UtilBooking {

	private UtilBooking() {
	}

	public static Predicate<Booking> filterByState(StateBooking state) {
		return STATE_HANDLER.get(state).filterTmpState();
	}

	private static final Map<StateBooking, FunctionBooking> STATE_HANDLER = Map.of(StateBooking.ALL, new All(),
			StateBooking.CURRENT, new Current(), StateBooking.FUTURE, new Future(), StateBooking.PAST, new Past(),
			StateBooking.REJECTED, new Rejected(), StateBooking.WAITING, new Waiting());

	private static class All implements FunctionBooking {
		@Override
		public Predicate<Booking> filterTmpState() {
			return v -> true;
		}
	}

	private static class Current implements FunctionBooking {
		@Override
		public Predicate<Booking> filterTmpState() {
			final Long time = UtilMapper.getCurrentTime();
			return v -> (Long.compare(v.getStart(), time) == -1 && Long.compare(time, v.getEnd()) == -1
					&& v.getStatus() == BookingStatus.APPROVED);
		}
	}

	private static class Past implements FunctionBooking {
		@Override
		public Predicate<Booking> filterTmpState() {
			final Long time = UtilMapper.getCurrentTime();
			return v -> (Long.compare(time, v.getEnd()) == -1 && v.getStatus() == BookingStatus.APPROVED);
		}
	}

	private static class Future implements FunctionBooking {
		@Override
		public Predicate<Booking> filterTmpState() {
			final Long time = UtilMapper.getCurrentTime();
			return v -> (Long.compare(v.getStart(), time) == -1 && v.getStatus() == BookingStatus.APPROVED);
		}
	}

	private static class Waiting implements FunctionBooking {
		@Override
		public Predicate<Booking> filterTmpState() {
			return v -> v.getStatus() == BookingStatus.WAITING;
		}
	}

	private static class Rejected implements FunctionBooking {
		@Override
		public Predicate<Booking> filterTmpState() {
			return v -> v.getStatus() == BookingStatus.REJECTED;
		}
	}
}
