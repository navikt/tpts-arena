package no.nav.tpts.sts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StsConfig {

    @Bean
    StsService stsService(
            @Value("${app.arena.soap.sts.url}")
                    String url,
            @Value("${app.arena.soap.sts.username}")
                    String username,
            @Value("${app.arena.soap.sts.password}")
                    String password
    ) {
        return new StsService(url, username, password);
    }

}
