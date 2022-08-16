INSERT INTO GENRES (GENRE_NAME) SELECT 'Комедия'        WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME = 'Комедия');
INSERT INTO GENRES (GENRE_NAME) SELECT 'Драма'          WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME = 'Драма');
INSERT INTO GENRES (GENRE_NAME) SELECT 'Мультфильм'     WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME = 'Мультфильм');
INSERT INTO GENRES (GENRE_NAME) SELECT 'Триллер'        WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME = 'Триллер');
INSERT INTO GENRES (GENRE_NAME) SELECT 'Документальный' WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME = 'Документальный');
INSERT INTO GENRES (GENRE_NAME) SELECT 'Боевик'         WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME = 'Боевик');

INSERT INTO MPAS (MPA_NAME) SELECT 'G'     WHERE NOT EXISTS (SELECT * FROM MPAS WHERE MPA_NAME = 'G');
INSERT INTO MPAS (MPA_NAME) SELECT 'PG'    WHERE NOT EXISTS (SELECT * FROM MPAS WHERE MPA_NAME = 'PG');
INSERT INTO MPAS (MPA_NAME) SELECT 'PG-13' WHERE NOT EXISTS (SELECT * FROM MPAS WHERE MPA_NAME = 'PG-13');
INSERT INTO MPAS (MPA_NAME) SELECT 'R'     WHERE NOT EXISTS (SELECT * FROM MPAS WHERE MPA_NAME = 'R');
INSERT INTO MPAS (MPA_NAME) SELECT 'NC-17' WHERE NOT EXISTS (SELECT * FROM MPAS WHERE MPA_NAME = 'NC-17');

INSERT INTO STATUSES (STATUS_NAME) SELECT 'Waiting'   WHERE NOT EXISTS (SELECT * FROM STATUSES WHERE STATUS_NAME = 'Waiting');
INSERT INTO STATUSES (STATUS_NAME) SELECT 'Confirmed' WHERE NOT EXISTS (SELECT * FROM STATUSES WHERE STATUS_NAME = 'Confirmed');

INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) SELECT 'Властелин колец', 'Описание фильма Властелин колец', '2003-01-01', 100, 2          WHERE NOT EXISTS (SELECT * FROM films WHERE FILM_ID = 1);
INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) SELECT 'Криминальное чтиво', 'Описание фильма Криминальное чтиво', '1994-01-01', 120, 4    WHERE NOT EXISTS (SELECT * FROM films WHERE FILM_ID = 2);
INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) SELECT 'Начало', 'Описание фильма Начало', '2010-01-01', 130, 4                            WHERE NOT EXISTS (SELECT * FROM films WHERE FILM_ID = 3);
INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) SELECT 'Эта прекрасная жизнь', 'Описание фильма Эта прекрасная жизнь', '1946-01-01', 90, 4 WHERE NOT EXISTS (SELECT * FROM films WHERE FILM_ID = 4);

INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) SELECT 'ivan.ivanov@yandex.ru', 'IvanI', 'Ivan Ivanov', '1954-10-07'      WHERE NOT EXISTS (SELECT * FROM users WHERE USER_ID = 1);
INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) SELECT 'petr.petrov@yandex.ru', 'PetrP', 'Petr Petrov', '1951-08-04'      WHERE NOT EXISTS (SELECT * FROM users WHERE USER_ID = 2);
INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) SELECT 'sidor.sidorov@yandex.ru', 'SidorS', 'Sidor Sidorov', '1990-01-01' WHERE NOT EXISTS (SELECT * FROM users WHERE USER_ID = 3);

INSERT INTO FILM_GENRES VALUES (1, 2);
INSERT INTO FILM_GENRES VALUES (1, 4);
INSERT INTO FILM_GENRES VALUES (3, 3);
INSERT INTO FILM_GENRES VALUES (4, 4);
INSERT INTO FILM_GENRES VALUES (4, 5);

INSERT INTO FRIENDSHIPS VALUES (1, 2, 1);
INSERT INTO FRIENDSHIPS VALUES (1, 3, 2);
INSERT INTO FRIENDSHIPS VALUES (2, 3, 2);

INSERT INTO FILM_LIKES VALUES (1, 1);
INSERT INTO FILM_LIKES VALUES (1, 3);
INSERT INTO FILM_LIKES VALUES (3, 1);
INSERT INTO FILM_LIKES VALUES (3, 2);
INSERT INTO FILM_LIKES VALUES (3, 3);
INSERT INTO FILM_LIKES VALUES (2, 3);