package no.nav.tpts.arena.ytelser;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Ytelseskontrakt;

import java.util.List;

@Getter
@Setter
@Builder
public class YtelseSak {

    String dataKravMottatt;
    Integer fagsystemSakId;
    String status;
    String ytelsestype;
    List<YtelseVedtak> vedtak;

    public static List<YtelseSak> of(List<Ytelseskontrakt> yteslser) {
        return yteslser.stream()
            .map(ytelse -> YtelseSak.builder()
                .dataKravMottatt(ytelse.getYtelsestype())
                .fagsystemSakId(ytelse.getFagsystemSakId())
                .status(ytelse.getStatus())
                .ytelsestype(ytelse.getYtelsestype())
                .vedtak(YtelseVedtak.of(ytelse.getIhtVedtak()))
                .build()
            )
            .toList();
    }
}
