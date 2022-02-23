package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import no.nav.security.token.support.core.api.Protected;
import no.nav.security.token.support.core.api.Unprotected;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Ytelseskontrakt;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Protected
@RestController
@RequestMapping(path = "/arena")
@RequiredArgsConstructor
public class ArenaController {

    private final ArenaSoapService service;

    @Unprotected
    @GetMapping("soap/ping")
    public void ping() {
        if (!service.ping()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to ping");
        }
    }

    @Protected
    @GetMapping("soap/ytelser/{fnr}")
    public List<Ytelseskontrakt> getYtelser(@PathVariable String fnr, @RequestParam(required = false) String fom, @RequestParam(required = false) String tom) {
        return service.getYtelser(fnr, fom, tom);
    }

}
