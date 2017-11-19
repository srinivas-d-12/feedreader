package com.test.feedreader.resource;

import com.test.feedreader.controller.RestControllerTest;
import com.test.feedreader.controller.resource.ArticleResource;
import com.test.feedreader.model.Article;
import com.test.feedreader.model.Feed;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by sdhruvakumar.
 */
public class ArticleResourceTest extends RestControllerTest {
    private Article article;

    @Before
    public void setup() {
        Feed feed = new Feed("test-feed", "feed-description");
        feed.setId(1L);
        article = new Article("article", feed);
        article.setId(1l);
    }

    @Test
    public void articleResourceShouldHaveSelfLink() {
        ArticleResource articleResource = ArticleResource.ASSEMBLER.toResource(article);
        assertThat(articleResource.hasLinks(), is(true));
        assertThat(articleResource.hasLink(Link.REL_SELF), is(true));
    }

}
