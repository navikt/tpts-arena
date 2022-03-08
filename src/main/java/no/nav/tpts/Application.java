package no.nav.tpts;

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJwtTokenValidation(ignore = { "org.springdoc", "org.springframework" })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
