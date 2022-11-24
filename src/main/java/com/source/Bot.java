package com.source;

import com.source.annotation.EnableLettuceRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Source
 * @date 2022/11/10/13:26
 */
@SpringBootApplication
@EnableLettuceRedis
public class Bot {
    public static void main(String[] args) {
        SpringApplication.run(Bot.class);
    }
}
