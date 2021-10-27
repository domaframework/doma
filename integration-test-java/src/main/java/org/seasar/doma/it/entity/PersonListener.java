package org.seasar.doma.it.entity;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

public class PersonListener implements EntityListener<Person> {

  @Override
  public void preDelete(Person entity, PreDeleteContext<Person> context) {
    context.setNewEntity(newPerson(entity, "_preD"));
  }

  @Override
  public void preInsert(Person entity, PreInsertContext<Person> context) {
    context.setNewEntity(newPerson(entity, "_preI"));
  }

  @Override
  public void preUpdate(Person entity, PreUpdateContext<Person> context) {
    context.setNewEntity(newPerson(entity, "_preU"));
  }

  @Override
  public void postInsert(Person entity, PostInsertContext<Person> context) {
    context.setNewEntity(newPerson(entity, "_postI"));
  }

  @Override
  public void postUpdate(Person entity, PostUpdateContext<Person> context) {
    context.setNewEntity(newPerson(entity, "_postU"));
  }

  @Override
  public void postDelete(Person entity, PostDeleteContext<Person> context) {
    context.setNewEntity(newPerson(entity, "_postD"));
  }

  protected Person newPerson(Person p, String suffix) {
    return new Person(
        p.employeeId,
        p.employeeNo,
        p.employeeName + suffix,
        p.managerId,
        p.hiredate,
        p.salary,
        p.departmentId,
        p.addressId,
        p.version);
  }
}
