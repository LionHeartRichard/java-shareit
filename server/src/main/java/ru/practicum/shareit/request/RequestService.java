package ru.practicum.shareit.request;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.common.dto.item.ItemDto;
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
				setItems(ans);
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
						setItems(ans);
						return ans;
					}).toList();
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	public RequestFullDto findRequestByUserId(Long userId, Long requestId) {
		if (userRepo.hasId(userId)) {
			Request request = requestRepo.findById(requestId)
					.orElseThrow(() -> new NotFoundException(Request.NOT_FOUND));
			return RequestMapper.toDto(request);
		}
		throw new NotFoundException(User.NOT_FOUND);
	}

	private void setItems(RequestFullDto dto) {
		List<ItemDto> items = itemRepo.findByRequestIdOrderByRequestIdDesc(dto.getId()).stream().map(ItemMapper::toDto)
				.toList();
		dto.setItems(items);
	}

}