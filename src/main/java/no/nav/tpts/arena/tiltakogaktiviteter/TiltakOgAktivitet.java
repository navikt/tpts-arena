package no.nav.tpts.arena.tiltakogaktiviteter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.informasjon.Deltakerstatuser;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.informasjon.Periode;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.informasjon.Tiltaksaktivitet;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;

@Getter
@Setter
@Builder
public class TiltakOgAktivitet {
    protected String tiltaksnavn;
    protected String aktivitetId;
    protected String tiltakLokaltNavn;
    protected String arrangoer;
    protected Periode deltakelsePeriode;
    protected Float deltakelseProsent;
    protected Float antallDagerPerUke;
    protected Deltakerstatuser deltakerStatus;
    protected XMLGregorianCalendar statusSistEndret;
    protected String begrunnelseInnsoeking;

    public static List<TiltakOgAktivitet> of(List<Tiltaksaktivitet> tiltaksaktiviteter) {
        return tiltaksaktiviteter.stream().map(
                tiltaksaktivitet ->
                        TiltakOgAktivitet.builder()
                        .aktivitetId(tiltaksaktivitet.getAktivitetId())
                        .tiltakLokaltNavn(tiltaksaktivitet.getTiltakLokaltNavn())
                        .arrangoer(tiltaksaktivitet.getArrangoer())
                        .deltakelsePeriode(tiltaksaktivitet.getDeltakelsePeriode())
                        .deltakelseProsent(tiltaksaktivitet.getDeltakelseProsent())
                        .antallDagerPerUke(tiltaksaktivitet.getAntallDagerPerUke())
                        .antallDagerPerUke(tiltaksaktivitet.getAntallDagerPerUke())
                        .statusSistEndret(tiltaksaktivitet.getStatusSistEndret())
                        .begrunnelseInnsoeking(tiltaksaktivitet.getBegrunnelseInnsoeking())
                        .build()
        ).toList();
    }
}
