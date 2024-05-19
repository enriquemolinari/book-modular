package spring.main;

import movies.api.MoviesSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import shows.api.ShowsSystem;

import java.util.ServiceLoader;

@Configuration
public class AppConfiguration {

    // this secret should not be here
//    private static final String SECRET = "nXXh3Xjr2T0ofFilg3kw8BwDEyHmS6OIe4cjWUm2Sm0=";

    @Bean
    @Profile("default")
    public MoviesSystem createMovies() {
        return ServiceLoader.load(MoviesSystem.class).findFirst().get();
    }

    @Bean
    @Profile("default")
    public ShowsSystem createShows() {
        return ServiceLoader.load(ShowsSystem.class).findFirst().get();
    }
}
