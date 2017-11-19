package com.test.feedreader.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by sdhruvakumar.
 */

@Entity
@Table(name = "articles")
public class Article {

    @NotNull
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "feed")
    private Feed feed;

    @Version
    private Long version;

    protected Article() {
    }

    public Article(String name, Feed feed) {
        this.name = name;
        this.feed = feed;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Feed getFeed() {
        return feed;
    }

    public Long getVersion() {
        return version;
    }
}
