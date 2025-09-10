package ru.practicum.shareit.commentitem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentItemRepository extends JpaRepository<CommentItem, Long> {

	public List<CommentItem> findCommentItemByItemId(final Long itemId);

}
