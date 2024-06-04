package spring.web;

import movies.api.MoviesSubSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import shows.api.ShowsSubSystem;
import users.api.UsersSubSystem;

import java.util.ServiceLoader;

import static common.constants.Environment.ENVIRONMENT_PROPERTY_NAME;
import static common.constants.Environment.ENVIRONMENT_TEST;

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
        //TODO: revisar esto...
        System.setProperty(ENVIRONMENT_PROPERTY_NAME, ENVIRONMENT_TEST);
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Module facade class " + clazz.getName() + " could not be loaded"));
    }

}
