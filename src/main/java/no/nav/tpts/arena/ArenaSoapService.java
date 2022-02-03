package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.YtelseskontraktV3;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArenaSoapService {

    private final YtelseskontraktV3 service;

    boolean ping() {
        try {
            service.ping();
            return true;
        } catch (Exception e) {
            log.error("Failed to ping service", e);
            return false;
        }
    }

}
