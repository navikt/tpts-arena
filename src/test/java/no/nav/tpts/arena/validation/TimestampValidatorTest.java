package no.nav.tpts.arena.validation;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TimestampValidatorTest {

    private static final ArenaKafkaValidator validator = new TimestampValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "2021-09-27T10:55:11.717193279Z",
            "2021-09-27T10:55:12.717177386Z",
            "2021-09-27T10:55:13.717232707Z",
            "2021-09-27T10:55:14.717255581Z",
            "2021-09-27T10:55:15.717416855Z",
            "2021-09-27T10:55:16.717224594Z",
            "2021-09-27T10:55:17.717336651Z",
            "2021-09-27T10:55:18.717318497Z",
            "2021-09-27T10:55:19.717385214Z",
            "2021-09-27T10:55:20.717397704Z"

    })
    void shouldValidate(String timestamp)
            throws Exception {

        ConsumerRecord<String, String> content = new ConsumerRecord<>("topic", 1, 2L, "key", timestamp);
        validator.validate(content);

    }

    @Test
    void shouldNotValidate() {

        ConsumerRecord<String, String> content = new ConsumerRecord<>("topic", 1, 2L, "key", "FOOBAR");
        assertThrows(ArenaKafkaValidationException.class, () -> validator.validate(content));

    }

}
