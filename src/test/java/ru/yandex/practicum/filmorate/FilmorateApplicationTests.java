package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final JdbcTemplate jdbcTemplate;
	private final UserDbStorage userStorage;

	@Test
	void deleteUsers() {
		String deleteUsersQuery = "DELETE FROM USERS WHERE USER_ID > 0";
		jdbcTemplate.update(deleteUsersQuery);

		String restartUsersFromOneQuery = "ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1";
		jdbcTemplate.update(restartUsersFromOneQuery);
	}

	@Test
	void deleteFilms() {
		String deleteFilmsQuery = "DELETE FROM FILMS WHERE FILM_ID > 0";
		jdbcTemplate.update(deleteFilmsQuery);

		String restartFilmsFromOneQuery = "ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1";
		jdbcTemplate.update(restartFilmsFromOneQuery);
	}

	@Test
	void testCreateUser() {
		User newUser = User.builder()
				.email("sergej.nishpal@yandex.ru")
				.login("Sergej_N")
				.name("Sergej Nishpal")
				.birthday(LocalDate.of(1975, 2, 14))
				.build();

		Optional<User> userOptional = Optional.ofNullable(userStorage.createUser(newUser));

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("name", "Sergej Nishpal")
				);

	}

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
