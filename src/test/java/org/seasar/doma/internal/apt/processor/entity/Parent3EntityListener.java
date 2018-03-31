package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.entity.*;

public class Parent3EntityListener implements EntityListener<Parent3Entity> {

  @Override
  public void preInsert(Parent3Entity entity, PreInsertContext<Parent3Entity> context) {}

  @Override
  public void preUpdate(Parent3Entity entity, PreUpdateContext<Parent3Entity> context) {}

  @Override
  public void preDelete(Parent3Entity entity, PreDeleteContext<Parent3Entity> context) {}

  @Override
  public void postInsert(Parent3Entity entity, PostInsertContext<Parent3Entity> context) {}

  @Override
  public void postUpdate(Parent3Entity entity, PostUpdateContext<Parent3Entity> context) {}

  @Override
  public void postDelete(Parent3Entity entity, PostDeleteContext<Parent3Entity> context) {}
}
