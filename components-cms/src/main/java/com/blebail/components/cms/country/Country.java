package com.blebail.components.cms.country;

import com.blebail.components.persistence.resource.PersistentResource;

import java.time.Instant;
import java.util.Objects;

public class Country extends PersistentResource<String> implements Comparable<Country> {

    public final String code;
    public final String name;
    
    public Country(String code, Instant creationDate, String name) {
        super(code, creationDate);
        this.code = Objects.requireNonNull(code);
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String toString() {
        return super.toStringHelper()
                .add("code", code)
                .add("name", name)
                .toString();
    }

    @Override
    public int compareTo(Country other) {
        if (this.equals(other)) {
            return 0;
        }

        if (this.name.equals(other.name)) {
            return code.compareTo(other.code);
        }

        return this.name.compareTo(other.name);
    }
}
