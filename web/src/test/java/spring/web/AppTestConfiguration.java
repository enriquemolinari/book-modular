package spring.web;

import movies.api.MoviesSubSystem;
import movies.builder.MoviesSubSystemBuilder;
import movies.listeners.NewUserListenerOnMovies;
import notifications.api.NotificationsSubSystem;
import notifications.builder.NotificationsSubSystemBuilder;
import notifications.listeners.NewTicketsListenerOnNotifications;
import notifications.listeners.NewUserListenerOnNotifications;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import shows.api.ShowsSubSystem;
import shows.builder.ShowsSubSystemBuilder;
import shows.listeners.NewMovieListenerOnShows;
import shows.listeners.NewUserListenerOnShows;
import users.api.UsersSubSystem;
import users.builder.UsersSubSystemBuilder;
import users.listeners.NewTicketsListenerOnUsers;

@Configuration
@Profile("test")
public class AppTestConfiguration {
    @Bean
    public MoviesSubSystem createMovies() {
        return new MoviesSubSystemBuilder()
                .testEnv()
                .addListener(new NewMovieListenerOnShows())
                .build();
    }

    @Bean
    public NotificationsSubSystem createNotifications() {
        return new NotificationsSubSystemBuilder()
                .testEnv()
                .build();
    }

    @Bean
    public ShowsSubSystem createShows() {
        return new ShowsSubSystemBuilder()
                .testEnv()
                .addListener(new NewTicketsListenerOnUsers())
                .addListener(new NewTicketsListenerOnNotifications())
                .build();
    }

    @Bean
    public UsersSubSystem createUsers() {
        return new UsersSubSystemBuilder()
                .testEnv()
                .addListener(new NewUserListenerOnShows())
                .addListener(new NewUserListenerOnMovies())
                .addListener(new NewUserListenerOnNotifications())
                .build();
    }
}
