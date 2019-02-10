package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.annotation.Annotation;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.AnyCtType;
import org.seasar.doma.internal.apt.cttype.ArrayCtType;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.DomainCtType;
import org.seasar.doma.internal.apt.cttype.EmbeddableCtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.ReferenceCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;

public class QueryParameterMeta {

  private final String name;

  private final CtType ctType;

  private final VariableElement element;

  private final ExecutableElement methodElement;

  private final TypeElement daoElement;

  public QueryParameterMeta(
      String name,
      CtType ctType,
      VariableElement parameterElement,
      ExecutableElement methodElement,
      TypeElement daoElement) {
    assertNotNull(name, ctType, parameterElement, methodElement, daoElement);
    this.name = name;
    this.ctType = ctType;
    this.element = parameterElement;
    this.methodElement = methodElement;
    this.daoElement = daoElement;
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
    return ctType.getType();
  }

  public String getQualifiedName() {
    return ctType.getQualifiedName();
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
    public Boolean visitArrayCtType(ArrayCtType ctType, Void p) throws RuntimeException {
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
}
