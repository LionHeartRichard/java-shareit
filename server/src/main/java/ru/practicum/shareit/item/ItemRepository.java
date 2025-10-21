package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

	List<Item> findItemsByUserId(final Long userId);

	@Query("FROM Item as i where i.available = true and "
			+ "(upper(i.name) like upper(?1) or upper(i.description) like upper(?1))")
	List<Item> searchAvailableItemsByText(final String text);

	@Query(value = "select COUNT(*)>0 from item where id = :id and user_id = :user_id;", nativeQuery = true)
	boolean isOwner(@Param("id") final Long id, @Param("user_id") final Long userId);

}
