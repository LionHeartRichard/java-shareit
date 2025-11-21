package ru.practicum.shareit.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@ActiveProfiles("default")
@Transactional
@Rollback(true)
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {

	private final UserRepository repo;

//	@Test
//	void testSaveUser() {
//		User expected = User.builder().id(null).name("myTestName").email("testEmail@mail.com").build();
//		User actual = repo.save(expected);
//		assertNotNull(actual.getId());
//		assertEquals(expected.getName(), actual.getName());
//		assertEquals(expected.getEmail(), actual.getEmail());
//	}
}
