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

	List<CommentItem> findCommentsByItem(Item itemId);

	Booking[] findLastBooking(Item item);

}
