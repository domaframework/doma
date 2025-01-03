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
package org.seasar.doma.internal.apt.meta.domain;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.def.TypeParametersDef;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public class ExternalDomainMeta implements TypeElementMeta {

  private final TypeElement converterElement;

  private final boolean isJdbcTypeProvider;

  private BasicCtType basicCtType;

  private TypeMirror valueType;

  private TypeElement typeElement;

  private TypeMirror type;

  private TypeParametersDef typeParametersDef;

  public ExternalDomainMeta(TypeElement converterElement, boolean isJdbcTypeProvider) {
    assertNotNull(converterElement);
    this.converterElement = converterElement;
    this.isJdbcTypeProvider = isJdbcTypeProvider;
  }

  public TypeElement getConverterElement() {
    return converterElement;
  }

  public boolean isJdbcTypeProvider() {
    return isJdbcTypeProvider;
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  public void setBasicCtType(BasicCtType basicCtType) {
    this.basicCtType = basicCtType;
  }

  public TypeMirror getValueType() {
    return valueType;
  }

  public void setValueType(TypeMirror valueType) {
    this.valueType = valueType;
  }

  public TypeElement getTypeElement() {
    return typeElement;
  }

  public void setTypeElement(TypeElement typeElement) {
    this.typeElement = typeElement;
  }

  public void setTypeParametersDef(TypeParametersDef typeParametersDef) {
    this.typeParametersDef = typeParametersDef;
  }

  public List<String> getTypeVariables() {
    return typeParametersDef.getTypeVariables();
  }

  public List<String> getTypeParameters() {
    return typeParametersDef.getTypeParameters();
  }

  public void setType(TypeMirror type) {
    this.type = type;
  }

  public TypeMirror asType() {
    return typeElement != null ? typeElement.asType() : type;
  }

  public boolean isParameterized() {
    return typeElement != null && !typeElement.getTypeParameters().isEmpty();
  }

  @Override
  public boolean isError() {
    return false;
  }
}
