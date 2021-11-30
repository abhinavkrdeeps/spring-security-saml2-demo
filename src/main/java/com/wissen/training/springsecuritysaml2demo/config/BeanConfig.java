package com.wissen.training.springsecuritysaml2demo.config;

import org.opensaml.security.x509.X509Support;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class BeanConfig {

    @Bean
    protected RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() throws CertificateException {
        ClassLoader classLoader = getClass().getClassLoader();
        File verificationKey = new File(classLoader.getResource("okta.cert").getFile());
        System.out.println(verificationKey.getAbsolutePath());

        System.out.println("file exists: "+verificationKey.exists());
        System.out.println(verificationKey.getAbsoluteFile());
        X509Certificate x509Certificate = X509Support.decodeCertificate(verificationKey);

        System.out.println("x509Certificate: "+x509Certificate);

        assert x509Certificate != null;
        Saml2X509Credential saml2X509Credential = Saml2X509Credential.verification(x509Certificate);

        RelyingPartyRegistration relyingPartyRegistration = RelyingPartyRegistration.withRegistrationId("okta-saml")
                .assertingPartyDetails(party-> party.
                        entityId("http://www.okta.com/exk2wnnilx1CNTqrh5d7")
                        .singleSignOnServiceLocation("https://dev-91259471.okta.com/app/dev-91259471_samldemo_1/exk2wnnilx1CNTqrh5d7/sso/saml")
                        .wantAuthnRequestsSigned(false)
                        .verificationX509Credentials(
                                c->c.add(saml2X509Credential)
                        )
                ).build();

        System.out.println("relyingPartyRegistration: "+relyingPartyRegistration.getRegistrationId());
        return new InMemoryRelyingPartyRegistrationRepository(relyingPartyRegistration);
    }
}
