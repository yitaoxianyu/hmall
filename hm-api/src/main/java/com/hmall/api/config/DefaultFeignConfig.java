package com.hmall.api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {


    //这个可以对某一个feign生效或者可以直接添加到启动类中对整个模块生效
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }
}
