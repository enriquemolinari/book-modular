module events {
    exports events.api;
    exports events.api.data.users to shows, movies, users, notifications;
    exports events.api.data.movies to movies, shows;
    exports events.api.data.shows to shows, notifications, users;

    requires jakarta.persistence;
}