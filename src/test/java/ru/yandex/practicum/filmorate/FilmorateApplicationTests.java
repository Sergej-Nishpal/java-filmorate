package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FilmorateApplicationTests {

    @Autowired
    UserController userController;
    @Autowired
    FilmController filmController;

    @Test
    void contextLoads() {
        assertThat(userController).isNotNull();
        assertThat(filmController).isNotNull();
    }
}