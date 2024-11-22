package com.hmall.trade;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hmall.trade.mapper")
public class TradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class,args);
    }
}
