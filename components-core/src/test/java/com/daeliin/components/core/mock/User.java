//package com.daeliin.components.core.mock;
//
//import com.daeliin.components.domain.resource.UUIDPersistentResource;
//import javax.persistence.Entity;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//import org.hibernate.validator.constraints.NotBlank;
//
//@Getter
//@Setter
//@Entity
//@ToString(of = {"name"}, callSuper = true)
//public class User extends UUIDPersistentResource implements Comparable<User> {
//
//    private static final long serialVersionUID = 6434352024112491080L;
//
//    @NotBlank
//    private String name;
//
//    public User() {
//    }
//
//    public User withId(final Long id) {
//        this.id = id;
//        return this;
//    }
//
//    public User withName(final String name) {
//        this.name = name;
//        return this;
//    }
//
//    public User withUuid(final String uuid) {
//        this.uuid = uuid;
//        return this;
//    }
//
//    @Override
//    public int compareTo(User other) {
//        return this.getName().compareTo(other.getName());
//    }
//}
