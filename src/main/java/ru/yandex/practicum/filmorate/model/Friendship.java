package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = { "userId", "friendId" })
public class Friendship {

    @NotNull
    @NotEmpty
    private long userId;

    @NotNull
    @NotEmpty
    private long friendId;

    @NotNull
    @NotEmpty
    private long statusId;
}
