package com.onixbyte.deltaforceguide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Entry point for the Delta Force Guide Server application.
 *
 * @author zihluwang
 */
@EnableAsync
@SpringBootApplication
public class DeltaForceGuideApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeltaForceGuideApplication.class, args);
    }

}
