package com.moon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 启动类
 * @author:Y.0
 * @date:2023/9/21
 */
@SpringBootApplication
@Slf4j
public class SaBlogSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaBlogSpringBootApplication.class, args);
        log.info("ʕ•ᴥ•ʔ Night breeze...");
        log.info("(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ");
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
