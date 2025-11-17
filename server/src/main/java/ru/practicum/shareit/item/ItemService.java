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
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.common.exception.AccessException;
import ru.practicum.shareit.common.exception.MyBadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ItemService {

	ItemRepository repoItem;
	UserRepository repoUser;
	CommentRepository repoComment;
	BookingRepository repoBooking;

	@Transactional
	public Item createItem(final Long userId, final Item item) {
		User user = repoUser.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		Item ans = repoItem.save(item.toBuilder().user(user).build());
		return ans;
	}

	public Item findItemById(final Long id) {
		return repoItem.findById(id).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
	}

	@Transactional
	public Item updateItem(final Long userId, final Item item) {
		if (repoUser.hasId(userId)) {
			if (item.isOwner(userId)) {
				return repoItem.save(item);
			}
			throw new AccessException(Item.NOT_OWNER);
		}
		throw new NotFoundException(User.NOT_FOUND);

	}

	public List<Item> findItemsByOwner(final Long userId) {
		return repoItem.findItemsByUserId(userId);
	}

	public List<Item> searchAvailableItemsByText(final String text) {
		if (text == null || text.isBlank()) {
			return List.of();
		}
		return repoItem.searchAvailableItemsByText("%" + text + "%");
	}

	@Transactional
	public Comment addComment(final Long userId, final Long itemId, final String text) {
		if (hasApprovedBooking(userId, itemId)) {
			final Item item = repoItem.findById(itemId).get();
			final User user = repoUser.findById(userId).get();
			final Comment comment = CommentMapper.toModel(user, item, text);
			final Comment ans = repoComment.save(comment);
			return ans;
		}
		throw new MyBadRequestException(Comment.NO_COMMIT);
	}

	public User findUserById(final Long userId) {
		return repoUser.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	public List<Comment> findCommentsByItemId(final Long itemId) {
		return repoComment.findAllByItemId(itemId);
	}

	public Booking[] findLastBooking(final Long itemId, final Long userId) {
		if (repoItem.isOwner(itemId, userId)) {
			final Long currentTime = UtilMapper.getCurrentTime();
			Booking lastBooking = repoBooking.findLastBooking(itemId, currentTime).orElse(null);
			Booking nextBooking = repoBooking.findNextBooking(itemId, currentTime).orElse(null);
			return new Booking[] {lastBooking, nextBooking};
		}
		return new Booking[] {null, null};
	}

	public Booking findBookingByUserIdByItemId(final Long userId, final Long itemId) {
		return repoBooking.findByUserIdAndItemId(userId, itemId)
				.orElseThrow(() -> new MyBadRequestException(Booking.NOT_FOUND));
	}

	public boolean hasApprovedBooking(final Long userId, final Long itemId) {
		final Long time = UtilMapper.getCurrentTime();
		if (repoBooking.hasApprovedBooking(userId, itemId, time)) {
			return true;
		}
		return false;
	}

}
