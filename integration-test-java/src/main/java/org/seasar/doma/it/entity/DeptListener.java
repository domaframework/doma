package org.seasar.doma.it.entity;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

public class DeptListener implements EntityListener<Dept> {

  @Override
  public void preDelete(Dept entity, PreDeleteContext<Dept> context) {
    Dept newEntity =
        new Dept(
            entity.departmentId,
            entity.departmentNo,
            entity.departmentName + "_preD",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  @Override
  public void preInsert(Dept entity, PreInsertContext<Dept> context) {
    Dept newEntity =
        new Dept(
            entity.departmentId,
            entity.departmentNo,
            entity.departmentName + "_preI",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  @Override
  public void preUpdate(Dept entity, PreUpdateContext<Dept> context) {
    Dept newEntity =
        new Dept(
            entity.departmentId,
            entity.departmentNo,
            entity.departmentName + "_preU",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  @Override
  public void postInsert(Dept entity, PostInsertContext<Dept> context) {
    Dept newEntity =
        new Dept(
            entity.departmentId,
            entity.departmentNo,
            entity.departmentName + "_postI",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  @Override
  public void postUpdate(Dept entity, PostUpdateContext<Dept> context) {
    Dept newEntity =
        new Dept(
            entity.departmentId,
            entity.departmentNo,
            entity.departmentName + "_postU",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }

  @Override
  public void postDelete(Dept entity, PostDeleteContext<Dept> context) {
    Dept newEntity =
        new Dept(
            entity.departmentId,
            entity.departmentNo,
            entity.departmentName + "_postD",
            entity.location,
            entity.version);
    context.setNewEntity(newEntity);
  }
}
