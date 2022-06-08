package no.nav.tpts;

import lombok.extern.slf4j.Slf4j;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJwtTokenValidation(ignore = { "org.springdoc", "org.springframework" })
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("Starting app tpts-arena");
        SpringApplication.run(Application.class, args);
    }

}
