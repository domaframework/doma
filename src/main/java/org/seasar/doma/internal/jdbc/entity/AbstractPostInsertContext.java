package org.seasar.doma.internal.jdbc.entity;

import java.lang.reflect.Method;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.entity.PostInsertContext;

/** @author taedium */
public class AbstractPostInsertContext<E> extends AbstractEntityListenerContext<E>
    implements PostInsertContext<E> {

  protected AbstractPostInsertContext(EntityDesc<E> entityDesc, Method method, Config config) {
    super(entityDesc, method, config);
  }
}
