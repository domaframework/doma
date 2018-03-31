package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

public class GenericListener8<E> implements GenericListener7<E, String> {

  @Override
  public void preInsert(String entity, PreInsertContext<String> context) {}

  @Override
  public void preUpdate(String entity, PreUpdateContext<String> context) {}

  @Override
  public void preDelete(String entity, PreDeleteContext<String> context) {}

  @Override
  public void postInsert(String entity, PostInsertContext<String> context) {}

  @Override
  public void postUpdate(String entity, PostUpdateContext<String> context) {}

  @Override
  public void postDelete(String entity, PostDeleteContext<String> context) {}
}
