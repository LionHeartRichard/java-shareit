package ru.practicum.shareit.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@Transactional
@Rollback(true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({UserService.class})
public class UserServiceTest {

	private final UserService service;
	private final UserRepository repo;
	private User expectedUser;

	@BeforeEach
	void setUp() {
		expectedUser = User.builder().id(null).name("myTestName").email("testEmail@mail.com").build();
	}

}
