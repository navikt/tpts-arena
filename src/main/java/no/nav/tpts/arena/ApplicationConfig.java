package no.nav.tpts.arena;

import no.nav.security.token.support.core.configuration.MultiIssuerConfiguration;
import no.nav.security.token.support.core.validation.JwtTokenValidationHandler;
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableJwtTokenValidation(ignore = { "org.springdoc" })
public class ApplicationConfig {}
