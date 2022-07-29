package cn.mianshiyi.braumadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.mianshiyi.mapper")
@SpringBootApplication(scanBasePackages = "cn.mianshiyi")
public class BraumAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BraumAdminApplication.class, args);
    }

}
