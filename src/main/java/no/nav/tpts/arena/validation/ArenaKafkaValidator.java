package no.nav.tpts.arena.validation;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ArenaKafkaValidator {

    void validate(ConsumerRecord<String, String> content) throws ArenaKafkaValidationException;

}
