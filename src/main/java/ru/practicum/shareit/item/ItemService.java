package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.UtilMapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.commentitem.CommentItem;
import ru.practicum.shareit.commentitem.CommentItemMapper;
import ru.practicum.shareit.commentitem.CommentItemRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.MyBadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ItemService {

	ItemRepository repItem;
	UserRepository repUser;
	CommentItemRepository repComment;
	BookingRepository repBooking;

	@Transactional
	public Item createItem(final Long userId, final Item item) {
		User user = repUser.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		Item ans = repItem.save(item.toBuilder().user(user).build());
		return ans;
	}

	public Item findItemById(final Long id) {
		return repItem.findById(id).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
	}

	@Transactional
	public Item updateItem(final Long userId, final Item item) {
		if (repUser.hasId(userId)) {
			if (item.isOwner(userId)) {
				return repItem.save(item);
			}
			throw new AccessException(Item.NOT_OWNER);
		}
		throw new NotFoundException(User.NOT_FOUND);

	}

	public List<Item> findItemsByOwner(final Long userId) {
		return repItem.findItemsByUserId(userId);
	}

	public List<Item> searchAvailableItemsByText(final String text) {
		if (text == null || text.isBlank()) {
			return List.of();
		}
		return repItem.searchAvailableItemsByText("%" + text + "%");
	}

	@Transactional
	public CommentItem addComment(final Long userId, final Long itemId, final String text) {
		if (hasApprovedBooking(userId, itemId)) {
			final Item item = repItem.findById(itemId).get();
			final User user = repUser.findById(userId).get();
			final CommentItem comment = CommentItemMapper.toModel(user, item, text);
			final CommentItem ans = repComment.save(comment);
			return ans;
		}
		throw new MyBadRequestException(CommentItem.NO_COMMIT);
	}

	public User findUserById(final Long userId) {
		return repUser.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	public List<CommentItem> findCommentsByItemId(final Long itemId) {
		return repComment.findAllByItemId(itemId);
	}

	public Booking[] findLastBooking(final Long itemId, final Long userId) {
		if (repItem.isOwner(itemId, userId)) {
			final Long currentTime = UtilMapper.getCurrentTime();
			Booking lastBooking = repBooking.findLastBooking(itemId, currentTime).orElse(null);
			Booking nextBooking = repBooking.findNextBooking(itemId, currentTime).orElse(null);
			return new Booking[] {lastBooking, nextBooking};
		}
		return new Booking[] {null, null};
	}

	public Booking findBookingByUserIdByItemId(final Long userId, final Long itemId) {
		return repBooking.findByUserIdAndItemId(userId, itemId)
				.orElseThrow(() -> new MyBadRequestException(Booking.NOT_FOUND));
	}

	public boolean hasApprovedBooking(final Long userId, final Long itemId) {
		final Long time = UtilMapper.getCurrentTime();
		if (repBooking.hasApprovedBooking(userId, itemId, time)) {
			return true;
		}
		return false;
	}

}
