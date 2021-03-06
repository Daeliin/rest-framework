package com.blebail.components.thirdparty.google.search.html;

import org.springframework.web.util.UriComponentsBuilder;

public final class GoogleUrlBuilder {

    private UriComponentsBuilder uriBuilder;

    public GoogleUrlBuilder() {
        this.uriBuilder = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("google.com");
    }

    public GoogleUrlBuilder withPath(String path) {
        this.uriBuilder = this.uriBuilder.path("/" + path);

        return this;
    }

    public GoogleUrlBuilder withParam(String name, String value) {
        this.uriBuilder = this.uriBuilder.queryParam(name, value);

        return this;
    }

    public String build() {
        return this.uriBuilder.build()
            .encode()
            .toUriString();
    }
}