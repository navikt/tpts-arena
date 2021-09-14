package no.nav.tpts.arena;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@EmbeddedKafka(controlledShutdown = true)
public class ArenaKafkaControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    ArenaKafkaConfig config;

    @Autowired
    private KafkaTemplate<String, String> template;

    @After
    public void after() {
        given()
                .port(port)
                .delete("/arena");
    }

    @Test
    public void testSingleReceived() {

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

        assertThat(received).hasSize(1);
        assertThat(received[0]).isEqualTo(sent);

    }

    @Test
    public void testCacheOverflow() {

        String[] sent = new String[11];
        for (int i = 0; i < sent.length; i++) {
            sent[i] = UUID.randomUUID().toString();
            template.send(config.getConsumer().getTopic(), 1, System.currentTimeMillis(), "testKey" + i, sent[i]);
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

        assertThat(received).hasSizeLessThan(sent.length);
        for (int i = 0; i < received.length; i++) {
            assertThat(received[i]).isEqualTo(sent[i]);
        }

    }

    @Test
    public void testDeleteAndReceive() {

        String[] sent = new String[20];
        for (int i = 0; i < sent.length; i++) {
            sent[i] = UUID.randomUUID().toString();
            template.send(config.getConsumer().getTopic(), 1, System.currentTimeMillis(), "sentKey" + i, sent[i]);
        }

        given()
                .port(port)
                .delete("/arena")
                .then()
                .assertThat()
                .statusCode(200);

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

        assertThat(received).hasSize(10);
        for (int i = 0; i < received.length; i++) {
            assertThat(received[i]).isEqualTo(sent[10 + i]);
        }


    }

}
