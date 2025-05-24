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
package org.seasar.doma.internal.apt.cttype;

import java.util.Objects;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.DataType;
import org.seasar.doma.Domain;
import org.seasar.doma.internal.ClassNames;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.RoundContext;

class InternalDomainCtTypeFactory {
  private final RoundContext ctx;

  InternalDomainCtTypeFactory(RoundContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
  }

  DomainCtType newDomainCtType(TypeMirror type, TypeElement typeElement, Domain domain) {
    var valueType = getValueType(domain);
    return newDomainCtType(type, typeElement, valueType);
  }

  private TypeMirror getValueType(Domain domain) {
    try {
      //noinspection ResultOfMethodCallIgnored
      domain.valueType();
    } catch (MirroredTypeException e) {
      return e.getTypeMirror();
    }
    throw new AptIllegalStateException("unreachable.");
  }

  DomainCtType newDataTypeCtType(TypeMirror type, TypeElement typeElement, DataType dataType) {
    var valueType = getValueType(typeElement, dataType);
    return newDomainCtType(type, typeElement, valueType);
  }

  private TypeMirror getValueType(TypeElement typeElement, DataType dataType) {
    var constructors =
        ElementFilter.constructorsIn(typeElement.getEnclosedElements()).stream()
            .filter(c -> !c.getModifiers().contains(Modifier.PRIVATE))
            .filter(c -> c.getParameters().size() == 1)
            .toList();
    if (constructors.size() != 1) {
      throw new AptIllegalStateException(
          String.format("%s : %d", typeElement.getQualifiedName(), constructors.size()));
    }
    var constructor = constructors.iterator().next();
    var param = constructor.getParameters().iterator().next();
    if (param == null) {
      throw new AptIllegalStateException("param is null");
    }
    return param.asType();
  }

  private DomainCtType newDomainCtType(
      TypeMirror type, TypeElement typeElement, TypeMirror valueType) {
    if (valueType == null) {
      return null;
    }
    var basicCtType = ctx.getCtTypes().newBasicCtType(valueType);
    if (basicCtType == null) {
      return null;
    }
    var declaredType = ctx.getMoreTypes().toDeclaredType(type);
    if (declaredType == null) {
      return null;
    }
    var typeArgCtTypes = ctx.getCtTypes().getAllTypeArguments(typeElement, declaredType);
    var binaryName = ctx.getMoreElements().getBinaryName(typeElement);
    var typeClassName = ClassNames.newDomainTypeClassName(binaryName);
    return new DomainCtType(ctx, type, basicCtType, typeArgCtTypes, typeClassName);
  }
}
