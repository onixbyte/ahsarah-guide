package com.onixbyte.ahsarahguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Entry point for the Ahsarah Guide Server application.
 *
 * @author zihluwang
 */
@EnableAsync
@SpringBootApplication
public class AhsarahGuideApplication {

    public static void main(String[] args) {
        SpringApplication.run(AhsarahGuideApplication.class, args);
    }

}
