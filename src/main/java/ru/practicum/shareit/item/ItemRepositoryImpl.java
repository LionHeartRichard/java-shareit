package ru.practicum.shareit.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

	private Long id;
	private Map<Long, Item> items;

	public ItemRepositoryImpl() {
		id = 0L;
		items = new HashMap<>();
	}

	public boolean hasItemById(final Long id) {
		return items.containsKey(id);
	}

	public Optional<Item> findItemById(final Long id) {
		return Optional.ofNullable(items.get(id));
	}

	public Item saveItem(Item item) {
		++id;
		Item ans = item.toBuilder().id(id).build();
		items.put(id, ans);
		return ans;
	}

	public Item update(Item item) {
		items.put(item.getId(), item);
		return item;
	}

	@Override
	public List<Item> findItemsByOwner(Long userId) {
		return items.values().stream().filter(v -> v.isOwner(userId)).toList();
	}

	@Override
	public List<Item> searchAvailableItemsByName(String nameItem) {
		return items.values().stream().filter(v -> v.isAvailable(nameItem)).toList();
	}
}
