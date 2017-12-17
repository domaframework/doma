package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import org.seasar.doma.internal.apt.meta.TypeElementMeta;
import org.seasar.doma.internal.apt.reflection.EmbeddableReflection;

/**
 * @author nakamura-to
 *
 */
public class EmbeddableMeta implements TypeElementMeta {

    private final EmbeddableReflection embeddableReflection;

    private final TypeElement embeddableElement;

    private final List<EmbeddablePropertyMeta> propertyMetas = new ArrayList<>();

    private EmbeddableConstructorMeta constructorMeta;

    public EmbeddableMeta(EmbeddableReflection embeddableReflection,
            TypeElement embeddableElement) {
        assertNotNull(embeddableReflection, embeddableElement);
        this.embeddableReflection = embeddableReflection;
        this.embeddableElement = embeddableElement;
    }

    public EmbeddableReflection getEmbeddableReflection() {
        return embeddableReflection;
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

    public boolean isAbstract() {
        return embeddableElement.getModifiers().contains(Modifier.ABSTRACT);
    }

}
