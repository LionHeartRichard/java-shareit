package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT COUNT(*) > 0 FROM user_ WHERE email = :email", nativeQuery = true)
	boolean hasEmail(@Param("email") final String email);

	@Query(value = "SELECT COUNT(*) > 0 FROM user_ WHERE email = :email AND id <> :id", nativeQuery = true)
	boolean emailIsUsed(@Param("id") final Long userId, @Param("email") final String email);

	@Query(value = "SELECT COUNT(*) > 0 FROM user_ WHERE id = :id", nativeQuery = true)
	boolean hasId(@Param("id") final Long userId);
}
