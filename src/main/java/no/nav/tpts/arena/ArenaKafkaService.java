package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tpts.util.SimpleCircularQueue;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArenaKafkaService {

    private final ArenaKafkaConfig config;
    private final SimpleCircularQueue<String> queue = new SimpleCircularQueue<>(10);

    List<String> getContent() {
        return queue.getAll();
    }

    void deleteContent() {
        log.info("Clearing cache of {} elements", queue.size());
        queue.clear();
    }

    @KafkaListener(topics = "${app.arena.kafka.consumer.topic}")
    public void consume(ConsumerRecord<String, String> content) {
        log.info("Received content {} from topic {}", content, config.getConsumer().getTopic());
        queue.put(content.value());
    }

}
