package ru.practicum.shareit.request;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.dto.item.ItemFullDto;
import ru.practicum.shareit.common.dto.request.RequestDto;
import ru.practicum.shareit.common.dto.request.RequestFullDto;
import ru.practicum.shareit.common.exception.MyBadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
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
	public RequestFullDto createRequest(Long userId, RequestDto dto) {
		User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException(User.NOT_FOUND));
		Request ans = RequestMapper.toModel(dto);
		ans.setRequester(user);
		return RequestMapper.toDto(requestRepo.save(ans));
	}

	public List<RequestFullDto> findAllRequestsByUserId(Long userId) {
		if (userRepo.hasId(userId)) {
			return requestRepo.findAllByRequesterId(userId, Sort.by(Sort.Direction.DESC, "created")).stream().map(v -> {
				RequestFullDto ans = RequestMapper.toDto(v);
				ans.setItems(findItemsByRequester(userId));
				return ans;
			}).toList();
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	public List<RequestFullDto> findAll(Long userId, Integer from, Integer size) {
		if (from < 0 || size < 0) {
			throw new MyBadRequestException("Arguments cannot be negative!!!");
		}
		if (userRepo.hasId(userId)) {
			return requestRepo.findAll(PageRequest.of((from / size), size, Sort.by(Sort.Direction.DESC, "created")))
					.stream().map(v -> {
						RequestFullDto ans = RequestMapper.toDto(v);
						ans.setItems(findItemsByRequester(userId));
						return ans;
					}).toList();
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	public RequestFullDto findRequestByUserId(Long userId, Long requestId) {
		if (userRepo.hasId(userId)) {
			Request request = requestRepo.findById(requestId)
					.orElseThrow(() -> new NotFoundException(Request.NOT_FOUND));
			List<ItemFullDto> itemsDto = itemRepo.findByRequestId(requestId).stream().map(ItemMapper::toDto).toList();
			return RequestMapper.toDto(request, itemsDto);
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	private List<ItemFullDto> findItemsByRequester(final Long userId) {
		return requestRepo.findByRequester(userId).stream().map(ItemMapper::toDto).toList();
	}

}