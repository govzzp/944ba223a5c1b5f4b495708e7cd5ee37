package pers.me.monday;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan("pers.me.monday.filter")
@MapperScan("pers.me.monday.mapper")
public class MondayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MondayApplication.class, args);
    }

}
