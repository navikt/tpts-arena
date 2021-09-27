package no.nav.tpts.arena;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@EmbeddedKafka
class ArenaKafkaControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    ArenaKafkaConfig config;

    @Autowired
    private KafkaTemplate<String, String> template;

    @AfterEach
    public void afterEach() {
        given()
                .port(port)
                .delete("/arena");
    }

    @Test
    void testSingleReceived() {

        String sent = UUID.randomUUID().toString();
        template.send(config.getConsumer().getTopic(), "testKey", sent);

        String[] received =
                given()
                        .port(port)
                        .get("/arena")
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(String[].class);

        assertThat(received)
                .hasSize(1)
                .contains(sent);

    }

    @Test
    void testQueueOverflow() {

        String[] sent = new String[12];
        for (int i = 0; i < sent.length; i++) {
            sent[i] = UUID.randomUUID().toString();
            template.send(config.getConsumer().getTopic(), 1, System.nanoTime(), "testKey" + i, sent[i]);
        }

        String[] received =
                given()
                        .port(port)
                        .get("/arena")
                        .then()
                        .assertThat()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(String[].class);

        assertThat(received)
                .hasSizeLessThan(sent.length)
                .doesNotContain(Arrays.copyOfRange(sent, 0, 2))
                .containsSequence(Arrays.copyOfRange(sent, 2, sent.length));

    }

}
