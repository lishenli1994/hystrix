package com.dodo.eurekaribbon.controller;

import com.dodo.eurekaribbon.inter.WelcomeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @Autowired
    WelcomeInterface welcomeInterface;
    @RequestMapping(value = "/welcome")
    public String welcome(@RequestParam String name){
        return welcomeInterface.welcomeClientOne(name);
    }
}