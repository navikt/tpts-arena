package no.nav.tpts;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Value("${app.azure.tenantId}")
    private String tenantId;

    @Value("${app.azure.clientId}")
    private String clientId;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes("spring_oauth", new SecurityScheme()
                                        //.type(SecurityScheme.Type.OAUTH2)
                                        .type(SecurityScheme.Type.OPENIDCONNECT)
                                        .openIdConnectUrl("https://login.microsoftonline.com/966ac572-f5b7-4bbe-aa88-c76419c0f851/v2.0/.well-known/openid-configuration")
                                        /*.scheme("Bearer")
                                        .bearerFormat("JWT")
                                        .flows(new OAuthFlows()
                                                .authorizationCode(new OAuthFlow()
                                                        .authorizationUrl(getAuthorizationUrl())
                                                        .scopes(new Scopes()
                                                                .addString(getScope(), "Access to the application.")
                                                        )
                                                )
                                        )*/
                                ));
    }

    private String getAuthorizationUrl() {
        return "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/authorize";
    }
    private String getScope() {
        return "api://" + clientId + "/.default";
    }

}
