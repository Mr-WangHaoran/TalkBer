package com.talkber;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.talkber.dao")
public class TalkberApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalkberApplication.class, args);
    }

}
