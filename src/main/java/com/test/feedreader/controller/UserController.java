package com.test.feedreader.controller;


import com.test.feedreader.FeedReaderConstants;
import com.test.feedreader.controller.resource.ArticleResource;
import com.test.feedreader.controller.resource.FeedResource;
import com.test.feedreader.controller.resource.UserResource;
import com.test.feedreader.exception.FeedNotFoundException;
import com.test.feedreader.exception.UserNotFoundException;
import com.test.feedreader.model.Article;
import com.test.feedreader.model.Feed;
import com.test.feedreader.model.User;
import com.test.feedreader.repository.FeedRespository;
import com.test.feedreader.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by sdhruvakumar.
 *
 * This class is a rest controller for adding and listing users
 * It also has methods for users to subscribe/ unsubscribe to a feed
 *
 */


@RestController
@RequestMapping(value = FeedReaderConstants.USER_CONTROLLER_ENDPOINT)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedRespository feedRespository;


    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<UserResource>> list() {
        return ResponseEntity.ok(new UserResource.CollectionAssembler().toResource(
                userRepository.findAll()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<UserResource> getById(@PathVariable Long id) {

        return userRepository.
                findById(id)
                .map(user -> ResponseEntity.ok(UserResource.ASSEMBLER.toResource(user)))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<UserResource> addUser(@Valid @RequestBody User userFromRequest) {
        final User user = new User(userFromRequest.getName(), userFromRequest.getEmail());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResource.ASSEMBLER.toResource(user));
    }

    @PutMapping(value = "/{id}/feeds")
    public ResponseEntity<FeedResource> subscribe(
            @PathVariable final Long id,
            @RequestBody final Feed feedFromRequest) {

        return userRepository.findById(id).map(
                user ->
                        feedRespository
                                .findById(feedFromRequest.getId())
                                .map(feed -> {
                                    user.getFeeds().add(feed);
                                    userRepository.save(user);
                                    return ResponseEntity.ok(FeedResource.ASSEMBLER.toResource(feed));
                                }).orElseThrow(() -> new FeedNotFoundException(feedFromRequest.getId()))
        ).orElseThrow(() -> new UserNotFoundException(id));

    }

    @DeleteMapping(value = "/{id}/feeds")
    public ResponseEntity unsubscribe(
            @PathVariable final Long id,
            @RequestBody final Feed feedFromRequest) {

        return userRepository.findById(id).map(
                user ->
                        feedRespository
                                .findById(feedFromRequest.getId())
                                .map(feed -> {
                                    user.getFeeds().remove(feed);
                                    userRepository.save(user);
                                    return ResponseEntity.noContent().build();
                                }).orElseThrow(() -> new FeedNotFoundException(feedFromRequest.getId()))
        ).orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping(value = "/{id}/feeds", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<FeedResource>> listFeeds(@PathVariable Long id) {

        return userRepository.
                findById(id)
                .map(user -> ResponseEntity.ok(new FeedResource.CollectionAssembler().toResource(user.getFeeds())))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping(value = "/{id}/articles", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Resources<ArticleResource>> listArticles(@PathVariable Long id) {

        Collection<Feed> feeds = userRepository.
                findById(id)
                .map(user -> user.getFeeds().stream().collect(Collectors.toList()))
                .orElseThrow(() -> new UserNotFoundException(id));

        Collection<Article> articles = feeds.stream().
                map(feed -> feed.getArticles())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ArticleResource.CollectionAssembler().toResource(articles));

    }


}
