package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ItemService {

	ItemRepository itemRepository;
	UserRepository userRepository;
	CommentItemRepository commentItemRepository;
	BookingRepository bookingRepository;

	@Transactional
	public Item createItem(final Long userId, final Item item) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		Item ans = itemRepository.save(item.toBuilder().user(user).build());
		return ans;
	}

	public Item findItemById(final Long id) {
		return itemRepository.findById(id).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
	}

	@Transactional
	public Item updateItem(final Long userId, final Item item) {
		if (userRepository.hasId(userId)) {
			if (item.isOwner(userId)) {
				return itemRepository.save(item);
			}
			throw new AccessException(Item.NOT_OWNER);
		}
		throw new NotFoundException(User.NOT_FOUND);

	}

	public List<Item> findItemsByOwner(final Long userId) {
		return itemRepository.findItemsByUserId(userId);
	}

	public List<Item> searchAvailableItemsByText(final String text) {
		if (text == null || text.isBlank()) {
			return List.of();
		}
		return itemRepository.searchAvailableItemsByText("%" + text + "%");
	}

	public CommentItem addComment(final Long userId, final Long itemId, final String text) {
		if (hasApprovedBooking(userId, itemId)) {
			final Item item = itemRepository.findById(itemId).get();
			final User user = userRepository.findById(userId).get();
			final CommentItem comment = CommentItemMapper.toModel(user, item, text);
			final CommentItem ans = commentItemRepository.save(comment);
			log.error("*****   ans = {}", ans.toString());
			return ans;
		}
		throw new MyBadRequestException(CommentItem.NO_COMMIT);
	}

	public User findUserById(final Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	public List<CommentItem> findCommentsByItemId(final Long itemId) {
		return commentItemRepository.findAllByItemId(itemId);
	}

	public Booking[] findLastBooking(final Long itemId, final Long userId) {
		if (itemRepository.isOwner(itemId, userId)) {
			final Long currentTime = UtilMapper.getCurrentTime();
			Booking lastBooking = bookingRepository.findLastBooking(itemId, currentTime).orElse(null);
			Booking nextBooking = bookingRepository.findNextBooking(itemId, currentTime).orElse(null);
			return new Booking[] {lastBooking, nextBooking};
		}
		return new Booking[] {null, null};
	}

	public Booking findBookingByUserIdByItemId(final Long userId, final Long itemId) {
		return bookingRepository.findByUserIdAndItemId(userId, itemId)
				.orElseThrow(() -> new MyBadRequestException(Booking.NOT_FOUND));
	}

	public boolean hasApprovedBooking(final Long userId, final Long itemId) {
		final Long time = UtilMapper.getCurrentTime();
		if (bookingRepository.hasApprovedBooking(userId, itemId, time)) {
			return true;
		}
		return false;
	}

}
