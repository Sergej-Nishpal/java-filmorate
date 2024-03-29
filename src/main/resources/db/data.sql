INSERT INTO GENRES (GENRE_NAME) SELECT 'Комедия'        WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME='Комедия');
INSERT INTO GENRES (GENRE_NAME) SELECT 'Драма'          WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME='Драма');
INSERT INTO GENRES (GENRE_NAME) SELECT 'Мультфильм'     WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME='Мультфильм');
INSERT INTO GENRES (GENRE_NAME) SELECT 'Триллер'        WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME='Триллер');
INSERT INTO GENRES (GENRE_NAME) SELECT 'Документальный' WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME='Документальный');
INSERT INTO GENRES (GENRE_NAME) SELECT 'Боевик'         WHERE NOT EXISTS (SELECT * FROM GENRES WHERE GENRE_NAME='Боевик');

INSERT INTO MPAS (MPA_NAME) SELECT 'G'     WHERE NOT EXISTS (SELECT * FROM MPAS WHERE MPA_NAME='G');
INSERT INTO MPAS (MPA_NAME) SELECT 'PG'    WHERE NOT EXISTS (SELECT * FROM MPAS WHERE MPA_NAME='PG');
INSERT INTO MPAS (MPA_NAME) SELECT 'PG-13' WHERE NOT EXISTS (SELECT * FROM MPAS WHERE MPA_NAME='PG-13');
INSERT INTO MPAS (MPA_NAME) SELECT 'R'     WHERE NOT EXISTS (SELECT * FROM MPAS WHERE MPA_NAME='R');
INSERT INTO MPAS (MPA_NAME) SELECT 'NC-17' WHERE NOT EXISTS (SELECT * FROM MPAS WHERE MPA_NAME='NC-17');

INSERT INTO STATUSES (STATUS_NAME) SELECT 'Waiting'   WHERE NOT EXISTS (SELECT * FROM STATUSES WHERE STATUS_NAME='Waiting');
INSERT INTO STATUSES (STATUS_NAME) SELECT 'Confirmed' WHERE NOT EXISTS (SELECT * FROM STATUSES WHERE STATUS_NAME='Confirmed');