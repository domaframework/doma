package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

public class EmpListener implements EntityListener<Emp> {

  @Override
  public void preDelete(Emp entity, PreDeleteContext<Emp> context) {}

  @Override
  public void preInsert(Emp entity, PreInsertContext<Emp> context) {}

  @Override
  public void preUpdate(Emp entity, PreUpdateContext<Emp> context) {}

  @Override
  public void postInsert(Emp entity, PostInsertContext<Emp> context) {}

  @Override
  public void postUpdate(Emp entity, PostUpdateContext<Emp> context) {}

  @Override
  public void postDelete(Emp entity, PostDeleteContext<Emp> context) {}
}
