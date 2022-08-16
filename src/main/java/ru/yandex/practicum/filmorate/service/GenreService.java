package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
public class GenreService {
    private static final String INCORRECT_PARAMETER = "Некорректный параметр %s = %d";
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(Long genreId) {
        if (genreId <= 0) {
            log.error("Передан некорректный id = {}.", genreId);
            throw new GenreNotFoundException(String.format(INCORRECT_PARAMETER, "id", genreId));
        }
        return genreStorage.getGenreById(genreId);
    }
}
