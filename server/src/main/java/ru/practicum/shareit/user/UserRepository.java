package ru.practicum.shareit.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
	boolean hasEmail(@Param("email") String email);

	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id <> :id")
	boolean emailIsUsed(@Param("id") Long id, @Param("email") String email);

	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.id = :id")
	boolean hasId(@Param("id") Long id);

	@Query("SELECT u FROM User u WHERE u.email = :email")
	Optional<User> findByEmail(@Param("email") String email);
}
