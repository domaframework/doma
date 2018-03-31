package org.seasar.doma.internal.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.PreDeleteContext;

public class AbstractPreDeleteContext<E> extends AbstractEntityListenerContext<E>
    implements PreDeleteContext<E> {

  protected AbstractPreDeleteContext(EntityDesc<E> entityDesc, Method method, Config config) {
    super(entityDesc, method, config);
  }
}
