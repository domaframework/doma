package org.seasar.doma.internal.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.entity.PreInsertContext;

/** @author taedium */
public class AbstractPreInsertContext<E> extends AbstractEntityListenerContext<E>
    implements PreInsertContext<E> {

  protected AbstractPreInsertContext(EntityType<E> entityType, Method method, Config config) {
    super(entityType, method, config);
  }
}
