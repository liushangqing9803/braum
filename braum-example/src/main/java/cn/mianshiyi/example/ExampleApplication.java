package cn.mianshiyi.example;

import cn.mianshiyi.braumclient.annotation.EnableEasyRateLimiter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication( scanBasePackages = "cn.mianshiyi.example")
@EnableEasyRateLimiter
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

}
