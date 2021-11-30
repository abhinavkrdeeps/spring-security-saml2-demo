package com.wissen.training.springsecuritysaml2demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @RequestMapping("/")
    public String index(){
        return "home";
    }

    public String securedHello(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal, Model model){
        model.addAttribute("user",principal.getName());
        return "secured";
    }
}
