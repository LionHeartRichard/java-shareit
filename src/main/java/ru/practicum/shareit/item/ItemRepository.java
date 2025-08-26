package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

	public boolean hasItemById(final Long id);

	public Optional<Item> findItemById(final Long id);

	public Item saveItem(Item item);

	public Item update(Item item);

	public List<Item> findItemsByOwner(final Long userId);

	public List<Item> searchAvailableItemsByName(final String nameItem);

}
