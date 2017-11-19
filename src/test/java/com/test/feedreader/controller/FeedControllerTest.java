package com.test.feedreader.controller;

import com.test.feedreader.FeedReaderConstants;
import com.test.feedreader.model.Feed;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by sdhruvakumar.
 */
public class FeedControllerTest extends RestControllerTest {
    private Feed feed;
    private JSONObject jsonFeed;
    private JSONObject jsonArticle;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        feed = new Feed("feed", "feed-description");
        jsonFeed = new JSONObject().put("name", "feed").put("description",
                "feed-description");

        jsonArticle = new JSONObject().put("name", "article1");

    }

    @Test
    public void testListFeeds() throws Exception {
        Feed feed = feedRespository.save(this.feed);
        Feed feedTwo = feedRespository.save(new Feed("feed2", "feed2-description"));

        mockMvc.perform(get(FeedReaderConstants.FEED_CONTROLLER_ENDPOINT)).andExpect(status().isOk())
                .andExpect(content().contentType(FeedReaderConstants.JSON_CONTENT_TYPE)).andDo(print())
                .andExpect(jsonPath("$._embedded.feeds", hasSize(2)))
                .andExpect(jsonPath("$._embedded.feeds.[0].name", equalTo(feed.getName())))
                .andExpect(jsonPath("$._embedded.feeds.[0].description", equalTo(feed.getDescription())))
                .andExpect(jsonPath("$._embedded.feeds.[1].name", equalTo(feedTwo.getName())))
                .andExpect(jsonPath("$._embedded.feeds.[1].description", equalTo(feedTwo.getDescription())));

    }

    @Test
    public void testGetFeedById() throws Exception {
        Feed savedFeed = feedRespository.save(this.feed);
        mockMvc.perform(get(FeedReaderConstants.FEED_CONTROLLER_ENDPOINT + "/" + savedFeed.getId()))
                .andExpect(status().isOk()).andExpect(content().contentType(FeedReaderConstants.JSON_CONTENT_TYPE))
                .andDo(print()).andExpect(jsonPath("$.name", equalTo(feed.getName())))
                .andExpect(jsonPath("$.description", equalTo(feed.getDescription())));
    }


    @Test
    public void testGetFeedsWithInvalidId() throws Exception {
        mockMvc.perform(get(FeedReaderConstants.FEED_CONTROLLER_ENDPOINT + "/0")).andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.requestUrl", equalTo("/feeds/0")))
                .andExpect(jsonPath("$.debugMessage", equalTo("Feed could not be found with id: 0")));
    }

    @Test
    public void testAddFeed() throws Exception {
        mockMvc.perform(post(FeedReaderConstants.FEED_CONTROLLER_ENDPOINT)
                .contentType(FeedReaderConstants.JSON_CONTENT_TYPE).content(jsonFeed.toString()))
                .andExpect(status().isCreated()).andDo(print())
                .andExpect(jsonPath("$.name", equalTo(jsonFeed.get("name"))))
                .andExpect(jsonPath("$.description", equalTo(jsonFeed.get("description"))));
    }


    @Test
    public void testAddFeedWithInvalidParameters() throws Exception {
        JSONObject feed = new JSONObject().put("name", "test-feed");
        mockMvc.perform(post(FeedReaderConstants.FEED_CONTROLLER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(feed.toString()))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(jsonPath("$.errorMessages.description", contains("may not be null - feed.description")))
                .andExpect(jsonPath("$.errorCount", equalTo("Validation failed with 1 error(s)")));
    }


    @Test
    public void testAddArticleToFeed() throws Exception {
        Feed savedFeed = feedRespository.save(this.feed);
        mockMvc.perform(post(FeedReaderConstants.FEED_CONTROLLER_ENDPOINT + "/" + savedFeed.getId() + "/articles")
                .contentType(FeedReaderConstants.JSON_CONTENT_TYPE).content(jsonArticle.toString()))
                .andExpect(status().isCreated()).andDo(print())
                .andExpect(jsonPath("$.name", equalTo(jsonArticle.get("name"))));
    }

    @Test
    public void testAddArticleWithInvalidParametersToFeed() throws Exception {
        Feed savedFeed = feedRespository.save(this.feed);
        JSONObject article = new JSONObject().put("invalid", "test-feed");
        mockMvc.perform(post(FeedReaderConstants.FEED_CONTROLLER_ENDPOINT + "/" + savedFeed.getId() + "/articles")
                .contentType(FeedReaderConstants.JSON_CONTENT_TYPE).content(article.toString()))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(jsonPath("$.errorMessages.name", contains("may not be null - article.name")))
                .andExpect(jsonPath("$.errorCount", equalTo("Validation failed with 1 error(s)")));
    }

    @After
    public void cleanup() {
        deleteAllRepositories();
    }

}
