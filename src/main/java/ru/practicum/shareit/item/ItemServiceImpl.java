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
		if (userRepository.hasUserById(userId)) {
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
	public List<Item> searchAvailableItemsByText(String text) {
		if (text == null || text.isBlank()) {
			return List.of();
		}
		return itemRepository.searchAvailableItemsByText("%" + text + "%");
	}

	@Override
	public CommentItem addComment(CommentItem commentItem) {
		final Long userId = commentItem.getUser().getId();
		bookingRepository.findByUserIdAndItemId(userId, commentItem.getItem().getId())
				.orElseThrow(() -> new MyBadRequestException(Booking.ERROR_STATUS));
		return commentItemRepository.save(commentItem);
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
		// return bookingRepository.findLastBooking(item.getId(), time);
		// TODO
		List<Booking> mokeListBooking = bookingRepository.findByItemId(item.getId());
		Booking[] mokeAns = new Booking[] {mokeListBooking.get(0), mokeListBooking.get(1)};
		return mokeAns;
	}

}
