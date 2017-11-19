package com.test.feedreader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by sdhruvakumar.
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.test.feedreader", exclude = HypermediaAutoConfiguration.class)
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedReader.class, args);
    }

}
