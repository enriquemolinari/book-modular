package spring.web;

import movies.api.MoviesSubSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import shows.api.ShowsSubSystem;
import users.api.UsersSubSystem;

import java.util.ServiceLoader;

@Configuration
@Profile("test")
public class AppTestConfiguration {
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

    private <T> T moduleFacadeLoader(Class<T> clazz) {
        //TODO: set test environment for modules
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Module facade class " + clazz.getName() + " could not be loaded"));
    }

}
