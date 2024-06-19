module common {
    exports common.constants to shows, movies, users, notifications;
    exports common.email to movies, users;
    exports common.strings to shows, movies, users;
    exports common.date to shows, movies, users;
    exports common.db to movies, shows, users, notifications;

    requires jakarta.persistence;
}