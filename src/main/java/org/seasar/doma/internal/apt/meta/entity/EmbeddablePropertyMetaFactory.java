package org.seasar.doma.internal.apt.meta.entity;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.ColumnAnnot;
import org.seasar.doma.internal.apt.cttype.*;
import org.seasar.doma.message.Message;

public class EmbeddablePropertyMetaFactory {

  protected final Context ctx;

  protected EmbeddableMeta embeddableMeta;

  protected VariableElement fieldElement;

  public EmbeddablePropertyMetaFactory(
      Context ctx, EmbeddableMeta embeddableMeta, VariableElement fieldElement) {
    assertNotNull(ctx, embeddableMeta, fieldElement);
    this.ctx = ctx;
    this.embeddableMeta = embeddableMeta;
    this.fieldElement = fieldElement;
  }

  public EmbeddablePropertyMeta createEmbeddablePropertyMeta() {
    CtType ctType = ctx.getCtTypes().newCtType(fieldElement.asType(), new CtTypeValidator());
    EmbeddablePropertyMeta embeddablePropertyMeta = new EmbeddablePropertyMeta(ctType);
    embeddablePropertyMeta.setName(fieldElement.getSimpleName().toString());
    ColumnAnnot columnAnnot = ctx.getAnnotations().newColumnAnnot(fieldElement);
    if (columnAnnot != null) {
      embeddablePropertyMeta.setColumnAnnot(columnAnnot);
    }
    return embeddablePropertyMeta;
  }

  private class CtTypeValidator extends SimpleCtTypeVisitor<Void, Void, AptException> {

    @Override
    protected Void defaultAction(CtType ctType, Void aVoid) throws AptException {
      throw new AptException(Message.DOMA4298, fieldElement, new Object[] {ctType.getType()});
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
            Message.DOMA4299, fieldElement, new Object[] {optionalCtType.getQualifiedName()});
      }
      if (optionalCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4301, fieldElement, new Object[] {optionalCtType.getQualifiedName()});
      }
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType domainCtType, Void aVoid) throws AptException {
      if (domainCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4295, fieldElement, new Object[] {domainCtType.getQualifiedName()});
      }
      if (domainCtType.hasWildcard() || domainCtType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4296, fieldElement, new Object[] {domainCtType.getQualifiedName()});
      }
      return null;
    }

    @Override
    public Void visitEmbeddableCtType(EmbeddableCtType embeddableCtType, Void aVoid)
        throws AptException {
      throw new AptException(
          Message.DOMA4297, fieldElement, new Object[] {embeddableCtType.getType()});
    }
  }
}
