package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {

	private static final String USER_EMAIL           = "sergej.nishpal@yandex.ru";
	private static final String USER_LOGIN           = "Sergej_N";
	private static final String USER_NAME            = "Sergej Nishpal";
	private static final LocalDate USER_BIRTHDAY     = LocalDate.of(1975, 2, 14);
	private static final String NEW_USER_NAME        = "Sergej Vladimirovich Nishpal";
	private static final String FILM_NAME            = "Интерстеллар";
	private static final String FILM_DESCRIPTION     = "Фильм о путешествиях uheggs исследователей, которые...";
	private static final String NEW_FILM_DESCRIPTION = "Фильм о путешествиях группы исследователей, которые...";
	private static final LocalDate FILM_RELEASE_DATE = LocalDate.of(2014, 11, 18);
	private static final int FILM_DURATION           = 169;
	private static final String INTERSTELLAR_MPA     = "PG-13";
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;

	@Test
	@Order(1)
	void testCreateUser() {
		final User newUser = User.builder()
				.email(USER_EMAIL)
				.login(USER_LOGIN)
				.name(USER_NAME)
				.birthday(USER_BIRTHDAY)
				.build();

		final Optional<User> optionalUser = Optional.ofNullable(userStorage.createUser(newUser));
		assertThat(optionalUser)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("name", USER_NAME)
				);

	}

	@Test
	@Order(2)
	void testFindAllUsers() {
		final Collection<User> users = userStorage.findAllUsers();
		assertThat(users).isNotEmpty();
	}

	@Test
	@Order(3)
	void testGetUserById() {
		final Optional<User> optionalUser = Optional.ofNullable(userStorage.getUserById(1L));
		assertThat(optionalUser)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);
	}

	@Test
	@Order(4)
	void testUpdateUser() {
		User userForUpdate = userStorage.getUserById(1L);
		assertThat(userForUpdate)
				.hasFieldOrPropertyWithValue("name", USER_NAME);

		userForUpdate.setName(NEW_USER_NAME);
		final User updatedUser = userStorage.updateUser(userForUpdate);
		assertThat(updatedUser)
				.hasFieldOrPropertyWithValue("name", NEW_USER_NAME);
	}

	@Test
	@Order(5)
	void testCreateFilm() {
		final Film newFilm = Film.builder()
				.name(FILM_NAME)
				.description(FILM_DESCRIPTION)
				.releaseDate(FILM_RELEASE_DATE)
				.duration(FILM_DURATION)
				.mpa(new Mpa(3L, "PG-13")) // TODO Тут так?
				.build();

		final Optional<Film> optionalFilm = Optional.ofNullable(filmStorage.createFilm(newFilm));
		assertThat(optionalFilm)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("description", FILM_DESCRIPTION)
				);

	}

	@Test
	@Order(6)
	void testFindAllFilms() {
		final Collection<Film> films = filmStorage.getAllFilms();
		assertThat(films).isNotEmpty();
	}

	@Test
	@Order(7)
	void testGetFilmById() {
		final Optional<Film> optionalFilm = Optional.ofNullable(filmStorage.getFilmById(1L));
		assertThat(optionalFilm)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
				);
	}

	@Test
	@Order(8)
	void testUpdateFilm() {
		Film filmForUpdate = filmStorage.getFilmById(1L);
		assertThat(filmForUpdate)
				.hasFieldOrPropertyWithValue("description", FILM_DESCRIPTION);

		filmForUpdate.setDescription(NEW_FILM_DESCRIPTION);
		final Film updatedFilm = filmStorage.updateFilm(filmForUpdate);
		assertThat(updatedFilm)
				.hasFieldOrPropertyWithValue("description", NEW_FILM_DESCRIPTION);
	}
}