package ru.practicum.shareit.item;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.UtilMapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.common.dto.comment.CommentDto;
import ru.practicum.shareit.common.dto.item.ItemDto;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.common.exception.MyBadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class ItemService {

	ItemRepository repoItem;
	UserRepository repoUser;
	CommentRepository repoComment;
	BookingRepository repoBooking;
	RequestRepository requestRepo;

	@Transactional
	public ItemFullDto createItem(final Long userId, final ItemDto dto) {
		User user = repoUser.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		Item ans = repoItem.save(ItemMapper.toModel(dto).toBuilder().owner(user).build());

		if (dto.hasRequestId()) {
			Request request = requestRepo.findById(dto.getRequestId())
					.orElseThrow(() -> new NotFoundException(Request.NOT_FOUND));
			ans.setRequest(request);
		}
		return ItemMapper.toDto(ans);
	}

	@Transactional
	public ItemFullDto updateItem(final Long userId, final Long itemId, final ItemDto dto) {
		if (!repoUser.hasId(userId)) {
			throw new NotFoundException(User.NOT_FOUND);
		}

		Item item = repoItem.findById(itemId).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
		if (!item.isOwner(userId)) {
			throw new MyBadRequestException(Item.NOT_OWNER);
		}
		if (item.getRequest() != null) {
			Request request = requestRepo.findById(item.getId())
					.orElseThrow(() -> new NotFoundException(Request.NOT_FOUND));
			item.setRequest(request);
		}

		Item ans = repoItem.save(ItemMapper.toModel(item, dto));
		return ItemMapper.toDto(ans);
	}

	public ItemFullDto findItemById(final Long userId, final Long itemId) {
		Item item = repoItem.findById(itemId).orElseThrow(() -> new NotFoundException(Item.NOT_FOUND));
		if (!repoUser.hasId(userId)) {
			throw new NotFoundException(User.NOT_FOUND);
		}
		return setFullDto(userId, item);
	}

	private ItemFullDto setFullDto(Long userId, Item item) {
		List<CommentDto> comments = repoComment.findAllByItemId(item.getId()).stream().map(v -> CommentMapper.toDto(v))
				.toList();
		Booking[] bookings = findLastBooking(item.getId(), userId);
		return ItemMapper.toDto(item, comments, bookings);
	}

	private Booking[] findLastBooking(final Long itemId, final Long userId) {
		if (repoItem.isOwner(itemId, userId)) {
			final Long currentTime = UtilMapper.getCurrentTime();
			List<Booking> lastBookings = repoBooking.findLastBooking(itemId, currentTime, PageRequest.of(0, 1));
			List<Booking> nextBookings = repoBooking.findNextBooking(itemId, currentTime, PageRequest.of(0, 1));
			Booking last = lastBookings.isEmpty() ? null : lastBookings.get(0);
			Booking next = nextBookings.isEmpty() ? null : nextBookings.get(0);
			return new Booking[] {last, next};
		}
		return new Booking[] {null, null};
	}

	public List<ItemFullDto> findItemsByOwner(final Long userId) {
		if (repoUser.hasId(userId)) {
			return repoItem.findByOwnerIdOrderByIdAsc(userId).stream().map(ItemMapper::toDto).toList();
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	public List<ItemFullDto> searchAvailableItemsByText(final Long userId, final String text) {
		if (!repoUser.hasId(userId)) {
			throw new NotFoundException(User.NOT_FOUND);
		}
		if (text == null || text.isBlank()) {
			return List.of();
		}
		return repoItem.searchAvailableItemsByText(text).stream().map(ItemMapper::toDto).toList();
	}

	@Transactional
	public CommentDto addComment(final Long userId, final Long itemId, final String text) {
		if (hasApprovedBooking(userId, itemId)) {
			final Item item = repoItem.findById(itemId).get();
			final User user = repoUser.findById(userId).get();
			final Comment comment = CommentMapper.toModel(user, item, text);
			final Comment ans = repoComment.save(comment);
			return CommentMapper.toDto(ans);
		}
		throw new MyBadRequestException(Comment.NO_COMMIT);
	}

	public boolean hasApprovedBooking(final Long userId, final Long itemId) {
		final Long time = UtilMapper.getCurrentTime();
		if (repoBooking.hasApprovedBooking(userId, itemId, time)) {
			return true;
		}
		return false;
	}

}
