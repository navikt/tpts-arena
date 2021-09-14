package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArenaKafkaService {

    private final ArenaKafkaConfig config;
    private final BlockingQueue<String> cache = new ArrayBlockingQueue<>(10);

    BlockingQueue<String> getContent() {
        return cache;
    }

    void deleteContent() {
        log.info("Clearing cache of {} elements", cache.size());
        cache.clear();
    }

    @KafkaListener(topics = "${app.arena.kafka.consumer.topic}")
    public void consume(ConsumerRecord<String, String> content) {
      log.info("Received content {} from topic {}", content, config.getConsumer().getTopic());
      try {
          cache.add(content.value());
      } catch (IllegalStateException ise) {
          log.info("Cache is full, waiting to add...");
          try {
              cache.put(content.value());
          } catch (InterruptedException ie) {
              log.warn("Interrupted waiting for cache", ie);
              Thread.currentThread().interrupt();
          }
      }
    }

}
