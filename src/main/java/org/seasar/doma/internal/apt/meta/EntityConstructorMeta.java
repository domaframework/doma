package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Collections;
import java.util.List;
import javax.lang.model.element.ExecutableElement;

/** @author nakamura-to */
public class EntityConstructorMeta {

  private final ExecutableElement constructorElement;
  private final List<EntityPropertyMeta> entityPropertyMetas;

  public EntityConstructorMeta(
      ExecutableElement constructorElement, List<EntityPropertyMeta> entityPropertyMetas) {
    assertNotNull(constructorElement, entityPropertyMetas);
    this.constructorElement = constructorElement;
    this.entityPropertyMetas = Collections.unmodifiableList(entityPropertyMetas);
  }

  public ExecutableElement getConstructorElement() {
    return constructorElement;
  }

  public List<EntityPropertyMeta> getEntityPropertyMetas() {
    return entityPropertyMetas;
  }
}
