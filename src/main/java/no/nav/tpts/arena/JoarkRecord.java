package no.nav.tpts.arena;

public record JoarkRecord(
        String behandlingstema,
        String hendelsesid,
        String hendelsestype,
        Long journalpostid,
        String journalpoststatus,
        String kanalreferanseid,
        String mottakskanal,
        String temagammelt,
        String temanytt,
        Integer versjon
) {
}
