package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.entity.*;

public class ListenerArgumentTypeIllegalEntityListener implements EntityListener<String> {

  @Override
  public void preDelete(String entity, PreDeleteContext<String> context) {}

  @Override
  public void preInsert(String entity, PreInsertContext<String> context) {}

  @Override
  public void preUpdate(String entity, PreUpdateContext<String> context) {}

  @Override
  public void postInsert(String entity, PostInsertContext<String> context) {}

  @Override
  public void postUpdate(String entity, PostUpdateContext<String> context) {}

  @Override
  public void postDelete(String entity, PostDeleteContext<String> context) {}
}
