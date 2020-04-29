package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.annot.EmbeddableDescAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public class EmbeddableDescMeta implements TypeElementMeta {

  private final EmbeddableDescAnnot embeddableDescAnnot;

  private final TypeElement typeElement;

  private final TypeMirror type;

  private final EmbeddableMeta embeddableMeta;

  private boolean error;

  public EmbeddableDescMeta(
      EmbeddableDescAnnot embeddableDescAnnot,
      TypeElement typeElement,
      EmbeddableMeta embeddableMeta) {
    assertNotNull(embeddableDescAnnot, typeElement, embeddableMeta);
    this.embeddableDescAnnot = embeddableDescAnnot;
    this.typeElement = typeElement;
    this.type = typeElement.asType();
    this.embeddableMeta = embeddableMeta;
  }

  public EmbeddableDescAnnot getEmbeddableDescAnnot() {
    return embeddableDescAnnot;
  }

  public EmbeddableMeta getEmbeddableMeta() {
    return embeddableMeta;
  }

  @Override
  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }
}
