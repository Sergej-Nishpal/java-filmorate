package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmorateApplicationTests {

    @Autowired
    UserController userController;
    @Autowired
    FilmController filmController;
    @Autowired
    FilmController mpaController;
    @Autowired
    FilmController genreController;

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(userController).isNotNull();
        assertThat(filmController).isNotNull();
        assertThat(mpaController).isNotNull();
        assertThat(genreController).isNotNull();
    }
}