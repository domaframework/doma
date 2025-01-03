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

import java.util.List;
import java.util.Map;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public interface QueryMeta {

  String getName();

  List<String> getTypeParameterNames();

  List<QueryParameterMeta> getParameterMetas();

  QueryReturnMeta getReturnMeta();

  Map<String, TypeMirror> getBindableParameterTypeMap();

  List<TypeMirror> getThrownTypes();

  ExecutableElement getMethodElement();

  TypeElement getDaoElement();

  QueryKind getQueryKind();

  boolean isVarArgs();

  List<String> getFileNames();

  <R> R accept(QueryMetaVisitor<R> visitor);
}
