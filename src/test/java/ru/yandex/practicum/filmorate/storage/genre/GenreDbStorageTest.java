package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenreDbStorageTest {
    private final GenreDbStorage genreStorage;

    @Test
    @Order(1)
    void testGetAllGenres() {
        final Collection<Genre> genres = genreStorage.getAllGenres();
        final int expectedGenreSize = 6; // 6 жанров созданы в data.sql
        assertThat(genres)
                .isNotEmpty()
                .hasSize(expectedGenreSize);
    }

    @Test
    @Order(2)
    void testGetGenreById() {
        final Optional<Genre> optionalGenre = Optional.ofNullable(genreStorage.getGenreById(4));
        final long expectedGenreId = 4L;
        final String expectedGenreName = "Триллер";
        assertThat(optionalGenre)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", expectedGenreId)
                ).hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", expectedGenreName)
                );
    }
}