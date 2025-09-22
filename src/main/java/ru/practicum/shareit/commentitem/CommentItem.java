package ru.practicum.shareit.commentitem;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Entity
@Table(name = "comment_item")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String text;

	@JoinColumn(name = "item_id")
	@ManyToOne(fetch = FetchType.LAZY)
	Item item;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	User user;

	Long created;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentItem other = (CommentItem) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public static final String NO_COMMIT = "The user cannot leave a comment because he is not the owner";
}
