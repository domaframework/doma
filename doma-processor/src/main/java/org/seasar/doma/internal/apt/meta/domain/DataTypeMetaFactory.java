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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.DataTypeAnnot;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.def.TypeParametersDef;
import org.seasar.doma.internal.apt.meta.TypeElementMetaFactory;
import org.seasar.doma.message.Message;

public class DataTypeMetaFactory implements TypeElementMetaFactory<DataTypeMeta> {

  private final Context ctx;

  public DataTypeMetaFactory(Context ctx) {
    assertNotNull(ctx);
    this.ctx = ctx;
  }

  @Override
  public DataTypeMeta createTypeElementMeta(TypeElement typeElement) {
    assertNotNull(typeElement);
    DataTypeAnnot dataTypeAnnot = ctx.getAnnotations().newDataTypeAnnot(typeElement);
    if (dataTypeAnnot == null) {
      throw new AptIllegalStateException("dataTypeAnnot");
    }
    DataTypeMeta dataTypeMeta = new DataTypeMeta(typeElement, typeElement.asType(), dataTypeAnnot);
    validateTypeElement(typeElement, dataTypeMeta);
    doTypeParameters(typeElement, dataTypeMeta);
    doConstructor(typeElement, dataTypeMeta);
    return dataTypeMeta;
  }

  private void validateTypeElement(TypeElement typeElement, DataTypeMeta dataTypeMeta) {
    if (typeElement.getKind() != ElementKind.RECORD) {
      throw new AptException(Message.DOMA4449, typeElement, new Object[] {});
    }
    if (typeElement.getNestingKind().isNested()) {
      validateEnclosingElement(typeElement);
    }
  }

  void validateEnclosingElement(Element element) {
    TypeElement typeElement = ctx.getMoreElements().toTypeElement(element);
    if (typeElement == null) {
      return;
    }
    String simpleName = typeElement.getSimpleName().toString();
    if (simpleName.contains(Constants.BINARY_NAME_DELIMITER)
        || simpleName.contains(Constants.TYPE_NAME_DELIMITER)) {
      throw new AptException(
          Message.DOMA4450, typeElement, new Object[] {typeElement.getQualifiedName()});
    }
    NestingKind nestingKind = typeElement.getNestingKind();
    if (nestingKind == NestingKind.TOP_LEVEL) {
      //noinspection UnnecessaryReturnStatement
      return;
    } else if (nestingKind == NestingKind.MEMBER) {
      Set<Modifier> modifiers = typeElement.getModifiers();
      if (modifiers.containsAll(Arrays.asList(Modifier.STATIC, Modifier.PUBLIC))) {
        validateEnclosingElement(typeElement.getEnclosingElement());
      } else {
        throw new AptException(
            Message.DOMA4451, typeElement, new Object[] {typeElement.getQualifiedName()});
      }
    } else {
      throw new AptException(
          Message.DOMA4452, typeElement, new Object[] {typeElement.getQualifiedName()});
    }
  }

  public void doTypeParameters(TypeElement typeElement, DataTypeMeta dataTypeMeta) {
    TypeParametersDef typeParametersDef = ctx.getMoreElements().getTypeParametersDef(typeElement);
    dataTypeMeta.setTypeParametersDef(typeParametersDef);
  }

  private void doConstructor(TypeElement typeElement, DataTypeMeta dataTypeMeta) {
    List<ExecutableElement> constructors =
        ElementFilter.constructorsIn(typeElement.getEnclosedElements()).stream()
            .filter(c -> !c.getModifiers().contains(Modifier.PRIVATE))
            .filter(c -> c.getParameters().size() == 1)
            .toList();
    if (constructors.isEmpty()) {
      throw new AptException(Message.DOMA4453, typeElement, new Object[] {});
    }
    if (constructors.size() > 1) {
      throw new AptException(Message.DOMA4456, typeElement, new Object[] {});
    }
    ExecutableElement constructor = constructors.iterator().next();
    VariableElement param = constructor.getParameters().iterator().next();
    if (param == null) {
      throw new AptIllegalStateException("param is null");
    }
    TypeMirror type = param.asType();
    BasicCtType basicCtType = ctx.getCtTypes().newBasicCtType(type);
    if (basicCtType == null) {
      throw new AptException(Message.DOMA4454, param, new Object[] {type});
    }
    dataTypeMeta.setBasicCtType(basicCtType);
    dataTypeMeta.setAccessorMethod(param.getSimpleName().toString());
  }
}
