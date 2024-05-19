module web {
    requires movies;
    requires shows;

    uses shows.api.ShowsSystem;
    uses movies.api.MoviesSystem;

    //TODO: ser mas fino con el opens
    opens spring.main;
    opens spring.web;

    requires spring.core;
    requires spring.boot;
    requires spring.web;
    requires spring.webmvc;
    requires spring.context;
    requires spring.beans;
    requires spring.boot.autoconfigure;
}