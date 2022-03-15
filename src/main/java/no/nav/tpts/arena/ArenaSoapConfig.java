package no.nav.tpts.arena;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.cxf.CXFClient;
import no.nav.common.cxf.StsConfig;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.YtelseskontraktV3;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.TiltakOgAktivitetV1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;

@Configuration
@Slf4j
public class ArenaSoapConfig {

    @Value("${app.arena.soap.endpoints.ytelseskontrakt}")
    private String ytelseskontraktUrl;

    @Value("${app.arena.soap.sts.url}")
    private String stsUrl;

    @Value("${app.arena.soap.sts.username}")
    private String stsUsername;

    @Value("${app.arena.soap.sts.password}")
    private String stsPassword;
    @Value("${app.arena.soap.endpoints.tiltakogaktivitet")
    private String tiltakogaktivitetUrl;

    @Bean
    public YtelseskontraktV3 ytelseskontraktV3() {

        log.info("Using URL {} for service {}", ytelseskontraktUrl, YtelseskontraktV3.class.getSimpleName());
        return new CXFClient<>(YtelseskontraktV3.class)
                .wsdl("classpath:wsdl/tjenestespesifikasjon/no/nav/tjeneste/virksomhet/ytelseskontrakt/v3/Binding.wsdl")
                .serviceName(new QName("http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3/Binding", "Ytelseskontrakt_v3"))
                .endpointName(new QName("http://nav.no/tjeneste/virksomhet/ytelseskontrakt/v3/Binding", "Ytelseskontrakt_v3Port"))
                .withOutInterceptor(new LoggingOutInterceptor())
                .address(ytelseskontraktUrl)
                .configureStsForSystemUser(StsConfig.builder()
                        .url(stsUrl)
                        .username(stsUsername)
                        .password(stsPassword)
                        .build())
                .build();

    }

    @Bean
    public TiltakOgAktivitetV1 tiltakOgAktivitetV1(
            @Value("${app.arena.soap.sts.url}")
                    String url,
            @Value("${app.arena.soap.sts.username}")
                    String username,
            @Value("${app.arena.soap.sts.password}")
                    String password
    ) {
        StsConfig stsConfig = StsConfig.builder()
                .url(url)
                .username(username)
                .password(password)
                .build();
        log.info("Using URL {} for service {}", tiltakogaktivitetUrl, TiltakOgAktivitetV1.class.getSimpleName());
        return new CXFClient<>(TiltakOgAktivitetV1.class)
                .wsdl("classpath:wsdl/tjenestespesifikasjon/no/nav/tjeneste/virksomhet/tiltakogaktivitet/v1/Binding.wsdl")
                .serviceName(new QName("http://nav.no/tjeneste/virksomhet/tiltakogaktivitet/v1/Binding", "<Tiltakogaktivitet_v1>"))
                .endpointName(new QName("http://nav.no/tjeneste/virksomhet/tiltakogaktivitet/v1/Binding", "Tiltakogaktivitet_v1Port"))
                .withOutInterceptor(new LoggingOutInterceptor())
                .address(tiltakogaktivitetUrl)
                .configureStsForSystemUser(stsConfig)
                .build();
    }
}
