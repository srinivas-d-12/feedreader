package com.test.feedreader.repository;

import com.test.feedreader.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by sdhruvakumar.
 */

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
