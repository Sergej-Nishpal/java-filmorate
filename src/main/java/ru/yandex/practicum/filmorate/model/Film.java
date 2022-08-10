package ru.yandex.practicum.filmorate.model;

import lombok.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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
    private Mpa mpa;

    public Map<String, Object> toMap() {
        Map<String, Object> filmMap = new HashMap<>();
        filmMap.put("film_name", name);
        filmMap.put("description", description);
        filmMap.put("release_date", releaseDate);
        filmMap.put("duration", duration);
        filmMap.put("mpa_id", mpa.getId());
        filmMap.put("mpa_name", mpa.getMpaName());
        return filmMap;
    }
}
