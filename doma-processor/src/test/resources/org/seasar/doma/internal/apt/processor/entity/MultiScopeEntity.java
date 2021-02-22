package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Metamodel;

import java.time.LocalDateTime;

@Entity(metamodel = @Metamodel(scopes = {CreatedAtScope.class, NameScope.class}))
class MultiScopeEntity {
  Long id;
  String name;
  LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setName(String name) {
    this.name = name;
  }
}