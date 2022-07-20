package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(value = "/films")
public class FilmController {

    @Autowired
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmInfo(@PathVariable("id") Long id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void setLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        filmService.setLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        filmService.unlike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        return filmService.getPopularFilms(count);
    }
}