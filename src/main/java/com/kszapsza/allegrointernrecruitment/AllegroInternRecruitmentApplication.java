package com.kszapsza.allegrointernrecruitment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AllegroInternRecruitmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AllegroInternRecruitmentApplication.class, args);
    }

}
