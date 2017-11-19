package com.test.feedreader.controller.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.feedreader.controller.UserController;
import com.test.feedreader.model.User;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.Relation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.test.feedreader.controller.support.LinkBuilderWrapper.relativeRel;
import static com.test.feedreader.controller.support.LinkBuilderWrapper.relativeSelfRel;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by sdhruvakumar.
 */
@Relation(value = "user", collectionRelation = "users")
public class UserResource extends ResourceSupport {

    public static final Assembler ASSEMBLER = new Assembler();
    private static final String FEEDS = "feeds";
    private static final String ARTICLES = "articles";
    private final String uuid;
    private final String name;
    private final String email;


    public UserResource(User user) {
        this.uuid = user.getId().toString();
        this.name = user.getName();
        this.email = user.getEmail();

    }

    @JsonProperty("id")
    public String getUuid() {
        return this.uuid;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }


    public static class Assembler implements ResourceAssembler<User, UserResource> {

        private Assembler() {
        }

        public UserResource toResource(User user) {
            UserResource userResource = new UserResource(user);

            userResource.add(relativeSelfRel(linkTo(methodOn(UserController.class).getById(user.getId()))));
            userResource
                    .add(relativeRel(linkTo(methodOn(UserController.class).getById(user.getId())).slash(FEEDS), FEEDS));
            userResource
                    .add(relativeRel(linkTo(methodOn(UserController.class).getById(user.getId())).slash(ARTICLES), ARTICLES));
            return userResource;
        }
    }

    public static class CollectionAssembler
            implements ResourceAssembler<Collection<User>, Resources<UserResource>> {

        public CollectionAssembler() {
        }

        @Override
        public Resources<UserResource> toResource(Collection<User> users) {
            List<UserResource> resources =
                    users.stream().map(UserResource.ASSEMBLER::toResource).collect(Collectors.toList());
            return new Resources<>(resources);

        }
    }


}
