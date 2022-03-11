package no.nav.tpts.sts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class StsService {

    private final String url;
    private final String username;
    private final String password;

    public String getAccessToken() {

        String uri = UriComponentsBuilder
            .fromHttpUrl(url)
            .queryParam("grant_type", "client_credentials")
            .queryParam("scope", "openid")
            .toUriString();
        RestTemplate template = new RestTemplate();
        template.setInterceptors(List.of(new BasicAuthenticationInterceptor(username, password)));
        Response r = template.getForObject(uri, Response.class);
        return r == null ? null : r.token;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Response {
        @JsonProperty("access_token")
        String token;
    }

}
