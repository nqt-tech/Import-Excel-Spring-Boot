package net.sharecs.city.sharecsservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class SharecsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharecsServiceApplication.class, args);
    }

}
