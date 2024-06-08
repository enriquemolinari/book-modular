package spring.web;

import movies.api.MoviesSubSystem;
import movies.builder.MoviesSubSystemBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import shows.api.ShowsSubSystem;
import shows.builder.ShowsSubSystemBuilder;
import users.api.UsersSubSystem;
import users.builder.UsersSubSystemBuilder;

@Configuration
@Profile("test")
public class AppTestConfiguration {
    @Bean
    public MoviesSubSystem createMovies() {
        return new MoviesSubSystemBuilder()
                .testEnv()
                .build();
    }

    @Bean
    public ShowsSubSystem createShows() {
        return new ShowsSubSystemBuilder()
                .testEnv()
                .build();
    }

    @Bean
    public UsersSubSystem createUsers() {
        return new UsersSubSystemBuilder().testEnv().build();
    }
}
