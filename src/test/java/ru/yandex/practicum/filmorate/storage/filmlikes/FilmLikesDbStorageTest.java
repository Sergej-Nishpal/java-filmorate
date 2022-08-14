package ru.yandex.practicum.filmorate.storage.filmlikes;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmLikesDbStorageTest {
    private final FilmLikesDbStorage filmLikesStorage;

    @Test
    @Order(1)
    void testAddLike() {
        final long filmId = 1; // у фильма с id = 1 есть лайки от пользователей с id = 1 и id = 3
        final int expectedLikesSize = 2;
        final int actualLikesCount = filmLikesStorage.getLikesCount(filmId);
        assertThat(actualLikesCount)
                .isEqualTo(expectedLikesSize);

        final long userId = 2;
        filmLikesStorage.addLike(filmId, userId);
        final int expectedLikesSizeAfterAddingLike = 3;
        final int actualLikesCountAfterAddingLike = filmLikesStorage.getLikesCount(filmId);
        assertThat(actualLikesCountAfterAddingLike)
                .isEqualTo(expectedLikesSizeAfterAddingLike);
    }

    @Test
    @Order(2)
    void testDeleteLike() {
        final long filmId = 1; // тот же фильм
        final int expectedLikesSize = 3; // лайков сейчас 3
        final int actualLikesCount = filmLikesStorage.getLikesCount(filmId);
        assertThat(actualLikesCount)
                .isEqualTo(expectedLikesSize);

        final long userId = 1; // убираем лайк пользователя с id = 1
        filmLikesStorage.deleteLike(filmId, userId);
        final int expectedLikesSizeAfterDeleteLike = 2;
        final int actualLikesCountAfterDeleteLike = filmLikesStorage.getLikesCount(filmId);
        assertThat(actualLikesCountAfterDeleteLike)
                .isEqualTo(expectedLikesSizeAfterDeleteLike);
    }

    @Test
    @Order(3)
    void testGetPopularFilms() {
        final int expectedCountForTopListOfFilms = 3;
        final String expectedFirstPlaceFilmName = "Начало";
        final String expectedSecondPlaceFilmName = "Властелин колец";
        final String expectedThirdPlaceFilmName = "Криминальное чтиво";

        final List<Film> popularFilmList = new ArrayList<>(filmLikesStorage.getPopularFilms(expectedCountForTopListOfFilms));
        final int actualCountForTopListOfFilms = popularFilmList.size();
        final String actualFirstPlaceFilmName = popularFilmList.get(0).getName();
        final String actualSecondPlaceFilmName = popularFilmList.get(1).getName();
        final String actualThirdPlaceFilmName = popularFilmList.get(2).getName();

        assertThat(actualCountForTopListOfFilms)
                .isEqualTo(expectedCountForTopListOfFilms);
        assertThat(actualFirstPlaceFilmName)
                .isEqualTo(expectedFirstPlaceFilmName);
        assertThat(actualSecondPlaceFilmName)
                .isEqualTo(expectedSecondPlaceFilmName);
        assertThat(actualThirdPlaceFilmName)
                .isEqualTo(expectedThirdPlaceFilmName);
    }
}