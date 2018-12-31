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
          ctx.getEnv(),
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
    ctType = createCtType(parameterElement, ctx);
  }

  protected CtType createCtType(final VariableElement parameterElement, final Context ctx) {
    IterableCtType iterableCtType = IterableCtType.newInstance(type, ctx);
    if (iterableCtType != null) {
      if (iterableCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4159,
            ctx.getEnv(),
            parameterElement,
            new Object[] {daoElement.getQualifiedName(), methodElement.getSimpleName()});
      }
      if (iterableCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4160,
            ctx.getEnv(),
            parameterElement,
            new Object[] {daoElement.getQualifiedName(), methodElement.getSimpleName()});
      }
      iterableCtType
          .getElementCtType()
          .accept(new IterableElementCtTypeVisitor(parameterElement), null);
      return iterableCtType;
    }

    EntityCtType entityCtType = EntityCtType.newInstance(type, ctx);
    if (entityCtType != null) {
      return entityCtType;
    }

    OptionalCtType optionalCtType = OptionalCtType.newInstance(type, ctx);
    if (optionalCtType != null) {
      if (optionalCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4236,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              optionalCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (optionalCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4237,
            ctx.getEnv(),
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
            Message.DOMA4208,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (domainCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4209,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              domainCtType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      return domainCtType;
    }

    BasicCtType basicCtType = BasicCtType.newInstance(type, ctx);
    if (basicCtType != null) {
      return basicCtType;
    }

    SelectOptionsCtType selectOptionsCtType = SelectOptionsCtType.newInstance(type, ctx);
    if (selectOptionsCtType != null) {
      return selectOptionsCtType;
    }

    FunctionCtType functionCtType = FunctionCtType.newInstance(type, ctx);
    if (functionCtType != null) {
      if (functionCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4240,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      if (functionCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4241,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      functionCtType
          .getTargetCtType()
          .accept(new FunctionTargetCtTypeVisitor(parameterElement), null);
      return functionCtType;
    }

    CollectorCtType collectorCtType = CollectorCtType.newInstance(type, ctx);
    if (collectorCtType != null) {
      if (collectorCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4258,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      if (collectorCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4259,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      collectorCtType
          .getTargetCtType()
          .accept(new CollectorTargetCtTypeVisitor(parameterElement), null);
      return collectorCtType;
    }

    ReferenceCtType referenceCtType = ReferenceCtType.newInstance(type, ctx);
    if (referenceCtType != null) {
      if (referenceCtType.isRaw()) {
        throw new AptException(
            Message.DOMA4108,
            ctx.getEnv(),
            parameterElement,
            new Object[] {daoElement.getQualifiedName(), methodElement.getSimpleName()});
      }
      if (referenceCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4112,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      referenceCtType
          .getReferentCtType()
          .accept(new ReferenceReferentCtTypeVisitor(parameterElement), null);
      return referenceCtType;
    }

    BiFunctionCtType biFunctionCtType = BiFunctionCtType.newInstance(type, ctx);
    if (biFunctionCtType != null) {
      if (biFunctionCtType.isRawType()) {
        throw new AptException(
            Message.DOMA4438,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      if (biFunctionCtType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4439,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              qualifiedName, daoElement.getQualifiedName(), methodElement.getSimpleName()
            });
      }
      return biFunctionCtType;
    }

    return AnyCtType.newInstance(type, ctx);
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

  protected class IterableElementCtTypeVisitor
      extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    protected final VariableElement parameterElement;

    protected IterableElementCtTypeVisitor(VariableElement parameterElement) {
      this.parameterElement = parameterElement;
    }

    @Override
    public Void visitDomainCtType(final DomainCtType ctType, Void p) throws RuntimeException {
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4212,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4213,
            ctx.getEnv(),
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
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4238,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4239,
            ctx.getEnv(),
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
        if (ctType.isRawType()) {
          throw new AptException(
              Message.DOMA4242,
              ctx.getEnv(),
              parameterElement,
              new Object[] {
                ctType.getQualifiedName(),
                daoElement.getQualifiedName(),
                methodElement.getSimpleName()
              });
        }
        if (ctType.isWildcardType()) {
          throw new AptException(
              Message.DOMA4243,
              ctx.getEnv(),
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
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4260,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4261,
            ctx.getEnv(),
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
      if (ctType.isRawType()) {
        throw new AptException(
            Message.DOMA4218,
            ctx.getEnv(),
            parameterElement,
            new Object[] {
              ctType.getQualifiedName(),
              daoElement.getQualifiedName(),
              methodElement.getSimpleName()
            });
      }
      if (ctType.isWildcardType()) {
        throw new AptException(
            Message.DOMA4219,
            ctx.getEnv(),
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
