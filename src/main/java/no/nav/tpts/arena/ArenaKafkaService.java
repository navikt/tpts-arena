package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.joarkjournalfoeringhendelser.JournalfoeringHendelseRecord;
import no.nav.tpts.util.SimpleCircularQueue;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ArenaKafkaService {

    private final String topic;
    private final SimpleCircularQueue<Object> queue = new SimpleCircularQueue<>(10);

    List<Object> getContent() {
        return queue.getAll();
    }

    @KafkaListener(topics = "${app.arena.kafka.consumer.topic}")
    public void consume(@Payload JournalfoeringHendelseRecord content) {
        log.info("Received content {} from topic {}", content, topic);
        queue.put(content);
    }

}
