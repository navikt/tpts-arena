package no.nav.tpts.sts;

import lombok.RequiredArgsConstructor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class StsService {

    private final String url;
    private final String username;
    private final String password;

    public String getAccessToken() {

        String uri = UriComponentsBuilder.fromHttpUrl(url).queryParam("grant_type", "client_credentials").queryParam("scope", "openid").toUriString();
        RestTemplate template = new RestTemplate();
        template.setInterceptors(List.of(new BasicAuthenticationInterceptor(username, password)));
        Map<String, String> response = template.getForObject(uri, Map.class);
        return response == null ? null : response.get("access_token");

    }

}
