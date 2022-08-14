package ru.yandex.practicum.filmorate.storage.filmgenres;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmGenresDbStorageTest {
    private final FilmGenresDbStorage filmGenresStorage;

    @Test
    @Order(1)
    void testGetFilmGenres() {
        final long filmId = 2; // фильм с id = 2, жанров нет, не добавлены в data.sql
        final int expectedGenresSize = 0;
        Set<Genre> filmGenres = filmGenresStorage.getFilmGenres(filmId);
        final int actualGenresSize = filmGenres.size();
        assertThat(actualGenresSize)
                .isEqualTo(expectedGenresSize);
    }

    @Test
    @Order(2)
    void testAddFilmGenre() {
        final long filmId = 2;
        final Set<Long> genresSet = new HashSet<>(Set.of(3L, 4L, 6L)); // добавим жанры фильму с id = 2
        final int expectedGenresSize = 3;

        for (Long genreId : genresSet) {
            filmGenresStorage.addFilmGenre(filmId, genreId);
        }

        Set<Genre> filmGenres = filmGenresStorage.getFilmGenres(filmId);
        final int actualGenresSize = filmGenres.size();
        assertThat(actualGenresSize)
                .isEqualTo(expectedGenresSize);
    }

    @Test
    @Order(3)
    void testDeleteFilmGenres() {
        final long filmId = 2; // фильм с id = 2, удаляем добавленные ему в этом тесте жанры
        final int expectedGenresSize = 0;
        filmGenresStorage.deleteFilmGenres(filmId);
        Set<Genre> filmGenres = filmGenresStorage.getFilmGenres(filmId);
        final int actualGenresSize = filmGenres.size();
        assertThat(actualGenresSize)
                .isEqualTo(expectedGenresSize);
    }
}