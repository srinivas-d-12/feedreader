package com.test.feedreader.controller.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.feedreader.controller.FeedController;
import com.test.feedreader.model.Feed;
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
@Relation(value = "feed", collectionRelation = "feeds")
public class FeedResource extends ResourceSupport {

    public static final Assembler ASSEMBLER = new Assembler();
    private static final String ARTICLES = "articles";
    private final String uuid;
    private final String name;
    private final String description;


    public FeedResource(Feed feed) {
        this.uuid = feed.getId().toString();
        this.name = feed.getName();
        this.description = feed.getDescription();

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
    public String getDescription() {
        return description;
    }


    public static class Assembler implements ResourceAssembler<Feed, FeedResource> {

        private Assembler() {
        }

        public FeedResource toResource(Feed feed) {
            FeedResource feedResource = new FeedResource(feed);

            feedResource.add(relativeSelfRel(linkTo(methodOn(FeedController.class).getById(feed.getId()))));
            feedResource
                    .add(relativeRel(linkTo(methodOn(FeedController.class).getById(feed.getId())).slash(ARTICLES), ARTICLES));
            return feedResource;
        }
    }

    public static class CollectionAssembler
            implements ResourceAssembler<Collection<Feed>, Resources<FeedResource>> {

        public CollectionAssembler() {
        }

        @Override
        public Resources<FeedResource> toResource(Collection<Feed> feeds) {
            List<FeedResource> resources =
                    feeds.stream().map(FeedResource.ASSEMBLER::toResource).collect(Collectors.toList());
            return new Resources<>(resources);

        }
    }

}
