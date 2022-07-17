package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;

    @NotNull
    @NotEmpty
    @Email(message = "Указан некорректный email!")
    private String email;

    @NotNull
    @NotEmpty
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не должна быть в будущем!")
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();
}
