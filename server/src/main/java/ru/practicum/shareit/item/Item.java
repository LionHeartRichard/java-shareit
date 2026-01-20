package ru.practicum.shareit.item;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	Long id;

	@Column(nullable = false)
	String name;

	@Column(nullable = false)
	String description;

	@Column(name = "is_available", nullable = false)
	Boolean available;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "owner_id")
	User owner;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "request_id")
	Request request;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Item item = (Item) o;
		if (item.id != null && id != null) {
			return id.equals(item.id);
		}
		return Objects.equals(description, item.description) && Objects.equals(name, item.name)
				&& Objects.equals(owner, item.owner) && Objects.equals(available, item.available)
				&& Objects.equals(request, item.request);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : Objects.hash(description, name, owner, available, request);
	}

	public static final String NOT_FOUND = "Item not found!";
	public static final String NOT_OWNER = "User is not the owner of item!";
	public static final String IS_OWNER = "User is owner of item!";
	public static final String NOT_AVAILABLE = "Item is not available!";

	public boolean isOwner(final Long userId) {
		return this.owner.getId().equals(userId);
	}

	public boolean isAvailable(String nameItem) {
		return this.available && this.name.equals(nameItem);
	}

}
