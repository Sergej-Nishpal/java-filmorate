package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmDbStorageTest {
    private static final String FILM_NAME = "Интерстеллар";
    private static final String INCORRECT_DESCRIPTION = "Фильм о путешествиях uheggs исследователей, которые...";
    private static final String CORRECT_DESCRIPTION = "Фильм о путешествиях группы исследователей, которые...";
    private static final LocalDate FILM_RELEASE_DATE = LocalDate.of(2014, 11, 18);
    private static final int FILM_DURATION = 169;
    private static final long PG_13_ID = 3L;
    private static final String PG_13_NAME = "PG-13";

    private final FilmDbStorage filmStorage;

    @Test
    @Order(1)
    void testCreateNewFilm() {
        final Film newFilm = Film.builder()
                .name(FILM_NAME)
                .description(INCORRECT_DESCRIPTION)
                .releaseDate(FILM_RELEASE_DATE)
                .duration(FILM_DURATION)
                .genres(new HashSet<>())
                .mpa(new Mpa(PG_13_ID, PG_13_NAME))
                .build();

        final Optional<Film> optionalFilm = Optional.ofNullable(filmStorage.createFilm(newFilm));
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("description", INCORRECT_DESCRIPTION)
                );
    }

    @Test
    @Order(2)
    void testGetAllFilms() {
        final Collection<Film> films = filmStorage.getAllFilms();
        final int expectedFilmsQty = 5; // 4 фильма из data.sql + 1 фильм создаём в ходе теста
        assertThat(films)
                .isNotEmpty()
                .hasSize(expectedFilmsQty);
    }

    @Test
    @Order(3)
    void testGetFilmById() {
        final Optional<Film> optionalFilm = Optional.ofNullable(filmStorage.getFilmById(3));
        final long expectedFilmId = 3L;
        final String expectedFilmName = "Начало";
        assertThat(optionalFilm)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", expectedFilmId)
                ).hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", expectedFilmName)
                );
    }

    @Test
    @Order(4)
    void testUpdateFilm() {
        Film filmForUpdate = filmStorage.getFilmById(5); // добавленный в тесте фильм
        assertThat(filmForUpdate)
                .hasFieldOrPropertyWithValue("description", INCORRECT_DESCRIPTION);

        filmForUpdate.setDescription(CORRECT_DESCRIPTION);
        final Film updatedFilm = filmStorage.updateFilm(filmForUpdate);
        assertThat(updatedFilm)
                .hasFieldOrPropertyWithValue("description", CORRECT_DESCRIPTION);
    }

    @Test
    @Order(5)
    void testAddLike() {
        final long filmId = 1; // у фильма с id = 1 есть лайки от пользователей с id = 1 и id = 3
        final int expectedLikesSize = 2;
        final int actualLikesCount = filmStorage.getLikesCount(filmId);
        AssertionsForClassTypes.assertThat(actualLikesCount)
                .isEqualTo(expectedLikesSize);

        final long userId = 2;
        filmStorage.addLike(filmId, userId);
        final int expectedLikesSizeAfterAddingLike = 3;
        final int actualLikesCountAfterAddingLike = filmStorage.getLikesCount(filmId);
        AssertionsForClassTypes.assertThat(actualLikesCountAfterAddingLike)
                .isEqualTo(expectedLikesSizeAfterAddingLike);
    }

    @Test
    @Order(6)
    void testDeleteLike() {
        final long filmId = 1; // тот же фильм
        final int expectedLikesSize = 3; // лайков сейчас 3
        final int actualLikesCount = filmStorage.getLikesCount(filmId);
        AssertionsForClassTypes.assertThat(actualLikesCount)
                .isEqualTo(expectedLikesSize);

        final long userId = 1; // убираем лайк пользователя с id = 1
        filmStorage.deleteLike(filmId, userId);
        final int expectedLikesSizeAfterDeleteLike = 2;
        final int actualLikesCountAfterDeleteLike = filmStorage.getLikesCount(filmId);
        AssertionsForClassTypes.assertThat(actualLikesCountAfterDeleteLike)
                .isEqualTo(expectedLikesSizeAfterDeleteLike);
    }

    @Test
    @Order(7)
    void testGetPopularFilms() {
        final int expectedCountForTopListOfFilms = 3;
        final String expectedFirstPlaceFilmName = "Начало";
        final String expectedSecondPlaceFilmName = "Властелин колец";
        final String expectedThirdPlaceFilmName = "Криминальное чтиво";

        final List<Film> popularFilmList = new ArrayList<>(filmStorage.getPopularFilms(expectedCountForTopListOfFilms));
        final int actualCountForTopListOfFilms = popularFilmList.size();
        final String actualFirstPlaceFilmName = popularFilmList.get(0).getName();
        final String actualSecondPlaceFilmName = popularFilmList.get(1).getName();
        final String actualThirdPlaceFilmName = popularFilmList.get(2).getName();

        AssertionsForClassTypes.assertThat(actualCountForTopListOfFilms)
                .isEqualTo(expectedCountForTopListOfFilms);
        AssertionsForClassTypes.assertThat(actualFirstPlaceFilmName)
                .isEqualTo(expectedFirstPlaceFilmName);
        AssertionsForClassTypes.assertThat(actualSecondPlaceFilmName)
                .isEqualTo(expectedSecondPlaceFilmName);
        AssertionsForClassTypes.assertThat(actualThirdPlaceFilmName)
                .isEqualTo(expectedThirdPlaceFilmName);
    }
}