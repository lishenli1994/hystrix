package com.dodo.eurekafeign.controller;

import com.dodo.eurekafeign.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @Autowired
    WelcomeService welcomeService;
    @RequestMapping(value = "/welcome")
    public String welcome(@RequestParam String name){
        return welcomeService.welcomeService(name);
    }
}