package com.daeliin.framework.commons.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Resource saved in a RDBMS and identified by a numeric id, equality is only based on id.
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@MappedSuperclass
public abstract class LongIdPersistentResource implements PersistentResource<Long> {
    
    @Id
    @GeneratedValue
    protected Long id;
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    @Override
    public void setId(final Long id) {
        this.id = id;
    }
}
