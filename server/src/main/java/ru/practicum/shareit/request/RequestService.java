package ru.practicum.shareit.request;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.exception.MyBadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RequestService {

	RequestRepository requestRepo;
	UserRepository userRepo;
	ItemRepository itemRepo;

	@Transactional
	public Request create(Long userId, Request request) {
		User requester = userRepo.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		request.setRequester(requester);
		return requestRepo.save(request);
	}

	public List<Request> getAllRequestsById(Long userId) {
		if (userRepo.hasId(userId)) {
			return requestRepo.findAllByRequesterId(userId, Sort.by(Sort.Direction.DESC, "created"));
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	public List<Request> findAll(Long userId, Integer from, Integer size) {
		if (from < 0 || size < 0) {
			throw new MyBadRequestException("Arguments cannot be negative!!!");
		}
		if (!userRepo.hasId(userId)) {
			throw new NotFoundException(User.NOT_FOUND);
		}
		return requestRepo.findAll(PageRequest.of((from / size), size, Sort.by(Sort.Direction.DESC, "created")))
				.stream().toList();

	}

	public Request findById(Long userId, Long requestId) {
		if (userRepo.hasId(userId)) {
			Request request = requestRepo.findById(requestId)
					.orElseThrow(() -> new NotFoundException(Request.NOT_FOUND));
			return request;
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	public List<Item> getRequestItems(Long requestId) {
		return itemRepo.findByRequestIdOrderByRequestIdDesc(requestId);
	}
}