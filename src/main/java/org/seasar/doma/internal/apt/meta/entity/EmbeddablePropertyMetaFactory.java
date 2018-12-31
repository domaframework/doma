package org.seasar.doma.internal.apt.meta.entity;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.message.Message;

public class EmbeddablePropertyMetaFactory {

  protected final Context ctx;

  public EmbeddablePropertyMetaFactory(Context ctx) {
    this.ctx = ctx;
  }

  public EmbeddablePropertyMeta createEmbeddablePropertyMeta(
      VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
    EmbeddablePropertyMeta embeddablePropertyMeta = new EmbeddablePropertyMeta(fieldElement, ctx);
    embeddablePropertyMeta.setName(fieldElement.getSimpleName().toString());
    CtType ctType = resolveCtType(fieldElement, fieldElement.asType(), embeddableMeta);
    embeddablePropertyMeta.setCtType(ctType);
    ColumnAnnot columnAnnot = ColumnAnnot.newInstance(fieldElement, ctx);
    if (columnAnnot != null) {
      embeddablePropertyMeta.setColumnAnnot(columnAnnot);
    }
    return embeddablePropertyMeta;
  }

  protected CtType resolveCtType(
      VariableElement fieldElement, TypeMirror type, EmbeddableMeta embeddableMeta) {

    final OptionalCtType optionalCtType = OptionalCtType.newInstance(type, ctx);
    if (optionalCtType != null) {
      if (optionalCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4299,
            fieldElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      if (optionalCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4301,
            fieldElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      return optionalCtType;
    }

    OptionalIntCtType optionalIntCtType = OptionalIntCtType.newInstance(type, ctx);
    if (optionalIntCtType != null) {
      return optionalIntCtType;
    }

    OptionalLongCtType optionalLongCtType = OptionalLongCtType.newInstance(type, ctx);
    if (optionalLongCtType != null) {
      return optionalLongCtType;
    }

    OptionalDoubleCtType optionalDoubleCtType = OptionalDoubleCtType.newInstance(type, ctx);
    if (optionalDoubleCtType != null) {
      return optionalDoubleCtType;
    }

    final DomainCtType domainCtType = DomainCtType.newInstance(type, ctx);
    if (domainCtType != null) {
      if (domainCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4295,
            fieldElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      if (domainCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4296,
            fieldElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      return domainCtType;
    }

    BasicCtType basicCtType = BasicCtType.newInstance(type, ctx);
    if (basicCtType != null) {
      return basicCtType;
    }

    final EmbeddableCtType embeddableCtType = EmbeddableCtType.newInstance(type, ctx);
    if (embeddableCtType != null) {
      throw new AptException(
          Message.DOMA4297,
          fieldElement,
          new Object[] {
            type,
            embeddableMeta.getEmbeddableElement().getQualifiedName(),
            fieldElement.getSimpleName()
          });
    }

    throw new AptException(
        Message.DOMA4298,
        fieldElement,
        new Object[] {
          type,
          embeddableMeta.getEmbeddableElement().getQualifiedName(),
          fieldElement.getSimpleName()
        });
  }
}
