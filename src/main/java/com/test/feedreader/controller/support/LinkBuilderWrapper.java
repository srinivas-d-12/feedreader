package com.test.feedreader.controller.support;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;


/**
 * Created by sdhruvakumar.
 */
public class LinkBuilderWrapper {

    public static Link relativeRel(LinkBuilder wrapped, String rel) {
        return new Link(wrapped.toString(), rel);
    }

    public static Link relativeSelfRel(LinkBuilder wrapped) {
        return relativeRel(wrapped, Link.REL_SELF);
    }

}