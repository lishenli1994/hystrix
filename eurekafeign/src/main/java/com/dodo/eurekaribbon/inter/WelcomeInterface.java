package com.dodo.eurekaribbon.inter;

import com.dodo.eurekaribbon.error.WelcomeError;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "welcome", fallback = WelcomeError.class)
public interface WelcomeInterface {
    @RequestMapping(value = "/welcome",method = RequestMethod.GET)
    String welcomeClientOne(@RequestParam(value = "name") String name);
}

