package ru.practicum.shareit.item;

import java.util.List;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.commentitem.CommentItem;
import ru.practicum.shareit.user.User;

public interface ItemService {

	Item createItem(final Long userId, Item item);

	Item findItemById(final Long id);

	Item updateItem(final Long userId, Item model);

	List<Item> findItemsByOwner(final Long ownerId);

	List<Item> searchAvailableItemsByText(String nameItem);

	CommentItem addComment(CommentItem commentItem);

	User findUserById(final Long userId);

	List<CommentItem> findCommentsByItemId(final Long itemId);

	Booking[] findLastBooking(final Long itemId);

	Booking findBookingByUserIdByItemId(final Long userId, final Long itemId);

	boolean hasApprovedBooking(final Long userId, final Long itemId);

}
