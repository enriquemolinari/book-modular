module publisher {
    exports publisher.api;
    exports publisher.api.data.users to shows, movies, users, notifications;
    exports publisher.api.data.movies to movies, shows;
    exports publisher.api.data.shows to shows, notifications, users;

    requires jakarta.persistence;
}