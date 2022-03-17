package no.nav.tpts.sts;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.Bus;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.apache.cxf.ws.security.trust.*;
import org.apache.cxf.ws.security.wss4j.SamlTokenInterceptor;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StsTokenInterceptor extends SamlTokenInterceptor {

    private static final String STS_CLIENT_AUTHENTICATION_POLICY = "classpath:untPolicy.xml";

    @Value("${app.arena.soap.sts.url}")
    private String url;

    @Value("${app.arena.soap.sts.username}")
    private String username;

    @Value("${app.arena.soap.sts.password}")
    private String password;

    private final STSTokenCacher stsTokenCacher;
    private final STSTokenRetriever.TokenRequestParams tokenParams;

    private STSClient stsClient;

    public StsTokenInterceptor() {
        super();
        this.stsTokenCacher = new DefaultSTSTokenCacher();
        this.tokenParams = new STSTokenRetriever.TokenRequestParams();
        this.tokenParams.setTokenTemplate(getRstTemplate());
    }

    private Element getRstTemplate() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(
                    StsTokenInterceptor.class.getResourceAsStream("/rstTemplate.xml"));
            doc.getDocumentElement().normalize();
            return doc.getDocumentElement();
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void addToken(SoapMessage message) {
        message.put(SecurityConstants.STS_CLIENT, getOrCreateSTSClient(message.getExchange().getBus()));
        message.put(SecurityConstants.CACHE_ISSUED_TOKEN_IN_ENDPOINT, true);
        SecurityToken securityToken = STSTokenRetriever.getToken(message, tokenParams, stsTokenCacher);
        log.info("Got security token with ID {} from STS at {}", securityToken.getId(), url);
        //appends the received token to the request for the actual service
        try {
            Header header = findSecurityHeader(message, true);
            SamlAssertionWrapper wrapper = new SamlAssertionWrapper(securityToken.getToken());
            Element element = (Element) header.getObject();
            element = (Element) DOMUtils.getDomElement(element);
            element.appendChild(wrapper.toDOM(element.getOwnerDocument()));
        } catch (WSSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private STSClient getOrCreateSTSClient(Bus bus) {
        if (this.stsClient == null) {
            STSClient stsClient = new STSClient(bus);
            stsClient.setEnableAppliesTo(false);
            stsClient.setAllowRenewing(false);
            stsClient.setLocation(url);
            stsClient.setKeyType("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Bearer");
            stsClient
                    .setTokenType("http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0");
            stsClient.setNamespace(STSUtils.WST_NS_05_12);
            HashMap<String, Object> properties = new HashMap<>();
            properties.put(SecurityConstants.USERNAME, username);
            properties.put(SecurityConstants.PASSWORD, password);

            stsClient.setProperties(properties);
            // used for the STS client to authenticate itself to the STS provider.
            stsClient.setPolicy(STS_CLIENT_AUTHENTICATION_POLICY);
            this.stsClient = stsClient;
        }
        return stsClient;
    }

}
