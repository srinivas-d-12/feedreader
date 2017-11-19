package com.test.feedreader.controller;


import com.test.feedreader.FeedReaderConstants;
import com.test.feedreader.model.Article;
import com.test.feedreader.model.Feed;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class ArticleControllerTest extends RestControllerTest {

    private Article article;
    private Feed feed;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        feed = new Feed("test-feed", "feed-description");
        article = new Article("article1", feed);

    }

    @Test
    public void testListArticles() throws Exception {
        Article savedArticle = articleRepository.save(this.article);
        mockMvc.perform(get(FeedReaderConstants.ARTICLE_CONTROLLER_ENDPOINT)).andExpect(status().isOk())
                .andExpect(content().contentType(FeedReaderConstants.JSON_CONTENT_TYPE)).andDo(print())
                .andExpect(jsonPath("$._embedded.articles", hasSize(1)))
                .andExpect(jsonPath("$._embedded.articles.[0].name", equalTo(savedArticle.getName())));

    }

    @Test
    public void testGetArticleById() throws Exception {
        Article savedArticle = articleRepository.save(this.article);
        mockMvc.perform(get(FeedReaderConstants.ARTICLE_CONTROLLER_ENDPOINT + "/" + savedArticle.getId()))
                .andExpect(status().isOk()).andExpect(content().contentType(FeedReaderConstants.JSON_CONTENT_TYPE))
                .andDo(print()).andExpect(jsonPath("$.name", equalTo(savedArticle.getName())));
    }

    @After
    public void cleanup() {
        deleteAllRepositories();
    }
}