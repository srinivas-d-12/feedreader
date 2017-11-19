package com.test.feedreader.controller;

import com.test.feedreader.FeedReaderConstants;
import com.test.feedreader.controller.resource.ArticleResource;
import com.test.feedreader.exception.ArticleNotFoundException;
import com.test.feedreader.repository.ArticleRepository;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sdhruvakumar.
 *
 * This class is a rest controller for listing articles
 */
@RestController
@RequestMapping(value = FeedReaderConstants.ARTICLE_CONTROLLER_ENDPOINT)
public class ArticleController {


    final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<ArticleResource>> list() {

        return ResponseEntity.ok(new ArticleResource.CollectionAssembler().
                toResource(articleRepository.findAll()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<ArticleResource> getById(@PathVariable Long id) {
        return articleRepository.
                findById(id)
                .map(article -> ResponseEntity.ok(ArticleResource.ASSEMBLER.toResource(article)))
                .orElseThrow(() -> new ArticleNotFoundException(id));
    }

}