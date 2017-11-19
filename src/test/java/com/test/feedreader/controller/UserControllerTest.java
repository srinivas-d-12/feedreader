package com.test.feedreader.controller;

import com.test.feedreader.FeedReaderConstants;
import com.test.feedreader.model.Feed;
import com.test.feedreader.model.User;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by sdhruvakumar.
 */

public class UserControllerTest extends RestControllerTest {

    private User user;
    private JSONObject jsonUser;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        user = new User("test", "test@email.com");
        jsonUser = new JSONObject().put("name", "test").put("email",
                "test@email.com");
    }

    @Test
    public void testListUsers() throws Exception {
        User user = userRepository.save(this.user);
        User userTwo = userRepository.save(new User("test2", "test2@email.com"));

        mockMvc.perform(get(FeedReaderConstants.USER_CONTROLLER_ENDPOINT)).andExpect(status().isOk())
                .andExpect(content().contentType(FeedReaderConstants.JSON_CONTENT_TYPE)).andDo(print())
                .andExpect(jsonPath("$._embedded.users", hasSize(2)))
                .andExpect(jsonPath("$._embedded.users.[0].name", equalTo(user.getName())))
                .andExpect(jsonPath("$._embedded.users.[0].email", equalTo(user.getEmail())))
                .andExpect(jsonPath("$._embedded.users.[1].name", equalTo(userTwo.getName())))
                .andExpect(jsonPath("$._embedded.users.[1].email", equalTo(userTwo.getEmail())));

    }

    @Test
    public void testGetUserById() throws Exception {
        User savedUser = userRepository.save(this.user);
        mockMvc.perform(get(FeedReaderConstants.USER_CONTROLLER_ENDPOINT + "/" + savedUser.getId()))
                .andExpect(status().isOk()).andExpect(content().contentType(FeedReaderConstants.JSON_CONTENT_TYPE))
                .andDo(print()).andExpect(jsonPath("$.name", equalTo(user.getName())))
                .andExpect(jsonPath("$.email", equalTo(user.getEmail())));
    }


    @Test
    public void testGetUsersWithInvalidIdShouldExpectFailure() throws Exception {
        mockMvc.perform(get(FeedReaderConstants.USER_CONTROLLER_ENDPOINT + "/0")).andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.requestUrl", equalTo("/users/0")))
                .andExpect(jsonPath("$.debugMessage", equalTo("User could not be found with id: 0")));
    }

    @Test
    public void testAddUser() throws Exception {
        mockMvc.perform(post(FeedReaderConstants.USER_CONTROLLER_ENDPOINT)
                .contentType(FeedReaderConstants.JSON_CONTENT_TYPE).content(jsonUser.toString()))
                .andExpect(status().isCreated()).andDo(print())
                .andExpect(jsonPath("$.name", equalTo(jsonUser.get("name"))))
                .andExpect(jsonPath("$.email", equalTo(jsonUser.get("email"))));
    }


    @Test
    public void testAddUserWithInvalidParameters() throws Exception {
        JSONObject user = new JSONObject().put("name", "test");
        mockMvc.perform(post(FeedReaderConstants.USER_CONTROLLER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON).content(user.toString()))
                .andExpect(status().isBadRequest()).andDo(print())
                .andExpect(jsonPath("$.errorMessages.email", contains("may not be null - user.email")))
                .andExpect(jsonPath("$.errorCount", equalTo("Validation failed with 1 error(s)")));
    }

    @Test
    public void testFeedSubscription() throws Exception {
        User savedUser = userRepository.save(this.user);
        Feed feed = new Feed("test-feed", "feed-description");
        Feed savedFeed = feedRespository.save(feed);
        // Add feed id to the user/{id}/feeds -> subscription
        JSONObject jsonFeed = new JSONObject().put("id", savedFeed.getId());

        mockMvc.perform(put(FeedReaderConstants.USER_CONTROLLER_ENDPOINT + "/" + savedUser.getId() + "/feeds")
                .contentType(FeedReaderConstants.JSON_CONTENT_TYPE).content(jsonFeed.toString()))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.name", equalTo(savedFeed.getName())))
                .andExpect(jsonPath("$.description", equalTo(savedFeed.getDescription())));

    }

    @Test
    public void testFeedUnSubscription() throws Exception {
        User savedUser = userRepository.save(this.user);
        Feed feed = new Feed("test-feed", "feed-description");
        Feed savedFeed = feedRespository.save(feed);
        // Delete feed id from user/{id}/feeds -> subscription
        JSONObject jsonFeed = new JSONObject().put("id", savedFeed.getId());

        //  subscribe to a feed
        mockMvc.perform(put(FeedReaderConstants.USER_CONTROLLER_ENDPOINT + "/" + savedUser.getId() + "/feeds")
                .contentType(FeedReaderConstants.JSON_CONTENT_TYPE).content(jsonFeed.toString()))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.name", equalTo(savedFeed.getName())))
                .andExpect(jsonPath("$.description", equalTo(savedFeed.getDescription())));
        // unsubscribe from a feed
        mockMvc.perform(delete(FeedReaderConstants.USER_CONTROLLER_ENDPOINT + "/" + savedUser.getId() + "/feeds")
                .contentType(FeedReaderConstants.JSON_CONTENT_TYPE).content(jsonFeed.toString()))
                .andExpect(status().isNoContent()).andDo(print());

    }

    @After
    public void cleanup() {
        deleteAllRepositories();
    }

}