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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.OptionalCtType;
import org.seasar.doma.internal.apt.cttype.OptionalDoubleCtType;
import org.seasar.doma.internal.apt.cttype.OptionalIntCtType;
import org.seasar.doma.internal.apt.cttype.OptionalLongCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.internal.apt.def.TypeParametersDef;
import org.seasar.doma.jdbc.query.Query;

public abstract class AbstractQueryMeta implements QueryMeta {

  private final String name;

  private final ExecutableElement methodElement;

  private final TypeElement daoElement;

  private final List<TypeMirror> thrownTypes = new ArrayList<>();

  QueryKind queryKind;

  private final LinkedHashMap<String, TypeMirror> bindableParameterTypeMap = new LinkedHashMap<>();

  private QueryReturnMeta returnMeta;

  private final List<QueryParameterMeta> parameterMetas = new ArrayList<>();

  private final List<String> fileNames = new ArrayList<>();

  private TypeParametersDef typeParametersDef;

  AbstractQueryMeta(TypeElement daoElement, ExecutableElement methodElement) {
    assertNotNull(daoElement, methodElement);
    this.daoElement = daoElement;
    this.methodElement = methodElement;
    this.name = methodElement.getSimpleName().toString();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public ExecutableElement getMethodElement() {
    return methodElement;
  }

  @Override
  public TypeElement getDaoElement() {
    return daoElement;
  }

  @Override
  public List<String> getTypeParameterNames() {
    return typeParametersDef.getTypeParameters();
  }

  public void setTypeParametersDef(TypeParametersDef typeParametersDef) {
    this.typeParametersDef = typeParametersDef;
  }

  @Override
  public List<TypeMirror> getThrownTypes() {
    return thrownTypes;
  }

  public void addThrownType(TypeMirror thrownType) {
    thrownTypes.add(thrownType);
  }

  public Class<? extends Query> getQueryClass() {
    if (queryKind == null) {
      return null;
    }
    return queryKind.getQueryClass();
  }

  public Class<?> getCommandClass() {
    if (queryKind == null) {
      return null;
    }
    return queryKind.getCommandClass();
  }

  @Override
  public QueryKind getQueryKind() {
    return queryKind;
  }

  public void setQueryKind(QueryKind queryKind) {
    this.queryKind = queryKind;
  }

  @Override
  public LinkedHashMap<String, TypeMirror> getBindableParameterTypeMap() {
    return bindableParameterTypeMap;
  }

  public void addBindableParameterCtType(
      final String parameterName, CtType bindableParameterCtType) {
    bindableParameterCtType.accept(new BindableParameterCtTypeVisitor(parameterName), null);
  }

  @Override
  public QueryReturnMeta getReturnMeta() {
    return returnMeta;
  }

  public void setReturnMeta(QueryReturnMeta returnMeta) {
    this.returnMeta = returnMeta;
  }

  @Override
  public List<QueryParameterMeta> getParameterMetas() {
    return parameterMetas;
  }

  public void addParameterMeta(QueryParameterMeta queryParameterMeta) {
    this.parameterMetas.add(queryParameterMeta);
  }

  @Override
  public List<String> getFileNames() {
    return fileNames;
  }

  public void addFileName(String fileName) {
    this.fileNames.add(fileName);
  }

  @Override
  public boolean isVarArgs() {
    return this.methodElement.isVarArgs();
  }

  class BindableParameterCtTypeVisitor extends SimpleCtTypeVisitor<Void, Void, RuntimeException> {

    final String parameterName;

    BindableParameterCtTypeVisitor(String parameterName) {
      this.parameterName = parameterName;
    }

    @Override
    protected Void defaultAction(CtType ctType, Void p) throws RuntimeException {
      bindableParameterTypeMap.put(parameterName, ctType.getType());
      return null;
    }

    @Override
    public Void visitOptionalCtType(OptionalCtType ctType, Void p) throws RuntimeException {
      return ctType.getElementCtType().accept(this, p);
    }

    @Override
    public Void visitOptionalIntCtType(OptionalIntCtType ctType, Void p) throws RuntimeException {
      return ctType.getElementCtType().accept(this, p);
    }

    @Override
    public Void visitOptionalLongCtType(OptionalLongCtType ctType, Void p) throws RuntimeException {
      return ctType.getElementCtType().accept(this, p);
    }

    @Override
    public Void visitOptionalDoubleCtType(OptionalDoubleCtType ctType, Void p)
        throws RuntimeException {
      return ctType.getElementCtType().accept(this, p);
    }
  }
}
