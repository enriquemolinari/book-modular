import movies.api.MoviesSubSystem;
import shows.api.ShowsSubSystem;
import users.api.UsersSubSystem;

module web {
    requires movies;
    requires shows;
    requires users;

    uses UsersSubSystem;
    uses ShowsSubSystem;
    uses MoviesSubSystem;

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