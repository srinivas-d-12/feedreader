package com.test.feedreader.resource;

import com.test.feedreader.controller.RestControllerTest;
import com.test.feedreader.controller.resource.UserResource;
import com.test.feedreader.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Link;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by sdhruvakumar.
 */

public class UserResourceTest extends RestControllerTest {

    private User user;

    @Before
    public void setup() {
        user = new User("test", "test@email.com");
        user.setId(1L);
    }

    @Test
    public void userResourceShouldHaveSelfLink() {
        UserResource userResource = UserResource.ASSEMBLER.toResource(user);
        assertThat(userResource.hasLinks(), is(true));
        assertThat(userResource.hasLink(Link.REL_SELF), is(true));
    }


    @Test
    public void userResourceShouldHaveRelLinks() {
        Link feedLink = new Link("http://localhost/users/1/feeds", "feeds");
        Link articleLink = new Link("http://localhost/users/1/articles", "articles");
        UserResource userResource = UserResource.ASSEMBLER.toResource(user);
        assertThat(userResource.hasLinks(), is(true));
        assertThat(userResource.getLink("articles"), is(articleLink));
        assertThat(userResource.getLink("feeds"), is(feedLink));
    }

}
