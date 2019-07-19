package com.dodo.eurekaservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaserviceApplication.class, args);
    }

    @Value("${some.words}")
    String words;
    @RequestMapping("/welcome")
    public String home(@RequestParam String name, HttpServletRequest request) {
        return "Port: " + request.getServerPort() + " - " + name+" 欢迎您，这里有一些话想对你说:    " +words;
    }

}
