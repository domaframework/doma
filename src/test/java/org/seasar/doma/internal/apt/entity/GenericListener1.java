package org.seasar.doma.internal.apt.entity;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

/** @author taedium */
public class GenericListener1<T> implements EntityListener<T> {

  @Override
  public void preInsert(T entity, PreInsertContext<T> context) {}

  @Override
  public void preUpdate(T entity, PreUpdateContext<T> context) {}

  @Override
  public void preDelete(T entity, PreDeleteContext<T> context) {}

  @Override
  public void postInsert(T entity, PostInsertContext<T> context) {}

  @Override
  public void postUpdate(T entity, PostUpdateContext<T> context) {}

  @Override
  public void postDelete(T entity, PostDeleteContext<T> context) {}
}
