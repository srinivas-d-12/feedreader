package com.test.feedreader.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdhruvakumar.
 */

@Entity
@Table(name = "feeds")
public class Feed {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @ManyToMany(mappedBy = "feeds")
    private List<User> users = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feed")
    private List<Article> articles = new ArrayList<>();

    @Version
    private Long version;

    protected Feed() {

    }

    public Feed(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public Long getVersion() {
        return version;
    }
}
