package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Scope;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Consumer;

class CreatedAtScope {

  @Scope
  public Consumer<WhereDeclaration> today(MultiScopeEntity_ e) {
    LocalDate now = LocalDate.now();
    return w -> {
      w.between(e.createdAt, now.atStartOfDay(), now.atTime(LocalTime.MAX));
    };
  }
}