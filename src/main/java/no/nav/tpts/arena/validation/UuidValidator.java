package no.nav.tpts.arena.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.UUID;

@SuppressWarnings("unused")
@Slf4j
public class UuidValidator implements ArenaKafkaValidator {

    @Override
    public void validate(ConsumerRecord<String, String> content) throws ArenaKafkaValidationException {
        try {
            log.debug("Validated {} OK", UUID.fromString(content.value()));
        } catch (IllegalArgumentException e) {
            throw new ArenaKafkaValidationException(content.value() + " is not a valid UUID", e);
        }
    }
}
