package no.nav.tpts.arena;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import no.nav.tpts.arena.validation.ArenaKafkaValidationException;
import no.nav.tpts.arena.validation.ArenaKafkaValidator;
import no.nav.tpts.util.SimpleCircularQueue;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ArenaKafkaService {

    private final String topic;
    private final Counter validationSuccess;
    private final Counter validationFailure;
    private final SimpleCircularQueue<String> queue = new SimpleCircularQueue<>(10);
    private final List<ArenaKafkaValidator> validators = new ArrayList<>();

    public ArenaKafkaService(ArenaKafkaConfig config, PrometheusMeterRegistry registry) {
        topic = config.getConsumer().getTopic();
        validationSuccess = registry.counter("kafka_consumer_validation", "outcome", "SUCCESS");
        validationFailure = registry.counter("kafka_consumer_validation", "outcome", "FAILURE");

        Optional
                .ofNullable(config.getConsumer().getValidators())
                .ifPresent(validatorsClassNames -> Arrays
                        .stream(validatorsClassNames)
                        .forEach(validatorClassName -> {
                            try {
                                ArenaKafkaValidator validatorInstance = (ArenaKafkaValidator) ClassUtils
                                        .forName(validatorClassName, null)
                                        .getDeclaredConstructor()
                                        .newInstance();
                                validators.add(validatorInstance);
                                log.info("Added validator {}", validatorInstance.getClass().getSimpleName());
                            } catch (ReflectiveOperationException e) {
                                log.error("Failed to instantiate validator class {}", validatorClassName, e);
                            }
                        }));
        log.info("Configured {} validator(s)", validators.size());
    }

    List<String> getContent() {
        return queue.getAll();
    }

    void deleteContent() {
        log.info("Clearing cache of {} elements", queue.size());
        queue.clear();
    }

    @KafkaListener(topics = "${app.arena.kafka.consumer.topic}")
    public void consume(ConsumerRecord<String, String> content) {
        log.info("Received content {} from topic {}", content, topic);
        try {
            for (ArenaKafkaValidator validator : validators) {
                validator.validate(content);
            }
            queue.put(content.value());
            validationSuccess.increment();
        } catch (ArenaKafkaValidationException e) {
            log.error("Validation failed", e);
            validationFailure.increment();
        }
    }

}
