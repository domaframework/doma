package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Collections;
import java.util.List;
import javax.lang.model.element.ExecutableElement;

/** @author nakamura-to */
public class EmbeddableConstructorMeta {

  private final ExecutableElement constructorElement;
  private final List<EmbeddablePropertyMeta> embeddablePropertyMetas;

  public EmbeddableConstructorMeta(
      ExecutableElement constructorElement, List<EmbeddablePropertyMeta> embeddablePropertyMetas) {
    assertNotNull(constructorElement, embeddablePropertyMetas);
    this.constructorElement = constructorElement;
    this.embeddablePropertyMetas = Collections.unmodifiableList(embeddablePropertyMetas);
  }

  public ExecutableElement getConstructorElement() {
    return constructorElement;
  }

  public List<EmbeddablePropertyMeta> getEmbeddablePropertyMetas() {
    return embeddablePropertyMetas;
  }
}
