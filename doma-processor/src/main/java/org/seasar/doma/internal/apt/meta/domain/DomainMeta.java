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

import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.seasar.doma.internal.apt.cttype.BasicCtType;
import org.seasar.doma.internal.apt.meta.TypeElementMeta;

public interface DomainMeta extends TypeElementMeta {
  TypeMirror getType();

  TypeElement getTypeElement();

  List<String> getTypeVariables();

  List<String> getTypeParameters();

  BasicCtType getBasicCtType();

  TypeMirror getValueType();

  String getFactoryMethod();

  String getAccessorMethod();

  boolean getAcceptNull();

  boolean providesConstructor();

  boolean isParameterized();
}
