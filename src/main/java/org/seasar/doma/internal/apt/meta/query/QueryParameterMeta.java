package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.annotation.Annotation;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.BiFunctionCtType;
import org.seasar.doma.internal.apt.cttype.CollectorCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.FunctionCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.ReferenceCtType;
import org.seasar.doma.internal.apt.cttype.SelectOptionsCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.cttype.StreamCtType;
import org.seasar.doma.internal.apt.meta.MetaConstants;
import org.seasar.doma.message.Message;

public class QueryParameterMeta {

  protected final VariableElement element;

  protected final ExecutableElement methodElement;

  protected final TypeElement daoElement;

  protected final Context ctx;

  protected final String name;

  protected final String typeName;

  protected final TypeMirror type;

  protected final String qualifiedName;

  protected final CtType ctType;

  public QueryParameterMeta(VariableElement parameterElement, QueryMeta queryMeta, Context ctx) {
    assertNotNull(parameterElement, queryMeta, ctx);
    this.element = parameterElement;
    this.methodElement = queryMeta.getMethodElement();
    this.daoElement = queryMeta.getDaoElement();
    this.ctx = ctx;
    name = ctx.getElements().getParameterName(parameterElement);
    if (name.startsWith(MetaConstants.RESERVED_NAME_PREFIX)) {
      throw new AptException(
          Message.DOMA4025,
          parameterElement,
          new Object[] {
            MetaConstants.RESERVED_NAME_PREFIX,
            queryMeta.getDaoElement().getQualifiedName(),
            queryMeta.getMethodElement().getSimpleName()
          });
    }
    type = parameterElement.asType();
    typeName = ctx.getTypes().getTypeName(type);
    TypeElement typeElement = ctx.getTypes().toTypeElement(type);
    if (typeElement != null) {
      qualifiedName = typeElement.getQualifiedName().toString();
    } else {
      qualifiedName = typeName;
    }
    ctType =
        ctx.getCtTypes()
            .newCtType(parameterElement.asType(), new CtTypeValidator(parameterElement));
  }

  public VariableElement getElement() {
    return element;
  }

  public ExecutableElement getMethodElement() {
    return methodElement;
  }

  public TypeElement getDaoElement() {
    return daoElement;
  }

  public String getName() {
    return name;
  }

  public TypeMirror getType() {
    return type;
  }

  public String getTypeName() {
    return typeName;
  }

  public String getQualifiedName() {
    return qualifiedName;
  }

  public CtType getCtType() {
    return ctType;
  }

  public boolean isNullable() {
    return ctType.accept(new NullableCtTypeVisitor(false), null);
  }

  public boolean isBindable() {
    return ctType.accept(new BindableCtTypeVisitor(false), null);
  }

  public boolean isAnnotated(Class<? extends Annotation> annotationType) {
    return element.getAnnotation(annotationType) != null;
  }

  protected class NullableCtTypeVisitor
      extends SimpleCtTypeVisitor<Boolean, Void, RuntimeException> {

    public NullableCtTypeVisitor(boolean nullable) {
      super(nullable);
    }

    @Override
    public Boolean visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      return true;
    }
  }

  protected class BindableCtTypeVisitor
      extends SimpleCtTypeVisitor<Boolean, Void, RuntimeException> {

    public BindableCtTypeVisitor(boolean bindable) {
      super(bindable);
    }

    @Override
    public Boolean visitBasicCtType(BasicCtType ctType, Void p) throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitEmbeddableCtType(EmbeddableCtType ctType, Void p) throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitEntityCtType(EntityCtType ctType, Void p) throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitOptionalIntCtType(OptionalIntCtType ctType, Void p)
        throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitOptionalLongCtType(OptionalLongCtType ctType, Void p)
        throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
        throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitIterableCtType(IterableCtType ctType, Void p) throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitReferenceCtType(ReferenceCtType ctType, Void p) throws RuntimeException {
      return true;
    }

    @Override
    public Boolean visitAnyCtType(AnyCtType ctType, Void p) throws RuntimeException {
      return true;
    }
  }

  private class CtTypeValidator extends SimpleCtTypeVisitor<Void, Void, AptException> {

    private final VariableElement parameterElement;

    private CtTypeValidator(VariableElement parameterElement) {
      this.parameterElement = parameterElement;
    }

    @Override
    protected Void defaultAction(CtType ctType, Void aVoid) throws AptException {
      return null;
    }

    @Override
    public Void visitEntityCtType(EntityCtType ctType, Void aVoid) throws AptException {
      return super.visitEntityCtType(ctType, aVoid);
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void aVoid) throws AptException {
      return super.visitOptionalIntCtType(ctType, aVoid);
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void aVoid) throws AptException {
      return super.visitOptionalLongCtType(ctType, aVoid);
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void aVoid)
        throws AptException {
      return super.visitOptionalDoubleCtType(ctType, aVoid);
    }

    @Override
    public Void visitBasicCtType(BasicCtType ctType, Void aVoid) throws AptException {
      return super.visitBasicCtType(ctType, aVoid);
    }

    @Override
    public Void visitSelectOptionsCtType(SelectOptionsCtType ctType, Void aVoid)
        throws AptException {
      return super.visitSelectOptionsCtType(ctType, aVoid);
    }

    @Override
    public Void visitIterableCtType(IterableCtType iterableCtType, Void aVoid) throws AptException {
      if (iterableCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4159,
            parameterElement,
            new Object[] {daoElement.getQualifiedName(), methodElement.getSimpleName()});
      }
      if (iterableCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4160,
            parameterElement,
            new Object[] {daoElement.getQualifiedName(), methodElement.getSimpleName()});
      }
      iterableCtType
          .getElementCtType()
          .accept(new IterableElementCtTypeVisitor(parameterElement), null);
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType optionalCtType, Void aVoid) throws AptException {
      if (optionalCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4236,
            parameterElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (optionalCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4237,
            parameterElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      optionalCtType
          .getElementCtType()
          .accept(new OptionalElementCtTypeVisitor(parameterElement), null);
      return null;
    }

    @Override
    public Void visitDomainCtType(DomainCtType domainCtType, Void aVoid) throws AptException {
      if (domainCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4208,
            parameterElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (domainCtType.hasWildcard() || domainCtType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4209,
            parameterElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      return null;
    }

    @Override
    public Void visitFunctionCtType(FunctionCtType functionCtType, Void aVoid) throws AptException {
      if (functionCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4240,
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      if (functionCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4241,
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      functionCtType
          .getTargetCtType()
          .accept(new FunctionTargetCtTypeVisitor(parameterElement), null);
      return null;
    }

    @Override
    public Void visitCollectorCtType(CollectorCtType collectorCtType, Void aVoid)
        throws AptException {
      if (collectorCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4258,
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      if (collectorCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4259,
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      collectorCtType
          .getTargetCtType()
          .accept(new CollectorTargetCtTypeVisitor(parameterElement), null);
      return null;
    }

    @Override
    public Void visitReferenceCtType(ReferenceCtType referenceCtType, Void aVoid)
        throws AptException {
      if (referenceCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4108,
            parameterElement,
            new Object[] {daoElement.getQualifiedName(), methodElement.getSimpleName()});
      }
      if (referenceCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4112,
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      referenceCtType
          .getReferentCtType()
          .accept(new ReferenceReferentCtTypeVisitor(parameterElement), null);
      return null;
    }

    @Override
    public Void visitBiFunctionCtType(BiFunctionCtType biFunctionCtType, Void aVoid)
        throws AptException {
      if (biFunctionCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4438,
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      if (biFunctionCtType.hasWildcard()) {
        throw new AptException(
            Message.DOMA4439,
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      return null;
    }
  }

  protected class IterableElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected final VariableElement parameterElement;

    protected IterableElementCtTypeVisitor(VariableElement parameterElement) {
      this.parameterElement = parameterElement;
    }

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4212,
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4213,
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      return null;
    }
  }

  protected class OptionalElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected final VariableElement parameterElement;

    protected OptionalElementCtTypeVisitor(VariableElement parameterElement) {
      this.parameterElement = parameterElement;
    }

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4238,
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4239,
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      return null;
    }
  }

  protected class FunctionTargetCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected final VariableElement parameterElement;

    protected FunctionTargetCtTypeVisitor(VariableElement parameterElement) {
      this.parameterElement = parameterElement;
    }

    @Override
    public Void visitStreamCtType(StreamCtType ctType, Void p) throws RuntimeException {
      ctType.getElementCtType().accept(new StreamElementCtTypeVisitor(), p);
      return null;
    }

    protected class StreamElementCtTypeVisitor
        extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

      @Override
      public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
        if (ctType.isRaw()) {
          throw new AptException(
              Message.DOMA4242,
              parameterElement,
              new Object[] {
                ctType.getQualifiedName(),
                daoElement.getQualifiedName(),
                methodElement.getSimpleName()
              });
        }
        if (ctType.hasWildcard() || ctType.hasTypevar()) {
          throw new AptException(
              Message.DOMA4243,
              parameterElement,
              new Object[] {
                ctType.getQualifiedName(),
                daoElement.getQualifiedName(),
                methodElement.getSimpleName()
              });
        }
        return null;
      }
    }
  }

  protected class CollectorTargetCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected final VariableElement parameterElement;

    protected CollectorTargetCtTypeVisitor(VariableElement parameterElement) {
      this.parameterElement = parameterElement;
    }

    @Override
    public Void visitDomainCtType(DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4260,
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4261,
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      return null;
    }
  }

  protected class ReferenceReferentCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected final VariableElement parameterElement;

    protected ReferenceReferentCtTypeVisitor(VariableElement parameterElement) {
      this.parameterElement = parameterElement;
    }

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRaw()) {
        throw new AptException(
            Message.DOMA4218,
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.hasWildcard() || ctType.hasTypevar()) {
        throw new AptException(
            Message.DOMA4219,
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      return null;
    }
  }
}
