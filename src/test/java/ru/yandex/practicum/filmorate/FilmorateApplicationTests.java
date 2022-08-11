package ru.yandex.practicum.filmorate;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;

	@Test
	void testFindUserById() {

		Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);
	}
}
