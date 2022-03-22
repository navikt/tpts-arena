package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.HentTiltakOgAktiviteterForBrukerPersonIkkeFunnet;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.HentTiltakOgAktiviteterForBrukerSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.HentTiltakOgAktiviteterForBrukerUgyldigInput;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.TiltakOgAktivitetV1;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.informasjon.Tiltaksaktivitet;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.meldinger.HentTiltakOgAktiviteterForBrukerRequest;
import no.nav.tjeneste.virksomhet.tiltakogaktivitet.v1.meldinger.HentTiltakOgAktiviteterForBrukerResponse;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.HentYtelseskontraktListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.YtelseskontraktV3;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Periode;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Ytelseskontrakt;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.HentYtelseskontraktListeRequest;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.HentYtelseskontraktListeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArenaSoapService {

    private final YtelseskontraktV3 ytelseskontraktV3Service;
    private final TiltakOgAktivitetV1 tiltakOgAktivitetV1Service;

    boolean ping() {
        try {
            ytelseskontraktV3Service.ping();
            return true;
        } catch (Exception e) {
            log.error("Failed to ping service", e);
            return false;
        }
    }

    public XMLGregorianCalendar toGregorianOrNull(String dateParam) {
        try {
            if (dateParam == null) return null;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = format.parse(dateParam);

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);

            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (ParseException | DatatypeConfigurationException exception) {
            return null;
        }
    }

    public List<Ytelseskontrakt> getYtelser(String fnr, String fom, String tom) {
        Periode periode = new Periode();
        periode.setFom(toGregorianOrNull(fom));
        periode.setTom(toGregorianOrNull(tom));
        HentYtelseskontraktListeRequest request = new HentYtelseskontraktListeRequest();
        request.setPeriode(periode);
        request.setPersonidentifikator(fnr);
        try {
            HentYtelseskontraktListeResponse response = ytelseskontraktV3Service.hentYtelseskontraktListe(request);
            return response.getYtelseskontraktListe();
        } catch (HentYtelseskontraktListeSikkerhetsbegrensning exception) {
            log.error("HentYtelseskontraktListeSikkerhetsbegrensning feil:", exception);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Tiltaksaktivitet> getTiltaksaktivitetListe(String fnr) {
        HentTiltakOgAktiviteterForBrukerRequest request = new HentTiltakOgAktiviteterForBrukerRequest();
        request.setPersonident(fnr);
        try {
            HentTiltakOgAktiviteterForBrukerResponse response = tiltakOgAktivitetV1Service.hentTiltakOgAktiviteterForBruker(request);
            return response.getTiltaksaktivitetListe();
        } catch (HentTiltakOgAktiviteterForBrukerSikkerhetsbegrensning |
                HentTiltakOgAktiviteterForBrukerUgyldigInput |
                HentTiltakOgAktiviteterForBrukerPersonIkkeFunnet exception) {
            log.error("HentTiltakOgAktiviteterForBrukerSikkerhetsbegrensning feil:", exception);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
