package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.EmbeddableDescAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;

public class EmbeddableDescMetaFactory implements TypeElementMetaFactory<EmbeddableDescMeta> {

  private final Context ctx;

  public EmbeddableDescMetaFactory(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  @Override
  public EmbeddableDescMeta createTypeElementMeta(TypeElement typeElement) {
    assertNotNull(typeElement);
    EmbeddableDescAnnot embeddableDescAnnot =
        ctx.getAnnotations().newEmbeddableDescAnnot(typeElement);
    if (embeddableDescAnnot == null) {
      throw new AptIllegalStateException("embeddableDescAnnot must not be null.");
    }
    TypeMirror embeddableTypeMirror = embeddableDescAnnot.getValueValue();
    TypeElement embeddableTypeElement = ctx.getMoreTypes().toTypeElement(embeddableTypeMirror);
    if (embeddableTypeElement == null) {
      throw new AptIllegalStateException("embeddableTypeElement");
    }
    EmbeddableMetaFactory factory = new EmbeddableMetaFactory(ctx);
    EmbeddableMeta embeddableMeta = factory.createTypeElementMeta(embeddableTypeElement);
    return new EmbeddableDescMeta(embeddableDescAnnot, typeElement, embeddableMeta);
  }
}
