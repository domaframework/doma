package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.EntityDescAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public class EntityDescMeta implements TypeElementMeta {

  private final EntityDescAnnot entityDescAnnot;
  private final TypeElement typeElement;
  private final TypeMirror type;
  private final EntityMeta entityMeta;
  private boolean error;

  public EntityDescMeta(
      EntityDescAnnot entityDescAnnot, TypeElement typeElement, EntityMeta entityMeta) {
    assertNotNull(entityDescAnnot, typeElement, entityMeta);
    this.entityDescAnnot = entityDescAnnot;
    this.typeElement = typeElement;
    this.type = typeElement.asType();
    this.entityMeta = entityMeta;
  }

  public EntityMeta getEntityMeta() {
    return entityMeta;
  }

  @Override
  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }
}
