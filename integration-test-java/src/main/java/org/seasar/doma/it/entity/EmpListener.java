package org.seasar.doma.it.entity;

import java.sql.Timestamp;
import java.util.Date;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

public class EmpListener<E extends Emp> implements EntityListener<E> {

  @Override
  public void preDelete(E entity, PreDeleteContext<E> context) {}

  @Override
  public void preInsert(E entity, PreInsertContext<E> context) {
    entity.setInsertTimestamp(new Timestamp(new Date().getTime()));
  }

  @Override
  public void preUpdate(E entity, PreUpdateContext<E> context) {
    if (context.isEntityChanged()) {
      entity.setUpdateTimestamp(new Timestamp(new Date().getTime()));
    }
  }

  @Override
  public void postInsert(E entity, PostInsertContext<E> context) {}

  @Override
  public void postUpdate(E entity, PostUpdateContext<E> context) {}

  @Override
  public void postDelete(E entity, PostDeleteContext<E> context) {}
}
