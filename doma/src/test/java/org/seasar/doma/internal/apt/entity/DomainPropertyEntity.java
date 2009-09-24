package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.Domain;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;

@Entity
public class DomainPropertyEntity {

    @Id
    Integer id;

    Name name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    @Domain(valueType = String.class)
    public static class Name {
        private final String value;

        public Name(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
