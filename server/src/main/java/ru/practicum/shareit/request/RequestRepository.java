package ru.practicum.shareit.request;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
	List<Request> findAllByRequesterId(Long userId, Sort sortOrder);
}
