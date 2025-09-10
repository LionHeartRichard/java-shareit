package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

	public List<Item> findItemsByOwner(final Long userId);

	public List<Item> searchAvailableItemsByName(final String nameItem);

}
