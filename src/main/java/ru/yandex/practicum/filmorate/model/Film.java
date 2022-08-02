package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.util.Set;
import lombok.Builder;
import java.util.HashSet;
import java.time.LocalDate;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Builder
public class Film {
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @Size(max = MAX_DESCRIPTION_LENGTH,
            message = "Максимальная длина описания ограничена: " + MAX_DESCRIPTION_LENGTH + " символов!")
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @NotNull
    @Positive(message = "Длительность фильма должна быть больше нуля!")
    private int duration;

    private final Set<Long> likes = new HashSet<>();

    private final Set<Genre> genres = new HashSet<>();

    @NotNull
    private final RatingMPA ratingMPA;
}
