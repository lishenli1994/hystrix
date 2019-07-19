package com.dodo.eurekaribbon.error;

import com.dodo.eurekaribbon.inter.WelcomeInterface;
import org.springframework.stereotype.Component;

@Component
public class WelcomeError implements WelcomeInterface {
    @Override
    public String welcomeClientOne(String name) {
        return "程序出现了一个" + name + "错误, 请稍后重试!";
    }
}
