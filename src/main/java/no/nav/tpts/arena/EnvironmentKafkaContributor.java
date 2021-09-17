package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * (Temporarily) used for testing. Will show any {@code KAFKA*} system and environment properties set on the {@code /actuator/info} endpoint.
 */
@Component
@RequiredArgsConstructor
public class EnvironmentKafkaContributor implements InfoContributor {

    private final ConfigurableEnvironment environment;

    @Override
    public void contribute(Info.Builder builder) {
        environment
                .getSystemProperties()
                .keySet()
                .stream()
                .filter(key -> key.toUpperCase().startsWith("KAFKA"))
                .forEach(key -> builder.withDetail(key, "<SET AS SYSTEM PROPERTY>"));
        environment
                .getSystemEnvironment()
                .keySet()
                .stream()
                .filter(key -> key.toUpperCase().startsWith("KAFKA"))
                .forEach(key -> builder.withDetail(key, "<SET AS SYSTEM ENVIRONMENT>"));
    }
}
