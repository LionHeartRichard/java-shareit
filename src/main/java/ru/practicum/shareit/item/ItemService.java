package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

	Item createItem(final Long userId, Item item);

	Item findItemById(final Long id);

	Item updateItem(final Long userId, Item model);

	List<Item> findItemsByOwner(final Long ownerId);

	List<Item> searchAvailableItemsByName(final String nameItem);
}
