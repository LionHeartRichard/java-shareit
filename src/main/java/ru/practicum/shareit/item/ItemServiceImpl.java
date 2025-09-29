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
import ru.practicum.shareit.commentitem.CommentItemRepository;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.MyBadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

	ItemRepository itemRepository;
	UserRepository userRepository;
	CommentItemRepository commentItemRepository;
	BookingRepository bookingRepository;

	@Transactional
	@Override
	public Item createItem(final Long userId, final Item item) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		Item ans = itemRepository.save(item.toBuilder().user(user).build());
		return ans;
	}

	@Override
	public Item findItemById(final Long id) {
		return itemRepository.findById(id).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
	}

	@Transactional
	@Override
	public Item updateItem(final Long userId, final Item item) {
		if (userRepository.hasId(userId)) {
			if (item.isOwner(userId)) {
				return itemRepository.save(item);
			}
			throw new AccessException(Item.NOT_OWNER);
		}
		throw new NotFoundException(User.NOT_FOUND);

	}

	@Override
	public List<Item> findItemsByOwner(final Long userId) {
		return itemRepository.findItemsByUserId(userId);
	}

	@Override
	public List<Item> searchAvailableItemsByText(final String text) {
		if (text == null || text.isBlank()) {
			return List.of();
		}
		return itemRepository.searchAvailableItemsByText("%" + text + "%");
	}

	@Override
	public CommentItem addComment(final CommentItem commentItem) {
		return commentItemRepository.save(commentItem);
	}

	@Override
	public User findUserById(final Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	@Override
	public List<CommentItem> findCommentsByItemId(final Long itemId) {
		return commentItemRepository.findAllByItemId(itemId);
	}

	@Override
	public Booking[] findLastBooking(final Long itemId, final Long userId) {
		if (itemRepository.isOwner(itemId, userId)) {
			final Long currentTime = UtilMapper.getCurrentTime();
			Booking lastBooking = bookingRepository.findLastBooking(itemId, currentTime).orElse(null);
			Booking nextBooking = bookingRepository.findNextBooking(itemId, currentTime).orElse(null);
			return new Booking[] {lastBooking, nextBooking};
		}
		return new Booking[] {null, null};
	}

	@Override
	public Booking findBookingByUserIdByItemId(final Long userId, final Long itemId) {
		return bookingRepository.findByUserIdAndItemId(userId, itemId)
				.orElseThrow(() -> new MyBadRequestException(Booking.NOT_FOUND));
	}

	@Override
	public boolean hasApprovedBooking(final Long userId, final Long itemId) {
		final Long time = UtilMapper.getCurrentTime();
		if (bookingRepository.hasApprovedBooking(userId, itemId, time)) {
			return true;
		}
		throw new MyBadRequestException(CommentItem.NO_COMMIT);
	}

}
