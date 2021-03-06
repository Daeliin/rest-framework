package com.blebail.components.persistence.fake;

import com.blebail.components.persistence.resource.service.CachedBaseService;
import com.blebail.components.persistence.sql.BUuidResource;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class UuidCachedBaseService extends CachedBaseService<UuidResource, BUuidResource, String, UuidCrudRepository> {

    @Inject
    public UuidCachedBaseService(UuidCrudRepository repository) {
        super(repository, new UuidResourceConversion());
    }
}
