package ru.practicum.shareit.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findUserById(Long id);

	boolean deleteUserById(Long id);

	boolean hasUserById(Long id);

	boolean hasUserByEmail(User user);

	public boolean isUsedEmail(User user);
}
