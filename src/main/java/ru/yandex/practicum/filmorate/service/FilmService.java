package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private static final String INCORRECT_PARAMETER = "Некорректный параметр %s = %d.";

    @Autowired
    private FilmStorage filmStorage;

    public void setLike(long filmId, long userId) {
        if (filmId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "id", filmId));
        }

        if (userId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "userId", userId));
        }

        filmStorage.getFilmById(filmId)
                .getLikes()
                .add(userId);
    }

    public void unlike(long filmId, long userId) {
        if (filmId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "id", filmId));
        }

        if (userId <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "userId", userId));
        }

        filmStorage.getFilmById(filmId)
                .getLikes()
                .remove(userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new IncorrectParameterException(String.format(INCORRECT_PARAMETER, "count", count));
        }

        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
