module events {
    exports events.api.data;
    exports events.api;
    exports events.api.data.users to shows, movies, users;
    exports events.api.data.movies to movies, shows;
    exports events.api.data.shows to shows, notifications, users;

    requires jakarta.persistence;
}