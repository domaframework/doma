package org.seasar.doma.internal.jdbc.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityDesc;

public abstract class AbstractEntityListenerContext<E> {

  protected final EntityDesc<E> entityDesc;

  protected final Method method;

  protected final Config config;

  protected E newEntity;

  protected AbstractEntityListenerContext(EntityDesc<E> entityDesc, Method method, Config config) {
    assertNotNull(entityDesc, method, config);
    this.entityDesc = entityDesc;
    this.method = method;
    this.config = config;
  }

  protected boolean isPropertyDefinedInternal(String propertyName) {
    assertNotNull(propertyName);
    return entityDesc.getEntityPropertyDesc(propertyName) != null;
  }

  public EntityDesc<E> getEntityDesc() {
    return entityDesc;
  }

  public Method getMethod() {
    return method;
  }

  public Config getConfig() {
    return config;
  }

  public E getNewEntity() {
    return this.newEntity;
  }

  public void setNewEntity(E newEntity) {
    if (newEntity == null) {
      throw new DomaNullPointerException("newEntity");
    }
    this.newEntity = newEntity;
  }
}
