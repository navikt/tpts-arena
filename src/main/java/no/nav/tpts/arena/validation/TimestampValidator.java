package no.nav.tpts.arena.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@SuppressWarnings("unused")
@Slf4j
public class TimestampValidator implements ArenaKafkaValidator {

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'");

    @Override
    public void validate(ConsumerRecord<String, String> content) throws ArenaKafkaValidationException {
        try {
            format.parse(content.value());
        } catch (ParseException e) {
            throw new ArenaKafkaValidationException("Failed to validate " + content.value(), e);
        }
    }
}
