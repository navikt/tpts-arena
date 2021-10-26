package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path = "/arena")
@RequiredArgsConstructor
public class ArenaKafkaController {

    private final ArenaKafkaService consumer;

    @GetMapping
    public Collection<Object> getCachedContent() {
        return consumer.getContent();
    }

}
