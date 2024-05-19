package spring.main;

import movies.api.MoviesSubSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import shows.api.ShowsSubSystem;
import users.api.UsersSubSystem;

import java.util.ServiceLoader;

@Configuration
public class AppConfiguration {
    @Bean
    @Profile("default")
    public MoviesSubSystem createMovies() {
        return moduleFacadeLoader(MoviesSubSystem.class);
    }

    @Bean
    @Profile("default")
    public ShowsSubSystem createShows() {
        return moduleFacadeLoader(ShowsSubSystem.class);
    }

    @Bean
    @Profile("default")
    public UsersSubSystem createUsers() {
        return moduleFacadeLoader(UsersSubSystem.class);
    }

    private <T> T moduleFacadeLoader(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Modules facade services could not be loaded"));
    }
}
