package no.nav.tpts.arena;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.HentYtelseskontraktListeSikkerhetsbegrensning;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.YtelseskontraktV3;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Periode;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.informasjon.ytelseskontrakt.Ytelseskontrakt;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.HentYtelseskontraktListeRequest;
import no.nav.tjeneste.virksomhet.ytelseskontrakt.v3.meldinger.HentYtelseskontraktListeResponse;
import no.nav.tpts.sts.StsService;
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

    private final YtelseskontraktV3 service;
    private final StsService sts;

    boolean ping() {
        try {
            service.ping();
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
            HentYtelseskontraktListeResponse response = service.hentYtelseskontraktListe(request);
            return response.getYtelseskontraktListe();
        } catch (HentYtelseskontraktListeSikkerhetsbegrensning exception) {
            log.error("HentYtelseskontraktListeSikkerhetsbegrensning feil:", exception);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
