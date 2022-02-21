FROM navikt/java:17

COPY .nais/init.sh /init-scripts/init.sh
COPY target/tpts-arena-*.jar app.jar
