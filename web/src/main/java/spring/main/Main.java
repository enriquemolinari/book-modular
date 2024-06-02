package spring.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.AbstractEnvironment;

import static common.constants.Environment.ENVIRONMENT_DEFAULT;
import static common.constants.Environment.ENVIRONMENT_PROPERTY_NAME;


@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan(basePackages = {"spring.*"})
public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, ENVIRONMENT_DEFAULT);
        System.setProperty(ENVIRONMENT_PROPERTY_NAME, ENVIRONMENT_DEFAULT);
        SpringApplication.run(Main.class, args);
    }
}
