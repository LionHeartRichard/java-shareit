package ru.practicum.shareit.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase
@Import(UserRepository.class)
public class UserRepositoryAppTest {

	@Autowired
	public UserRepositoryAppTest(UserRepository rep) {
		this.rep = rep;
		postfix = 0;
	}

	private final UserRepository rep;

	private User user;
	private User ans;
	private static int postfix;

	@BeforeEach
	void setUp() {
		++postfix;
		user = User.builder().id(null).name("name-" + postfix).email(postfix + "myemail@gmail.com").build();
		ans = rep.save(user);

		assertTrue(ans.getId() != null);
		assertEquals(ans.getName(), user.getName());
		assertEquals(ans.getEmail(), user.getEmail());
	}

	@Test
	void findAllTest() {
		List<User> actual = rep.findAll();
		assertTrue(!actual.isEmpty());
	}

	@Test
	void findByIdTest() {
		User actual = rep.findById(1L).get();
		assertTrue(actual != null);
	}

}
