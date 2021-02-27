package org.seasar.doma.internal.apt.processor.metamodel;

import org.seasar.doma.Entity;
import org.seasar.doma.Metamodel;

import java.time.LocalDateTime;

@Entity(metamodel = @Metamodel(scopes = ItemScope.class))
class Item {
    Long id;
    String name;
    LocalDateTime publishedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
}