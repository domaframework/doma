package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import org.seasar.doma.internal.apt.annot.EmbeddableAnnot;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public class EmbeddableMeta implements TypeElementMeta {

  protected final EmbeddableAnnot embeddableAnnot;

  protected final TypeElement embeddableElement;

  protected final List<EmbeddablePropertyMeta> propertyMetas = new ArrayList<>();

  protected EmbeddableConstructorMeta constructorMeta;

  protected boolean error;

  public EmbeddableMeta(EmbeddableAnnot embeddableAnnot, TypeElement embeddableElement) {
    assertNotNull(embeddableAnnot, embeddableElement);
    this.embeddableAnnot = embeddableAnnot;
    this.embeddableElement = embeddableElement;
  }

  public EmbeddableAnnot getEmbeddableAnnot() {
    return embeddableAnnot;
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
