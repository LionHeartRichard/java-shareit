package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.stereotype.Service;

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

	@Override
	public Item createItem(final Long userId, Item item) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		Item ans = itemRepository.save(item.toBuilder().user(user).build());
		return ans;
	}

	@Override
	public Item findItemById(final Long id) {
		return itemRepository.findById(id).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
	}

	@Override
	public Item updateItem(final Long userId, Item item) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		if (item.isOwner(user.getId())) {
			return itemRepository.save(item);
		}
		throw new AccessException(Item.NOT_OWNER);

	}

	@Override
	public List<Item> findItemsByOwner(final Long userId) {
		return itemRepository.findItemsByOwner(userId);
	}

	@Override
	public List<Item> searchAvailableItemsByName(final String nameItem) {
		if (nameItem == null || nameItem.isBlank()) {
			return List.of();
		}
		return itemRepository.searchAvailableItemsByName(nameItem);
	}

	@Override
	public CommentItem addComment(CommentItem commentItem) {
		final Long userId = commentItem.getUser().getId();
		if (bookingRepository.youBooked(userId, commentItem.getItem().getId())) {
			return commentItemRepository.save(commentItem);
		}
		throw new MyBadRequestException(Booking.ERROR_STATUS);

	}

	@Override
	public User findUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
	}

	@Override
	public List<CommentItem> findCommentsByItem(Item item) {
		return commentItemRepository.findCommentItemByItemId(item.getId());
	}

	@Override
	public Booking[] findLastBooking(Item item) {
		Long time = UtilMapper.getCurrentTime();
		return bookingRepository.findLastBooking(item.getId(), time);
	}

}
