package com.dodo.eurekafeign.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WelcomeService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "error")
    public String welcomeService(String name) {
        return restTemplate.getForObject("http://welcome/welcome?name="+name,String.class);
    }

    public String error(String name) {
        return "程序出现了一个" + name + "错误, 请稍后重试!";
    }
}