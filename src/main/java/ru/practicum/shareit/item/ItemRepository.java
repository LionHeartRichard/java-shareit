package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

	public boolean hasItemById(final Long id);

	public Optional<Item> findItemById(final Long id);

	public List<Item> findItemsByOwner(final Long userId);

	public List<Item> searchAvailableItemsByName(final String nameItem);

}
