package spring.main;

import common.constants.Environment;
import movies.api.MoviesSubSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import shows.api.ShowsSubSystem;
import users.api.UsersSubSystem;

import static spring.main.ModuleFacadeLoader.moduleFacadeLoader;

@Configuration
@Profile(Environment.ENVIRONMENT_DEFAULT)
public class AppConfiguration {

    @Bean
    public MoviesSubSystem createMovies() {
        return moduleFacadeLoader(MoviesSubSystem.class);
    }

    @Bean
    public ShowsSubSystem createShows() {
        return moduleFacadeLoader(ShowsSubSystem.class);
    }

    @Bean
    public UsersSubSystem createUsers() {
        return moduleFacadeLoader(UsersSubSystem.class);
    }
}
