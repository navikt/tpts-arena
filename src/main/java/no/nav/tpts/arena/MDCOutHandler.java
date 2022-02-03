package no.nav.tpts.arena;

import lombok.extern.slf4j.Slf4j;
import no.nav.common.log.MDCConstants;
import org.slf4j.MDC;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.ProtocolException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collections;
import java.util.Set;

/**
 * Cut'n'paste from https://github.com/navikt/modiapersonoversikt-api/blob/eb85e37863ba5691a0285209dc8655eaaf11483a/web/src/main/java/no/nav/modiapersonoversikt/infrastructure/jaxws/handlers/MDCOutHandler.java, with some cleanup.
 */
@Slf4j
class MDCOutHandler implements SOAPHandler<SOAPMessageContext> {

    // QName for the callId header
    private static final QName CALLID_QNAME = new QName("uri:no.nav.applikasjonsrammeverk", MDCConstants.MDC_CALL_ID);

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        // OUTBOUND processing
        if (Boolean.TRUE.equals(outbound)) {
            String callId = MDC.get(MDCConstants.MDC_CALL_ID);
            if (callId == null) {
                throw new NullPointerException("CallId skal være tilgjengelig i MDC på dette tidspunkt. Om du er en webapp, må du legge til et MDCFilter i web.xml " +
                        "(oppskrift på dette: http://confluence.adeo.no/display/Modernisering/MDCFilter). " +
                        "Om du er noe annet må du generere callId selv og legge på MDC. Hjelpemetoder finnes i no.nav.modig.common.MDCOperations.");
            }
            log.debug("Add the callId to the SOAP message: " + callId);
            try {
                SOAPEnvelope envelope = context.getMessage().getSOAPPart().getEnvelope();
                SOAPHeader header = envelope.getHeader();

                SOAPElement callIdElement = header.addChildElement(CALLID_QNAME);
                callIdElement.setValue(callId);
            } catch (SOAPException e) {
                log.error(e.getMessage());
                throw new ProtocolException(e);
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
        // Does nothing.
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.singleton(CALLID_QNAME);
    }

}
