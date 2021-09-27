package no.nav.tpts.arena;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
@EnableKafka
@ConfigurationProperties(prefix = "app.arena.kafka")
@Getter
@Setter
public class ArenaKafkaConfig {

    @NonNull
    private Consumer consumer;

    @Getter
    @Setter
    public static class Consumer {

        @NonNull
        private String topic;

        private String[] validators;

    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory(ConsumerFactory<String, Object> consumerFactory) {
        final ConcurrentKafkaListenerContainerFactory<String, Object> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory);
        return containerFactory;
    }

}
