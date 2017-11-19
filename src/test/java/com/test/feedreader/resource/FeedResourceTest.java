package com.test.feedreader.resource;

/**
 * Created by sdhruvakumar.
 */

import com.test.feedreader.controller.RestControllerTest;
import com.test.feedreader.controller.resource.FeedResource;
import com.test.feedreader.model.Feed;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by sdhruvakumar.
 */

public class FeedResourceTest extends RestControllerTest {

    private Feed feed;

    @Before
    public void setup() {
        feed = new Feed("test-feed", "feed-description");
        feed.setId(1L);
    }

    @Test
    public void feedResourceShouldHaveSelfLink() {
        FeedResource feedResource = FeedResource.ASSEMBLER.toResource(feed);
        assertThat(feedResource.hasLinks(), is(true));
        assertThat(feedResource.hasLink(Link.REL_SELF), is(true));
    }


    @Test
    public void feedResourceShouldHaveRelLinks() {
        Link articleLink = new Link("http://localhost/feeds/1/articles", "articles");
        FeedResource feedResource = FeedResource.ASSEMBLER.toResource(feed);
        assertThat(feedResource.hasLinks(), is(true));
        assertThat(feedResource.getLink("articles"), is(articleLink));

    }

}
