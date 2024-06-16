module web {
    requires movies;
    requires shows;
    requires users;
    requires notifications;

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