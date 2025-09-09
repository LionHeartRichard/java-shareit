package ru.practicum.shareit.commentitem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.user.User;

@Entity
@Table(name = "comment_item")
@Value
@Builder(toBuilder = true)
public class CommentItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String text;

	@Column(name = "item_id")
	@ManyToOne(fetch = FetchType.LAZY)
	Long itemId;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	User user;

	Long created;
}
