package org.seasar.doma.internal.apt.meta.entity;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
import org.seasar.doma.internal.apt.cttype.*;
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
    ColumnAnnot columnAnnot = ctx.getAnnotations().newColumnAnnot(fieldElement);
    if (columnAnnot != null) {
      embeddablePropertyMeta.setColumnAnnot(columnAnnot);
    }
    return embeddablePropertyMeta;
  }

  protected CtType resolveCtType(
      VariableElement fieldElement, TypeMirror type, EmbeddableMeta embeddableMeta) {
    return ctx.getCtTypes().newCtType(type, new CtTypeValidator(fieldElement, embeddableMeta));
  }

  private class CtTypeValidator extends SimpleCtTypeVisitor<Void, Void, AptException> {

    private final VariableElement fieldElement;

    private final EmbeddableMeta embeddableMeta;

    private CtTypeValidator(VariableElement fieldElement, EmbeddableMeta embeddableMeta) {
      this.fieldElement = fieldElement;
      this.embeddableMeta = embeddableMeta;
    }

    @Override
    protected Void defaultAction(CtType ctType, Void aVoid) throws AptException {
      throw new AptException(
          Message.DOMA4298,
          fieldElement,
          new Object[] {
            ctType.getType(),
            embeddableMeta.getEmbeddableElement().getQualifiedName(),
            fieldElement.getSimpleName()
          });
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void aVoid)
        throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType optionalCtType, Void aVoid) throws AptException {
      if (optionalCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4299,
            fieldElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      if (optionalCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4301,
            fieldElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType domainCtType, Void aVoid) throws AptException {
      if (domainCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4295,
            fieldElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      if (domainCtType.hasWildcard() || domainCtType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4296,
            fieldElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              embeddableMeta.getEmbeddableElement().getQualifiedName(),
              fieldElement.getSimpleName()
            });
      }
      return null;
    }

    @Override
    public Void visitEmbeddableCtType(EmbeddableCtType embeddableCtType, Void aVoid)
        throws AptException {
      throw new AptException(
          Message.DOMA4297,
          fieldElement,
          new Object[] {
            embeddableCtType.getType(),
            embeddableMeta.getEmbeddableElement().getQualifiedName(),
            fieldElement.getSimpleName()
          });
    }
  }
}
