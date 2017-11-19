package com.test.feedreader;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by sdhruvakumar.
 */

@SpringBootApplication
@EnableJpaRepositories
public class FeedReader {

    public static void main(String[] args) {
        SpringApplication.run(FeedReader.class, args);
    }


}
