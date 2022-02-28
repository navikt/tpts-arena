package no.nav.tpts;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import no.nav.security.token.support.core.api.Unprotected;
import org.springdoc.webmvc.ui.SwaggerConfigResource;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${app.azure.tenantId}")
    private String tenantId;

    @Value("${app.azure.clientId}")
    private String clientId;

    @Value("${app.azure.wellKnown}")
    private String openidConnectUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(
                new Components()
                    .addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                            .type(SecurityScheme.Type.OPENIDCONNECT)
                            .openIdConnectUrl(openidConnectUrl)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            );
    }

    private String getScope() {
        return "api://" + clientId + "/.default";
    }
}
