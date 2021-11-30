package com.wissen.training.springsecuritysaml2demo.config;

import org.opensaml.security.x509.X509Support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.servlet.filter.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Service Provider (Application)
 * Identity Provider (Okta in this case)
 *
 * RelyingPartyRegistration:
 *      It Represents a configured relying party (service provider) and asserting party (Identity Provider) pair
 *
 *
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Add saml2 login for all urls except /
        http.authorizeRequests(authorize->authorize.antMatchers("/").permitAll().anyRequest().authenticated()).saml2Login();

        Converter<HttpServletRequest,RelyingPartyRegistration> relyingPartyRegistrationConverter =
                new DefaultRelyingPartyRegistrationResolver(relyingPartyRegistrationRepository);

        Saml2MetadataFilter saml2MetadataFilter = new Saml2MetadataFilter(relyingPartyRegistrationConverter,new OpenSamlMetadataResolver());

        http.addFilterBefore(saml2MetadataFilter, Saml2WebSsoAuthenticationFilter.class);

    }




}
