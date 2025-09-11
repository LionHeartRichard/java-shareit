package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Long> {

	public List<Item> findItemsByUserId(final Long userId);

	@Query("FROM Item as i where i.available = true and "
			+ "(upper(i.name) like upper(?1) or upper(i.description) like upper(?1))")
	public List<Item> searchAvailableItemsByText(final String text);

}
