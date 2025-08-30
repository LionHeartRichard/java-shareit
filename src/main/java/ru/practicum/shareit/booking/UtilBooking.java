package ru.practicum.shareit.booking;

import java.util.Map;
import java.util.function.Predicate;

import ru.practicum.shareit.TmpState;
import ru.practicum.shareit.UtilMapper;

public class UtilBooking {

	private UtilBooking() {
	}

	public static Predicate<Booking> filterByState(TmpState state) {
		return STATE_HANDLER.get(state).filterTmpState();
	}

	private static final Map<TmpState, BookingHandler> STATE_HANDLER = Map.of(TmpState.ALL, new All(), TmpState.CURRENT,
			new Current(), TmpState.FUTURE, new Future(), TmpState.PAST, new Past(), TmpState.REJECTED, new Rejected(),
			TmpState.WAITING, new Waiting());

	private static interface BookingHandler {
		Predicate<Booking> filterTmpState();
	}

	private static class All implements BookingHandler {
		@Override
		public Predicate<Booking> filterTmpState() {
			return v -> true;
		}
	}

	private static class Current implements BookingHandler {
		@Override
		public Predicate<Booking> filterTmpState() {
			final Long time = UtilMapper.getCurrentTime();
			return v -> (Long.compare(v.getStart(), time) == -1 && Long.compare(time, v.getEnd()) == -1
					&& v.getStatus() == BookingStatus.APPROVED);
		}
	}

	private static class Past implements BookingHandler {
		@Override
		public Predicate<Booking> filterTmpState() {
			final Long time = UtilMapper.getCurrentTime();
			return v -> (Long.compare(time, v.getEnd()) == -1 && v.getStatus() == BookingStatus.APPROVED);
		}
	}

	private static class Future implements BookingHandler {
		@Override
		public Predicate<Booking> filterTmpState() {
			final Long time = UtilMapper.getCurrentTime();
			return v -> (Long.compare(v.getStart(), time) == -1 && v.getStatus() == BookingStatus.APPROVED);
		}
	}

	private static class Waiting implements BookingHandler {
		@Override
		public Predicate<Booking> filterTmpState() {
			return v -> v.getStatus() == BookingStatus.WAITING;
		}
	}

	private static class Rejected implements BookingHandler {
		@Override
		public Predicate<Booking> filterTmpState() {
			return v -> v.getStatus() == BookingStatus.REJECTED;
		}
	}
}
