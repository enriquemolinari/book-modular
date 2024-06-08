module common {
    exports common.constants;

    exports common.email to movies, users;
    exports common.strings to shows, movies, users;
    exports common.date to shows, movies, users;
}