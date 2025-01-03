/*
 * Copyright Doma Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.seasar.doma.internal.apt.meta.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.annotation.Annotation;
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

  public QueryParameterMeta(String name, CtType ctType, VariableElement parameterElement) {
    assertNotNull(name, ctType, parameterElement);
    this.name = name;
    this.ctType = ctType;
    this.element = parameterElement;
  }

  public VariableElement getElement() {
    return element;
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
    return ctType.accept(new NullableCtTypeVisitor(), null);
  }

  public boolean isBindable() {
    return ctType.accept(new BindableCtTypeVisitor(), null);
  }

  public boolean isAnnotated(Class<? extends Annotation> annotationType) {
    return element.getAnnotation(annotationType) != null;
  }

  static class NullableCtTypeVisitor extends SimpleCtTypeVisitor<Boolean, Void, RuntimeException> {

    NullableCtTypeVisitor() {
      super(false);
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

  static class BindableCtTypeVisitor extends SimpleCtTypeVisitor<Boolean, Void, RuntimeException> {

    BindableCtTypeVisitor() {
      super(false);
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
