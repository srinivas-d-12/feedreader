package com.test.feedreader.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sdhruvakumar.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subscription", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "feed_id", referencedColumnName = "id"))
    private List<Feed> feeds = new ArrayList<>();


    @Version
    private Long version;


    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public Long getVersion() {
        return version;
    }


}