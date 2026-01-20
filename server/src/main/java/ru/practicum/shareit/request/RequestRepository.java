package ru.practicum.shareit.request;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.practicum.shareit.item.Item;

public interface RequestRepository extends JpaRepository<Request, Long> {

	List<Request> findAllByRequesterId(final Long userId, Sort sortOrder);

	@Query("FROM Item i WHERE i.request.requester.id = :userId")
	List<Item> findByRequester(@Param("userId") final Long userId);
}
