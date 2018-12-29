package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.mirror.EmbeddableMirror;

public class EmbeddableMeta implements TypeElementMeta {

  protected final EmbeddableMirror embeddableMirror;

  protected final TypeElement embeddableElement;

  protected final List<EmbeddablePropertyMeta> propertyMetas = new ArrayList<>();

  protected EmbeddableConstructorMeta constructorMeta;

  protected boolean error;

  public EmbeddableMeta(EmbeddableMirror embeddableMirror, TypeElement embeddableElement) {
    assertNotNull(embeddableMirror, embeddableElement);
    this.embeddableMirror = embeddableMirror;
    this.embeddableElement = embeddableElement;
  }

  public EmbeddableMirror getEmbeddableMirror() {
    return embeddableMirror;
  }

  public TypeElement getEmbeddableElement() {
    return embeddableElement;
  }

  public void addEmbeddablePropertyMeta(EmbeddablePropertyMeta propertyMeta) {
    propertyMetas.add(propertyMeta);
  }

  public List<EmbeddablePropertyMeta> getEmbeddablePropertyMetas() {
    if (constructorMeta != null) {
      return constructorMeta.getEmbeddablePropertyMetas();
    }
    return propertyMetas;
  }

  public void setConstructorMeta(EmbeddableConstructorMeta constructorMeta) {
    this.constructorMeta = constructorMeta;
  }

  @Override
  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }

  public boolean isAbstract() {
    return embeddableElement.getModifiers().contains(Modifier.ABSTRACT);
  }
}
