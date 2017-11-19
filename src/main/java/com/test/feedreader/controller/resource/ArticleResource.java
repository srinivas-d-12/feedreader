package com.test.feedreader.controller.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.feedreader.controller.ArticleController;
import com.test.feedreader.model.Article;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.Relation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.test.feedreader.controller.support.LinkBuilderWrapper.relativeSelfRel;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by sdhruvakumar.
 */
@Relation(value = "article", collectionRelation = "articles")
public class ArticleResource extends ResourceSupport {

    public static final Assembler ASSEMBLER = new Assembler();
    private final String uuid;
    private final String name;


    public ArticleResource(Article article) {
        this.uuid = article.getId().toString();
        this.name = article.getName();
    }

    @JsonProperty("id")
    public String getUuid() {
        return this.uuid;
    }

    @JsonProperty
    public String getName() {
        return name;
    }


    public static class Assembler implements ResourceAssembler<Article, ArticleResource> {

        private Assembler() {
        }

        public ArticleResource toResource(Article article) {
            ArticleResource articleResource = new ArticleResource(article);

            articleResource.add(relativeSelfRel(linkTo(methodOn(ArticleController.class).getById(article.getId()))));
            return articleResource;
        }
    }

    public static class CollectionAssembler
            implements ResourceAssembler<Collection<Article>, Resources<ArticleResource>> {

        public CollectionAssembler() {
        }

        @Override
        public Resources<ArticleResource> toResource(Collection<Article> articles) {
            List<ArticleResource> resources =
                    articles.stream().map(ArticleResource.ASSEMBLER::toResource).collect(Collectors.toList());
            return new Resources<>(resources);

        }
    }

}
