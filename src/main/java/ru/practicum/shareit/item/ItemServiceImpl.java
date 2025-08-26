package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

	ItemRepository itemRepository;
	UserRepository userRepository;

	@Override
	public Item createItem(final Long userId, Item item) {
		if (userRepository.hasUserById(userId)) {
			Item swap = item.toBuilder().userId(userId).build();
			Item ans = itemRepository.saveItem(swap);
			return ans;
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	@Override
	public Item findItemById(final Long id) {
		return itemRepository.findItemById(id).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
	}

	@Override
	public Item updateItem(final Long userId, Item item) {
		if (userRepository.hasUserById(userId)) {
			if (item.isOwner(userId)) {
				return itemRepository.update(item);
			}
			throw new AccessException(Item.NOT_OWNER);
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	@Override
	public List<Item> findItemsByOwner(final Long userId) {
		return itemRepository.findItemsByOwner(userId);
	}

	@Override
	public List<Item> searchAvailableItemsByName(final String nameItem) {
		if (nameItem == null || nameItem.isBlank()) {
			return List.of();
		}
		return itemRepository.searchAvailableItemsByName(nameItem);
	}

}
