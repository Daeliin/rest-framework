package com.blebail.components.webservices.fake;

import com.blebail.components.persistence.resource.PersistentResource;

import java.time.Instant;

public class UuidResource extends PersistentResource<String> implements Comparable<UuidResource> {

    public final String label;

    public UuidResource(String id, Instant creationDate, String label) {
        super(id, creationDate);
        this.label = label;
    }

    @Override
    public int compareTo(UuidResource other) {
        return id().compareTo(other.id());
    }
}
