package ru.practicum.shareit.commentitem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CommentItemRepository extends JpaRepository<CommentItem, Long> {

	@Query(value = "SELECT * FROM comment_item WHERE item_id = :item_id", nativeQuery = true)
	public List<CommentItem> findByItemId(@Param("item_id") final Long itemId);

}
