package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/arena")
@RequiredArgsConstructor
public class ArenaController {

    private final ArenaSoapService service;

    @GetMapping("soap/ping")
    public void ping() {
        if (!service.ping()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to ping");
        }
    }

}
