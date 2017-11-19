package com.test.feedreader.repository;

import com.test.feedreader.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * Created by sdhruvakumar.
 */
public interface FeedRespository extends JpaRepository<Feed, Long> {

}
