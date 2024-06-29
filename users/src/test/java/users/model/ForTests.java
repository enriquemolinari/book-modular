package users.model;

import jakarta.persistence.EntityManager;
import publisher.api.Event;
import publisher.api.EventListener;
import publisher.api.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForTests {

    static final String SUPER_MOVIE_PLOT = "a super movie that shows the life of ...";
    static final String SUPER_MOVIE_NAME = "a super movie";
    static final String OTHER_SUPER_MOVIE_NAME = "another super movie";


    Token doNothingToken() {
        return new Token() {
            @Override
            public Long verifyAndGetUserIdFrom(String token) {
                return 0L;
            }

            @Override
            public String tokenFrom(Map<String, Object> payload) {
                return "aToken";
            }
        };
    }

    User createUserCharly() {
        return new User("Carlos", "Edgun", "cedgun@mysite.com",
                "cedgun", "afbcdefghigg", "afbcdefghigg");
    }

    User createUserJoseph() {
        return new User("Joseph", "Valdun", "jvaldun@wabla.com",
                "jvaldun", "tabcd1234igg", "tabcd1234igg");
    }

    User createUserNicolas() {
        return new User(
                "Nicolas", "Molinari", "nmolinari@yesmy.com",
                "nmolinari", "oneplayminebrawl", "oneplayminebrawl");
    }

    Publisher doNothingEventPubliser() {
        return new Publisher() {
            private final List<EventListener> subscribers = new ArrayList<>();

            @Override
            public <E extends Event> void subscribe(EventListener<E> listener) {
                this.subscribers.add(listener);
            }

            @Override
            public <E extends Event> void notify(EntityManager em, E event) {

            }
        };
    }
}