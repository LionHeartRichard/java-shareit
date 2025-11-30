package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

	List<Item> findByOwnerIdOrderByIdAsc(final Long userId);

	@Query("FROM Item i WHERE i.available = TRUE AND " + "(UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%')) "
			+ "OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%')))")
	List<Item> searchAvailableItemsByText(@Param("text") final String text);

	@Query("SELECT COUNT(i) > 0 FROM Item i WHERE i.id = :id AND i.owner.id = :userId")
	boolean isOwner(@Param("id") final Long id, @Param("userId") final Long userId);

	List<Item> findByRequestIdOrderByRequestIdDesc(Long id);
}
