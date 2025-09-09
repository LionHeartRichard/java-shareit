package ru.practicum.shareit.commentitem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class CommentItemRepository {

	private Map<Long, CommentItem> comments;
	private Long id;

	public CommentItemRepository() {
		id = 0L;
		comments = new HashMap<>();
	}

	public CommentItem saveComment(CommentItem commentItem) {
		++id;
		CommentItem ans = commentItem.toBuilder().id(id).build();
		comments.put(id, ans);
		return ans;
	}

	public List<CommentItem> findCommentsByItemId(final Long itemId) {
		List<CommentItem> ans = comments.values().stream().filter(v -> v.getItemId().equals(itemId)).toList();
		return ans;
	}

}
