package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ArenaKafkaController {

    private final ArenaKafkaService consumer;

    @GetMapping
    public void root() {
        // Does nothing.
    }

    @GetMapping(path = "/arena")
    public Collection<String> getCachedContent() {
        return consumer.getContent();
    }

    @DeleteMapping(path = "/arena")
    public void deleteCachedContent() {
        consumer.deleteContent();
    }

}
