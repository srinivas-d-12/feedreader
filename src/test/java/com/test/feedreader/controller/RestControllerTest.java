package com.test.feedreader.controller;

import com.test.feedreader.TestApplication;
import com.test.feedreader.repository.ArticleRepository;
import com.test.feedreader.repository.FeedRespository;
import com.test.feedreader.repository.UserRepository;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by sdhruvakumar.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles(value = "test")
public abstract class RestControllerTest {

    protected static final String REST_DOCS_DOCUMENT_CONFIG = "{class-name}/{method-name}";
    protected MockMvc mockMvc;
    protected HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    protected WebApplicationContext webApplicationContext;
    @Autowired
    protected FeedRespository feedRespository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
    }

    @SuppressWarnings("unchecked")
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    protected void deleteAllRepositories() {

        this.articleRepository.deleteAllInBatch();
        this.feedRespository.deleteAllInBatch();
        this.userRepository.deleteAllInBatch();

    }

}