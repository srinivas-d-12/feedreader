package com.test.feedreader.controller;

import com.test.feedreader.FeedReaderConstants;
import com.test.feedreader.controller.resource.ArticleResource;
import com.test.feedreader.controller.resource.FeedResource;
import com.test.feedreader.exception.FeedNotFoundException;
import com.test.feedreader.model.Article;
import com.test.feedreader.model.Feed;
import com.test.feedreader.repository.ArticleRepository;
import com.test.feedreader.repository.FeedRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by sdhruvakumar.
 * <p>
 * This class is a rest controller for adding and listing feeds
 * It also has methods for adding articles to a feed.
 */
@RestController
@RequestMapping(value = FeedReaderConstants.FEED_CONTROLLER_ENDPOINT)
public class FeedController {

    @Autowired
    private FeedRespository feedRepository;

    @Autowired
    private ArticleRepository articleRepository;


    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<FeedResource>> list() {
        return ResponseEntity.ok(new FeedResource.CollectionAssembler().toResource(
                feedRepository.findAll()
        ));
    }

    @PostMapping
    public ResponseEntity<FeedResource> addFeed(@Valid @RequestBody Feed feedFromRequest) {
        final Feed feed = new Feed(feedFromRequest.getName(), feedFromRequest.getDescription());
        feedRepository.save(feed);
        return ResponseEntity.status(HttpStatus.CREATED).body(FeedResource.ASSEMBLER.toResource(feed));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<FeedResource> getById(@PathVariable Long id) {
        return feedRepository.
                findById(id)
                .map(feed -> ResponseEntity.ok(FeedResource.ASSEMBLER.toResource(feed)))
                .orElseThrow(() -> new FeedNotFoundException(id));

    }

    @PostMapping(value = "/{id}/articles", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<ArticleResource> addArticle(
            @PathVariable final Long id,
            @Valid @RequestBody final Article articleFromRequest) {

        return feedRepository.findById(id)
                .map(feed -> {
                    final Article article = new Article(articleFromRequest.getName(), feed);
                    articleRepository.save(article);
                    return ResponseEntity.status(HttpStatus.CREATED).body(ArticleResource.ASSEMBLER.toResource(article));
                }).orElseThrow(() -> new FeedNotFoundException(id));

    }

    @GetMapping(value = "/{id}/articles", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<ArticleResource>> listArticles(@PathVariable Long id) {

        return feedRepository.findById(id)
                .map(feed -> ResponseEntity.ok(new ArticleResource.CollectionAssembler().toResource(feed.getArticles())))
                .orElseThrow(() -> new FeedNotFoundException(id));
    }

}
