package no.nav.tpts.arena.ytelser;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Vedtak;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

@Getter
@Setter
@Builder
public class YtelseVedtak {

    XMLGregorianCalendar beslutningsDato;
    String periodetypeForYtelse;
    XMLGregorianCalendar vedtaksperiodeFom;
    XMLGregorianCalendar vedtaksperiodeTom;
    String vedtaksType;
    String status;

    static List<YtelseVedtak> of(List<Vedtak> vedtakListe) {
        return vedtakListe.stream()
            .map(vedtak -> YtelseVedtak.builder()
                .beslutningsDato(vedtak.getBeslutningsdato())
                .periodetypeForYtelse(vedtak.getPeriodetypeForYtelse())
                .vedtaksperiodeFom(vedtak.getVedtaksperiode().getFom())
                .vedtaksperiodeTom(vedtak.getVedtaksperiode().getTom())
                .vedtaksType(vedtak.getVedtakstype())
                .status(vedtak.getStatus())
                .build()
            )
            .toList();
    }
}
