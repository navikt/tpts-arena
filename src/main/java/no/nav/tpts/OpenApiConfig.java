package no.nav.tpts;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
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
                    .addSecuritySchemes("oauth",
                        new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(new OAuthFlows()
                            .authorizationCode(
                                new OAuthFlow()
                                    .authorizationUrl("https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/oauth2/v2.0/authorize")
                                    .scopes(new Scopes().addString(getScope(), "api-access"))
                                    .tokenUrl("https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/oauth2/v2.0/token")
                            )
                        )
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
