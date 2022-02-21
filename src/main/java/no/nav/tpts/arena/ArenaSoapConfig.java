package no.nav.tpts.arena;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.cxf.CXFClient;
import no.nav.common.cxf.StsConfig;
import no.nav.common.utils.EnvironmentUtils;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.YtelseskontraktV3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;

@Configuration
@Slf4j
public class ArenaSoapConfig {

    @Value("${app.arena.soap.sts.url}")
    private String stsUrl;

    @Value("${app.arena.soap.sts.username}")
    private String stsUsername;

    @Value("${app.arena.soap.sts.password}")
    private String stsPassword;

    @Value("${app.arena.soap.endpoints.ytelseskontrakt}")
    private String ytelseskontraktUrl;

    @Bean
    StsConfig stsConfig() {
        log.info("Using URL {} for STS", stsUrl);
        return StsConfig.builder()
                .url(stsUrl)
                .username(stsUsername)
                .password(stsPassword)
                .build();
    }

    @Bean
    public YtelseskontraktV3 ytelseskontraktV3(StsConfig stsConfig) {
        log.info("Using URL {} for service {}", ytelseskontraktUrl, YtelseskontraktV3.class.getSimpleName());
        return new CXFClient<>(YtelseskontraktV3.class)
                .wsdl("classpath:wsdl/tjenestespesifikasjon/no/nav/tjeneste/virksomhet/ytelseskontrakt/v3/Binding.wsdl")
                .serviceName(new QName("http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3/Binding", "Ytelseskontrakt_v3"))
                .endpointName(new QName("http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3/Binding", "Ytelseskontrakt_v3Port"))
                .withHandler(new MDCOutHandler())
                .address(ytelseskontraktUrl)
                .configureStsForSystemUser(stsConfig) // .configureStsForSubject(stsConfig)
                .build();
    }

}