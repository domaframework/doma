package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.EntityDescAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;

public class EntityDescMetaFactory implements TypeElementMetaFactory<EntityDescMeta> {

  private final Context ctx;

  public EntityDescMetaFactory(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  @Override
  public EntityDescMeta createTypeElementMeta(TypeElement typeElement) {
    assertNotNull(typeElement);
    EntityDescAnnot entityDescAnnot = ctx.getAnnotations().newEntityDescAnnot(typeElement);
    if (entityDescAnnot == null) {
      throw new AptIllegalStateException("entityDescAnnot must not null.");
    }
    TypeMirror entityTypeMirror = entityDescAnnot.getValueValue();
    TypeElement entityTypeElement = ctx.getMoreTypes().toTypeElement(entityTypeMirror);
    if (entityTypeElement == null) {
      throw new AptIllegalStateException("entityTypeElement");
    }
    EntityMeta entityMeta = new EntityMetaFactory(ctx).createTypeElementMeta(entityTypeElement);
    return new EntityDescMeta(entityDescAnnot, typeElement, entityMeta);
  }
}
