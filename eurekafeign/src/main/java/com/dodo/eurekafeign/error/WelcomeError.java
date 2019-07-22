package com.dodo.eurekafeign.error;

import com.dodo.eurekafeign.inter.WelcomeInterface;
import org.springframework.stereotype.Component;

@Component
public class WelcomeError implements WelcomeInterface {
    @Override
    public String welcomeClientOne(String name) {
        return "程序出现了一个" + name + "错误, 请稍后重试!";
    }
}
