package org.seasar.doma.internal.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.query.DuplicateKeyType;

public class AbstractPostInsertContext<E> extends AbstractEntityListenerContext<E>
    implements PostInsertContext<E> {

  private final DuplicateKeyType duplicateKeyType;

  protected AbstractPostInsertContext(
      EntityType<E> entityType, Method method, Config config, DuplicateKeyType duplicateKeyType) {
    super(entityType, method, config);
    this.duplicateKeyType = duplicateKeyType;
  }

  @Override
  public DuplicateKeyType getDuplicateKeyType() {
    return duplicateKeyType;
  }
}
