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
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    @Value("${app.azure.tenantId}")
    private String tenantId;

    @Value("${app.azure.clientId}")
    private String clientId;

    @Value("${app.azure.wellKnown}")
    private String openidConnectUrl;

    /**
     * Redirect root URL to Swagger UI.
     *
     * @see WebMvcConfigurer#addViewControllers(ViewControllerRegistry)
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/swagger-ui/index.html");
    }

    @Bean
    public OpenAPI openAPI() {
        String authorizationUrl = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/authorize";
        String scope = "api://" + clientId + "/.default";
        String tokenUrl = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";

        return new OpenAPI()
            .components(
                new Components()
                    .addSecuritySchemes("oauth",
                        new SecurityScheme()
                            .type(SecurityScheme.Type.OAUTH2)
                            .flows(new OAuthFlows()
                                .authorizationCode(
                                    new OAuthFlow()
                                        .authorizationUrl(authorizationUrl)
                                        .scopes(new Scopes().addString(scope, "Get access to the API."))
                                        .tokenUrl(tokenUrl)
                                )
                            )
                            .openIdConnectUrl(openidConnectUrl)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            );
    }

}
