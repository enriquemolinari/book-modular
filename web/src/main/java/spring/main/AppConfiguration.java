package spring.main;

import movies.api.MoviesSubSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import shows.api.ShowsSubSystem;
import users.api.UsersSubSystem;

import static spring.main.ModuleFacadeLoader.moduleFacadeLoader;

@Configuration
@Profile(AppConfiguration.ENVIRONMENT)
public class AppConfiguration {

    public static final String ENVIRONMENT = "default";

    @Bean
    public MoviesSubSystem createMovies() {
        return moduleFacadeLoader(MoviesSubSystem.class, ENVIRONMENT);
    }

    @Bean
    public ShowsSubSystem createShows() {
        return moduleFacadeLoader(ShowsSubSystem.class, ENVIRONMENT);
    }

    @Bean
    public UsersSubSystem createUsers() {
        return moduleFacadeLoader(UsersSubSystem.class, ENVIRONMENT);
    }
}
