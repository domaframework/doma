package org.seasar.doma.internal.apt.processor.entity;

import java.sql.Date;
import org.seasar.doma.jdbc.entity.*;

/** @author taedium */
public class GenericListener4<E extends Date> implements EntityListener<E> {

  @Override
  public void preInsert(E entity, PreInsertContext<E> context) {}

  @Override
  public void preUpdate(E entity, PreUpdateContext<E> context) {}

  @Override
  public void preDelete(E entity, PreDeleteContext<E> context) {}

  @Override
  public void postInsert(E entity, PostInsertContext<E> context) {}

  @Override
  public void postUpdate(E entity, PostUpdateContext<E> context) {}

  @Override
  public void postDelete(E entity, PostDeleteContext<E> context) {}
}
